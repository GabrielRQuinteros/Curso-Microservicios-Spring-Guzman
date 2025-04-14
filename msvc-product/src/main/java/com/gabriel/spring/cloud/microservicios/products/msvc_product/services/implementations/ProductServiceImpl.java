package com.gabriel.spring.cloud.microservicios.products.msvc_product.services.implementations;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.gabriel.spring.cloud.microservicios.products.msvc_product.entities.Product;
import com.gabriel.spring.cloud.microservicios.products.msvc_product.repositories.ProductRepository;
import com.gabriel.spring.cloud.microservicios.products.msvc_product.services.interfaces.ProductService;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository repository;

    @Autowired
    private Environment environment;

    @Transactional
    @Override
    public List<Product> findAll() {
        return ((List<Product>)this.repository.findAll()).stream()
                                                        .map( prod -> {
                                                            prod.setPort( Integer.parseInt( environment.getProperty("server.port") ) );
                                                            return prod;
                                                        }).toList();
    }

    @Transactional
    @Override
    public Optional<Product> findById(Long id) {
        return this.repository.findById(id).map( prod -> {
            prod.setPort( Integer.parseInt( environment.getProperty("server.port") ) );
            return prod;
        });
    }



}
