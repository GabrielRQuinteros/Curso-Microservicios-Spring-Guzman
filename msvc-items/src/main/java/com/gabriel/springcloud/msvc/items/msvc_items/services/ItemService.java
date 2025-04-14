package com.gabriel.springcloud.msvc.items.msvc_items.services;

import java.util.List;
import java.util.Optional;

import com.gabriel.springcloud.msvc.items.msvc_items.models.Item;

public interface ItemService {

    public List<Item> findAll();
    public Optional<Item> findById(Long id);
}
