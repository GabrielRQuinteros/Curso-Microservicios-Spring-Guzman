package com.gabriel.springcloud.msvc.items.msvc_items.services;

import java.util.List;
import java.util.Optional;

import com.gabriel.springcloud.msvc.items.msvc_items.dtos.requests.CreateProductRequest;
import com.gabriel.springcloud.msvc.items.msvc_items.dtos.requests.UpdateProductRequest;
import com.gabriel.springcloud.msvc.items.msvc_items.models.Item;
import com.gabriel.springcloud.msvc.items.msvc_items.models.Product;

public interface ItemService {

    public List<Item> findAll();
    public Optional<Item> findById(Long id);
    public Product create(CreateProductRequest requestBody);
    public Product update (UpdateProductRequest requestBody, Long id);
    public Product delete (Long id);

}
