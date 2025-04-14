package com.gabriel.springcloud.msvc.items.msvc_items.services;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.gabriel.springcloud.msvc.items.msvc_items.models.Item;
import com.gabriel.springcloud.msvc.items.msvc_items.models.Product;


@Primary
@Service
public class ItemServiceWebClient implements ItemService{

    private final WebClient.Builder client;

    

    public ItemServiceWebClient(@Autowired Builder client) {
        this.client = client;
    }

    @Override
    public List<Item> findAll() {
        Random randomizer = new Random();
        return  this.client.build()
                            .get()                                              // TIPO DE PETICIÓN
                            .accept(MediaType.APPLICATION_JSON)                 // FORMATO EN EL QUE SE RECIBE LA RESPUESTA HTTP 
                            .retrieve()
                            .bodyToFlux( Product.class )          // TIPO DE DATO DE LA RESPUESTA / COLECCIÓN
                            .map( p -> Item.builder()
                                            .quantity(randomizer.nextInt(10)+1)
                                            .product(p)
                                            .build()
                                            )
                            .collectList()                                     // AL SER UNA LISTA LA RESPUESTA lA CASTEA A UNA LISTA
                            .block();                                          // HACE QUE LA PETICIÓN SEA BLOQUEANTE
        
    }

    @Override
    public Optional<Item> findById(Long id) {
        Map<String, Object> params = Map.of("id", id);
        Random randomizer = new Random();
        // try {
            return Optional.of(
                this.client.build()
                        .get()
                        .uri("/{id}", params)
                        .accept(MediaType.APPLICATION_JSON)                
                        .retrieve()
                        .bodyToMono( Product.class )
                        .map( p -> Item.builder()
                                                .quantity(randomizer.nextInt(10)+1)
                                                .product(p)
                                                .build()
                                                )
                        .block()
                );
        // } catch (WebClientResponseException e) {
        //     return Optional.empty();
        // }

    }




}
