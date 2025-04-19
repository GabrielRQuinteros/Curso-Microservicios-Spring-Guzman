package com.gabriel.spring.cloud.microservicios.products.msvc_product.services.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.gabriel.spring.cloud.microservicios.products.msvc_product.dtos.requests.CreateProductRequest;
import com.gabriel.spring.cloud.microservicios.products.msvc_product.dtos.requests.UpdateProductRequest;
import com.gabriel.spring.cloud.microservicios.products.msvc_product.entities.Product;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;

public interface ProductService {

    public List<Product> findAll();
    public Optional<Product> findById( Long id );
    public Product create(CreateProductRequest requesBody) throws InternalServerError;
    public Optional<Product> update( UpdateProductRequest requestBody, Long id ) throws InternalServerError, BadRequestException ;
    public Optional<Product> delete ( Long id ) throws BadRequestException, InternalServerErrorException;

}