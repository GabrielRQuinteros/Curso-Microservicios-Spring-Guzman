package com.gabriel.springcloud.app.gateway.msvc_gateway_server.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;



@Component
public class SampleGlobalFilter implements GlobalFilter, Ordered {

    private Logger logger = LoggerFactory.getLogger(SampleGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Ejecutando el filtro del Request PRE");

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
            .header("token", "abcdefg")
            .build();

        ServerWebExchange mutatedExchange = exchange.mutate()
            .request(mutatedRequest)
            .build();

        return chain.filter(mutatedExchange).then(Mono.fromRunnable(() -> {
            logger.info("Ejecutando el filtro POST Response");

            String token = mutatedExchange.getRequest().getHeaders().getFirst("token");
            logger.info("token: " + token);

            mutatedExchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "red").build());
            // mutatedExchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        }));
    }


    /** Implementar la interfaz Ordered, junto con este Método para
     *  definir el ORDEN en que se ejecutan los WebFilter estos. En este caso, este es el filtro numero
     *  100 que se ejecuta, si devolviera 1 el Método sería el 
     *  Primer filtro en ejecutarse -> En la sección de PRE
     *  Ultimo filtro en ejecutarse --> la sección de POST.
     *  El comportamiento es el mismo que en la programación orientada a objetos y demas Web Filters.
     * 
     *  Esto diferencia de JAKARTA, recordar que los Web filters se ejecutan en el orden que están declarados en el POM.
     */
    @Override
    public int getOrder() {
        return 100;
    }

}
