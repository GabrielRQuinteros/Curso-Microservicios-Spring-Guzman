package com.gabriel.springcloud.msvc.items.msvc_items.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Product {

    private Long id;
    private String name;
    private Double price;
    private LocalDateTime created_at;
    private long port;

}
