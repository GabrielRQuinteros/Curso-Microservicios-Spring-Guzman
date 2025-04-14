package com.gabriel.springcloud.msvc.items.msvc_items.clients;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gabriel.springcloud.msvc.items.msvc_items.models.Product;



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

}
