package com.gabriel.spring.cloud.microservicios.products.msvc_product.services.implementations;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.gabriel.spring.cloud.microservicios.products.msvc_product.dtos.requests.CreateProductRequest;
import com.gabriel.spring.cloud.microservicios.products.msvc_product.dtos.requests.UpdateProductRequest;
import com.gabriel.spring.cloud.microservicios.products.msvc_product.entities.Product;
import com.gabriel.spring.cloud.microservicios.products.msvc_product.repositories.ProductRepository;
import com.gabriel.spring.cloud.microservicios.products.msvc_product.services.interfaces.ProductService;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;

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

    @Transactional()
    @Override
    public Product create( CreateProductRequest requestBody ) throws InternalServerError {
        
        Product newProduct = Product.builder()
                                    .name( requestBody.name() )
                                    .price( requestBody.price() )
                                    .build();
        try {
            return this.repository.save( newProduct );
        } catch (Exception e) {
            throw new InternalServerErrorException("Se hay producido un error al tratar de guardar el producto");
        }

    }



    @Transactional()
    @Override
    public Product update( UpdateProductRequest requestBody, Long id ) throws BadRequestException, InternalServerErrorException {
        try {
            Optional<Product> productOpt = this.findById(id);
            return this.repository.save(productOpt.orElseThrow( () -> new BadRequestException( "El producto indicado no0 existe." ) ));
        } catch( BadRequestException e ){
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al intentar actualizar el producto en base de datos");
        }
    }


    @Transactional()
    @Override
    public Optional<Product> delete ( Long id ) throws BadRequestException, InternalServerErrorException {
        try {
            if ( Objects.isNull(id) )
                throw new BadRequestException("No se suministro el Id del Producto.");
            Optional<Product> productOpt = this.findById(id);
            if( productOpt.isPresent() )
                this.repository.deleteById(id);
            return productOpt;
        } catch( BadRequestException e ){
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al intentar eliminar el producto en base de datos");
        }
    }






}
