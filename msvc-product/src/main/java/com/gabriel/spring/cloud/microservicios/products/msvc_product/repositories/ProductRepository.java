package com.gabriel.spring.cloud.microservicios.products.msvc_product.repositories;

import org.springframework.data.repository.CrudRepository;

import com.gabriel.spring.cloud.microservicios.products.msvc_product.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long>{

}
