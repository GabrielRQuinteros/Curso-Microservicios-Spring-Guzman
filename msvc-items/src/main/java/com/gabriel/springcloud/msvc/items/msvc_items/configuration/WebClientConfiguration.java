package com.gabriel.springcloud.msvc.items.msvc_items.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;



/** Clase que me permite configurar los web clients
 */
@Configuration
public class WebClientConfiguration {

    @Bean
    @LoadBalanced /// -> Me permite que mi Web Client implemente un balanceo de carga.
    public WebClient.Builder webClient (@Value(value = "${config.msvc-product.base.url}") String baseUrl ){
        return WebClient.builder().baseUrl(baseUrl);
    }  

}
