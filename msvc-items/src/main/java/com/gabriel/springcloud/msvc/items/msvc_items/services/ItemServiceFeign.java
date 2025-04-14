package com.gabriel.springcloud.msvc.items.msvc_items.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabriel.springcloud.msvc.items.msvc_items.clients.ProductFeignClient;
import com.gabriel.springcloud.msvc.items.msvc_items.models.Item;

import feign.FeignException;

@Service
public class ItemServiceFeign implements ItemService {


    @Autowired
    ProductFeignClient client;

    @Override
    public List<Item> findAll() {

        return this.client.findAll().stream().map( prod -> {
            Random randomizer = new Random();
            return Item.builder()
                        .product(prod)
                        .quantity(randomizer.nextInt(10) + 1 )
                        .build();
        } ).toList();
    }

    @Override
    public Optional<Item> findById(Long id) {
        
        try {
            return Optional.ofNullable( this.client.findById(id).map( prod -> {
                Random randomizer = new Random();
                return Item.builder()
                            .product(prod)
                            .quantity(randomizer.nextInt(10) + 1 )
                            .build();
                } 
                )
                .orElse(null) );
        } catch (FeignException e) {
            return Optional.empty();
        }
    }



}
