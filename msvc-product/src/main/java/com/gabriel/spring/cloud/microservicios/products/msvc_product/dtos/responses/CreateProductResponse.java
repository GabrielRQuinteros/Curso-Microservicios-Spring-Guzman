package com.gabriel.spring.cloud.microservicios.products.msvc_product.dtos.responses;

public record CreateProductResponse(
    long id,
    String name,
    Double price
    ) {}
