package com.gabriel.spring.cloud.microservicios.products.msvc_product.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateProductRequest(
    @NotBlank(message = "El nombre es obligatorio")
    String name,
    @Min(value = 0, message = "El precio del producto debe ser mayor a 0")
    Double price
) { }
