package com.gabriel.springcloud.msvc.items.msvc_items.clients;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gabriel.springcloud.msvc.items.msvc_items.dtos.requests.CreateProductRequest;
import com.gabriel.springcloud.msvc.items.msvc_items.dtos.requests.UpdateProductRequest;
import com.gabriel.springcloud.msvc.items.msvc_items.models.Product;

import jakarta.validation.constraints.NotNull;



/**
 * NOTAS
 * -----
 * Me crea un Client que hace peticiones  Http al micro-servicio de productos.
 * 1- Feign por defecto te crea clientes con Balanceo de Carga Activo pr defecto.
 */
@FeignClient(name = "msvc-product")
public interface ProductFeignClient {


    @GetMapping("")
    public List<Product> findAll();

    @GetMapping("/{id}")
    public Optional<Product> findById(@PathVariable Long id);

    @PostMapping("")
    public Product create( @Validated @RequestBody CreateProductRequest requestBody);

    @PatchMapping("/{id}")
    public Product update ( @Validated @RequestBody UpdateProductRequest requestBody, @PathVariable @NotNull Long id );
    
    @DeleteMapping("/{id}")
    public Product delete ( @PathVariable @NotNull Long id );



}
