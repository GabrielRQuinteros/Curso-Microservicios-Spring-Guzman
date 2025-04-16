package com.gabriel.spring.cloud.microservicios.products.msvc_product.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.gabriel.spring.cloud.microservicios.products.msvc_product.dtos.requests.CreateProductRequest;
import com.gabriel.spring.cloud.microservicios.products.msvc_product.dtos.requests.UpdateProductRequest;
import com.gabriel.spring.cloud.microservicios.products.msvc_product.entities.Product;
import com.gabriel.spring.cloud.microservicios.products.msvc_product.services.interfaces.ProductService;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BadRequestException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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


    @PostMapping("")
    public ResponseEntity<?> create( @Validated @RequestBody CreateProductRequest requestBody) {
        try {
            return ResponseEntity.status( HttpStatus.CREATED).body(this.service.create(requestBody));
        } catch (BadRequestException e ) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (InternalServerError e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update ( @Validated @RequestBody UpdateProductRequest requestBody, @PathVariable @NotNull Long id ) {
        try {
            return ResponseEntity.ok( this.service.update(requestBody, id) );
        } catch (BadRequestException e ) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (InternalServerError e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete ( @PathVariable @NotNull Long id ) {
        try {
            Optional<Product> prodOpt = this.service.delete(id);
            if( prodOpt.isPresent() )
                return ResponseEntity.ok( prodOpt.get() );
            else
                return ResponseEntity.notFound().build();
        } catch (BadRequestException e ) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (InternalServerError e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

}
