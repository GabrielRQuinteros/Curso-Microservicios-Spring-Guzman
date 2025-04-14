package com.gabriel.springcloud.msvc.items.msvc_items.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.springcloud.msvc.items.msvc_items.models.Item;
import com.gabriel.springcloud.msvc.items.msvc_items.services.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

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


    /** @CircuitBreaker funciona SOLO CON CONFIGURACIONES HECHAS en application.properties o yml.
     *  NO FUNCIONA CON CONFIGURACIONES programaticas como la hecha en AppConfig.java
     */
    @CircuitBreaker(name = "items") 
    @GetMapping("/{id}")
    public ResponseEntity<?> findById2(@PathVariable Long id) {
        
        Optional<Item> itemOpt = this.service.findById(id);

        if( itemOpt.isPresent() )
            return ResponseEntity.ok().body(itemOpt.get());
        return ResponseEntity.status( HttpStatus.NOT_FOUND.value())
                             .body( Map.of( "message", "Producto no encontrado en microservicio de productos" ) )
                             ;

    }
    

    


}
