package com.gabriel.springcloud.msvc.items.msvc_items.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.springcloud.msvc.items.msvc_items.models.Item;
import com.gabriel.springcloud.msvc.items.msvc_items.models.Product;
import com.gabriel.springcloud.msvc.items.msvc_items.services.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("")
public class ItemController {

    private final ItemService service;
    private final CircuitBreakerFactory cbf;
    private final Logger logger = LoggerFactory.getLogger( ItemController.class );


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
    

    


}
