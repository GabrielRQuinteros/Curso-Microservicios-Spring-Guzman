package com.gabriel.springcloud.msvc.items.msvc_items;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


/// ESTA ANNOTATION ME PERMITE USAR FEIGN CLIENTS EN EL PROYECTO
@EnableFeignClients
@SpringBootApplication
public class MsvcItemsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcItemsApplication.class, args);
	}

}
