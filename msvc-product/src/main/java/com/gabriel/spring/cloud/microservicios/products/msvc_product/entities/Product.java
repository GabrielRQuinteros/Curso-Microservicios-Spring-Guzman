package com.gabriel.spring.cloud.microservicios.products.msvc_product.entities;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    private String name;

    private Double price;

    private LocalDateTime created_at;

    @Transient
    private long port;

}
