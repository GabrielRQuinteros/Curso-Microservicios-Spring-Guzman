package com.gabriel.spring.cloud.microservicios.products.msvc_product.controllers;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.spring.cloud.microservicios.products.msvc_product.entities.Product;
import com.gabriel.spring.cloud.microservicios.products.msvc_product.services.interfaces.ProductService;

@RestController
@RequestMapping("")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("")
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok( this.service.findAll() );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {

        if( id.equals(10L)) {
            throw new IllegalStateException("Producto no encontrado");
        }

        if( id.equals(7L)) {
            try {
                TimeUnit.SECONDS.sleep(2L);
            } catch (InterruptedException e) {
                System.out.println("INTERRUPCIÓN AL SLEEP");
            }
        }
        
        Optional<Product> productOpt = this.service.findById(id);
        if( productOpt.isPresent() )
            return ResponseEntity.ok(productOpt.get());
        return ResponseEntity.notFound().build();
    }
    
    


}
