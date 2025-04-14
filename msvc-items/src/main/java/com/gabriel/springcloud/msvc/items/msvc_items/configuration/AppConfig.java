package com.gabriel.springcloud.msvc.items.msvc_items.configuration;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class AppConfig {


    /** RECORDATORIO COMO FUNCIONA Resilience4JCircuitBreaker
     * -------------------------------------------------------
     * Este sistema de cortocircuitos sirve para evaluar si hay zonas de código que estan experimentando fallos o esperas significativas.
     * Permite ofrecer rutas alternativas al flujo principal para mantener la Alta disponibilidad del sistema.
     * 
     * Se destacan 3 estados para estas zonas de código:
     * 
     * Cerrado. --> Estado que representa que el flujo principal del sistema esta funcionando correctamente sin excepciones
     *              o esperas significativas.
                    Se puede pasar a estado Abierto desde este estado, si ocurren fallos o esperas que cumplen con unas condiciones
                    tanto de fallos o de esperas en relación a unas proporciones definidas por código.
     *
     * Abierto --> Estado que indica que hay Fallos (excepciones) o esperas significativas "repetidas", en esa zona de código evaluada.
     *             Este estado indica que, se la ruta alternativa de código como por defecto por un tiempo para mantener la
     *             la alta disponibilidad del sistema.
     *             Luego de un tiempo designado, pasa al estado "Semi-abierto" para evaluar si el flujo principal sigue experimentando
     *             problemas o no.    
     * 
     * Semi-abierto --> En este estado se retoma el flujo principal por un corto tiempo evaluando si 
     *                  la causa del cortocircuito persiste o no en la ruta principal.
     *                  1- EN caso de persistir --> Se vuelve a estado abierto y se usa la ruta alternativa por otro tiempo mas
     *                  2- En caso que se solucione el problema --> Pasa a estado cerrado y se sigue con la ruta principal. 
     * 
     * 
     * IMPORTANTE: Cuando el flujo principal falla, siempre se pasa al flujo alternativo. El tema es que cuando falla varias veces se establece como el
     *              flujo por defecto el alternativo.
     *              OJO --> No pensar que cuando decimos que el flujo principal falla, se va a lanzar el error y no se resuelve en el flujo alternativo,
     *                      Si el flujo principal falla SIEMPRE  se trata con el alternativo.
     *                       
     */



    /** En este método se customiza la cantidad de excepciones o de tiempos de espera que se consideran "Excesivos"
     *  y que desatan el cortocircuito en el sistema.
     *  Como asi también el tiempo en el que se mantiene en un estado semi-abierto abierto y otras cosas mas.
     *  En resumen: Se configuran los parametros del sistema de cortocircuitos para cada zona de código.
     */
    @Bean
    Customizer<Resilience4JCircuitBreakerFactory> customizerCircuitBreaker() {
        return ( factory ) -> {
            /**
             *  EL ID es el ID que le dimos al CircuitBreaker cuando lo colocamos en el controller.
             * EN el controller lo llamamos "item".
             * Si queremos, podemos colocar una serie de IF o switchs para manejar grupos de CircuitsBreaker con distintas 
             * configuraciones que se aplican a Cortocircuitos con ids diferentes que tengamos en el sistema.  
             */
            factory.configureDefault( id -> new Resilience4JConfigBuilder(id)
                                                        .circuitBreakerConfig(
                                                            CircuitBreakerConfig.custom()
                                                            
                                                            /// --- CONFIGURACION DE CORTOCIRCUITOS POR EXCEPCIONES -------/////
                                                            // El cortocircuito evalua las ultimas 10 llamadas para pasar de estado Cerrado -> Abierto
                                                            .slidingWindowSize(10) 
                                                            // SI mas del 50% de las llamadas falla se habre el circuito
                                                            .failureRateThreshold(50)
                                                            // Durante 10 segundos el cuircuito esta en estado abierto hasta pasar a 
                                                            // semi-abierto donde evalua si se vuelve a abrir o se cierra.
                                                            
                                                            
                                                            /// --- CONFIGURACION DE ESTADO SEMI- ABIERTO ----------//
                                                            .waitDurationInOpenState(Duration.ofSeconds(10L))
                                                            // Se evalua 5 peticiones cuando esta en estado Semi-Abierto
                                                            .permittedNumberOfCallsInHalfOpenState(5)

                                                            ///--- CONFIGURACIÓN DE CORTOCIRCUITOS POR LLAMADAS LENTAS ---//
                                                            // Se indica cuando una llamada es lenta
                                                            .slowCallDurationThreshold(Duration.ofSeconds(1))
                                                            // Se indica el porcentaje de llamadas lentas. Osea del 50 %
                                                            .slowCallRateThreshold(50)

                                                            .build()
                                                        )

                                                        /// ----- CONFIGURACION DE LIMITE DE LAS LLAMADAS  -----///
                                                        /// NOTA: es diferente de las llamadas lentas. Aca si la llamada esta durando mas de 3 segundos
                                                        /// Lanza una EXCEPTION de TimeOutExceptio y se contabiliza como una excepcion osea como un Failure
                                                        /// mas de lo configurado arriba de Excepciones.
                                                        /// DIFERENCIA DE LAS LLAMADAS LENTAS: Este cancela la llamada si dura mas de 3 segundos.
                                                        /// En llamadas lentas, la llamada no se cancela.
                                                        .timeLimiterConfig(
                                                                            TimeLimiterConfig.custom()
                                                                                            .timeoutDuration( Duration.ofSeconds(3L))
                                                                                            .build()
                                                                                            )
                                                        .build()
                                                        /// RESUMEN:
                                                        /// Hay EXCEPCION --> Camino Alternativo --> Contabiliza para cortocircuito
                                                        /// Hay TimeOut --> Lanza Excepcion --> Camino Alternativo --> Contabiliza para cortocircuito
                                                        /// Hay llamada Lenta --> Camino Principal --> Contabiliza para cortocircuito
            );
        };
    }

}
