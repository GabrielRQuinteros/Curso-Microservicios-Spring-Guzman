package com.gabriel.spring.cloud.microservicios.products.msvc_product.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "products")
public class Product {


    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    @Min(value = 0, message = "El valor del producto debe ser mayor a 0")
    private Double price;

    @NotNull
    @JsonIgnore
    private LocalDateTime created_at;

    @NotNull
    @JsonIgnore
    private LocalDateTime updated_at;

    @Transient
    private long port;


    @PrePersist
    protected void onCreate() {
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated_at = LocalDateTime.now();
    }





}
