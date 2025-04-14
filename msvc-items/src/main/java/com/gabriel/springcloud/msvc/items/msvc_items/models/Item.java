package com.gabriel.springcloud.msvc.items.msvc_items.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private Long id;
    private int quantity;
    private Product product;

    private Double getSubtotal() {
        return quantity * product.getPrice();
    }


}
