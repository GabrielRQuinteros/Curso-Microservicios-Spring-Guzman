package com.gabriel.springcloud.msvc.items.msvc_items.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.springcloud.msvc.items.msvc_items.dtos.requests.CreateProductRequest;
import com.gabriel.springcloud.msvc.items.msvc_items.dtos.requests.UpdateProductRequest;
import com.gabriel.springcloud.msvc.items.msvc_items.models.Item;
import com.gabriel.springcloud.msvc.items.msvc_items.models.Product;
import com.gabriel.springcloud.msvc.items.msvc_items.services.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @RefreshScope es una anotación de Spring que permite actualizar el Environment
 * en caso de que la configuración del microservicio se modifique, ya sea que esté alojada
 * en un repositorio Git o en forma local.
 *
 * Esto permite que las propiedades se actualicen sin necesidad de reiniciar el servidor.
 * Es decir, el microservicio (por ejemplo, el de Items) puede aplicar los nuevos valores
 * de configuración en caliente, sin ser dado de baja.
 */
@RefreshScope
@RestController
@RequestMapping("")
public class ItemController {

    private final ItemService service;
    private final CircuitBreakerFactory cbf;
    private final Logger logger = LoggerFactory.getLogger( ItemController.class );

    /// Esta es una property que es importada desde el servidor de configuraciones
    @Value("${configuracion.texto}")
    private String texto;

    @Autowired
    private Environment env;


    @GetMapping("/fetch-configs")
    public ResponseEntity<?> fetchCpnfigs(@Value("${server.port}") String puerto) {
        Map<String, String> json = new HashMap<>();
        json.put("text", texto);
        json.put("port", puerto);
        logger.info("PUERTO: " + puerto );
        logger.info("TEXTO: " + texto );


        if( env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev") ) {
            json.put("autor", env.getProperty("configuracion.autor.nombre") );
            json.put("email", env.getProperty("configuracion.autor.email") );
        }

        return ResponseEntity.ok(json);
    }
    


    public ItemController(CircuitBreakerFactory cbf, ItemService service) {
        this.cbf = cbf;
        this.service = service;
    }


    @GetMapping("")
    public ResponseEntity<List<Item>> findAll( @RequestParam("name") String name  ) {
        System.out.println("Este es el PARAM de mi FILTER: " + name );
        return ResponseEntity.ok(this.service.findAll());
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<?> findById1(@PathVariable Long id) {
    //     Optional<Item> itemOpt = cbf.create("items").run( () -> this.service.findById(id)
    //     , e -> {
    //         logger.error(e.getMessage());
            
    //         Item itemCortoCircuito = new Item(77L, 15, Product.builder()
    //                                                          .created_at(LocalDateTime.now())
    //                                                          .id(99L)
    //                                                          .name("Cable --> CortoCircuito")
    //                                                          .price(250.5)
    //                                                          .port(1234)
    //                                                          .build()
    //                                                          );
    //         return Optional.of(itemCortoCircuito);
    //     });
    //     if( itemOpt.isPresent() )
    //         return ResponseEntity.ok().body(itemOpt.get());
    //     return ResponseEntity.status( HttpStatus.NOT_FOUND.value())
    //                          .body( Map.of( "message", "Producto no encontrado en microservicio de productos" ) )
    //                          ;

    // }


    // /** @CircuitBreaker funciona SOLO CON CONFIGURACIONES HECHAS en application.properties o yml.
    //  *  NO FUNCIONA CON CONFIGURACIONES programaticas como la hecha en AppConfig.java
    //  */
    // @CircuitBreaker(name = "items", fallbackMethod = "caminoAlternativoCortoCircuito" ) 
    // @GetMapping("/{id}")
    // public ResponseEntity<?> findById2(@PathVariable Long id) {
        
    //     Optional<Item> itemOpt = this.service.findById(id);

    //     if( itemOpt.isPresent() )
    //         return ResponseEntity.ok().body(itemOpt.get());
    //     return ResponseEntity.status( HttpStatus.NOT_FOUND.value())
    //                          .body( Map.of( "message", "Producto no encontrado en microservicio de productos" ) )
    //                          ;

    // }
    /** @CircuitBreaker funciona SOLO CON CONFIGURACIONES HECHAS en application.properties o yml.
     *  NO FUNCIONA CON CONFIGURACIONES programaticas como la hecha en AppConfig.java
     */


    @CircuitBreaker(name = "items", fallbackMethod = "caminoAlternativoCortoCircuitoCompletableFuturo" ) 
    @TimeLimiter(name = "items") /// ESTO ME SIRVE PARA SETEARLE EL TIME OUT SOLAMENTE. HAY QUE COMBINARLO CON @CircuitBreaker.
    @GetMapping("/{id}")
    public CompletableFuture<?> findById2(@PathVariable Long id) {
        /// Se envuelve el codigo en un completable futuro, porque esto permite poder calcular cuanto tiempo lleva la peticion asincronica
        /// y en base a esto saber cuando hay o no TimeOut.
        return CompletableFuture.supplyAsync( () -> {
                Optional<Item> itemOpt = this.service.findById(id);
                if( itemOpt.isPresent() )
                    return ResponseEntity.ok().body(itemOpt.get());
                return ResponseEntity.status( HttpStatus.NOT_FOUND.value())
                                     .body( Map.of( "message", "Producto no encontrado en microservicio de productos" ) )
                                     ;
        });
        

    }

    public ResponseEntity<?> caminoAlternativoCortoCircuito( Throwable e ) {
        logger.error(e.getMessage());
        Item itemCortoCircuito = new Item(77L, 15, Product.builder()
                                                            .created_at(LocalDateTime.now())
                                                            .id(99L)
                                                            .name("Cable --> CortoCircuito")
                                                            .price(250.5)
                                                            .port(1234)
                                                            .build()
                                                            );
        return ResponseEntity.ok(itemCortoCircuito);
    
    }

    /// SE CREA ESTE METODO PORQUE TIENE QUE TENER EL MISMO TIPO DE RETORNO QUE EL CONTROLADOR
    public CompletableFuture <?> caminoAlternativoCortoCircuitoCompletableFuturo( Throwable e ) {
        return CompletableFuture.supplyAsync( () -> {
            logger.error(e.getMessage());
            Item itemCortoCircuito = new Item(77L, 15, Product.builder()
                                                            .created_at(LocalDateTime.now())
                                                            .id(99L)
                                                            .name("Cable --> CortoCircuito")
                                                            .price(250.5)
                                                            .port(1234)
                                                            .build()
                                                            );
                                                            return ResponseEntity.ok(itemCortoCircuito);
            } );
    }


    @PostMapping("")
    public ResponseEntity<?> create( @Validated @RequestBody CreateProductRequest requestBody){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.create(requestBody));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update ( @Validated @RequestBody UpdateProductRequest requestBody, @PathVariable @NotNull Long id ){
        return ResponseEntity.ok(this.service.update(requestBody, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete ( @PathVariable @NotNull Long id ) {
        return ResponseEntity.ok(this.service.delete(id));
    }
    

    


}
