package com.gabriel.spring.cloud.microservicios.products.msvc_product.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.gabriel.spring.cloud.microservicios.products.msvc_product.entities.Product;

public interface ProductService {

    public List<Product> findAll();
    public Optional<Product> findById( Long id );

}