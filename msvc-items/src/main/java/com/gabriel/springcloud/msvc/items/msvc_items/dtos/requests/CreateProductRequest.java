package com.gabriel.springcloud.msvc.items.msvc_items.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateProductRequest(
    @NotBlank(message = "El nombre es obligatorio")
    String name,
    @Min(value = 0, message = "El precio del producto debe ser mayor a 0")
    Double price
) { }
