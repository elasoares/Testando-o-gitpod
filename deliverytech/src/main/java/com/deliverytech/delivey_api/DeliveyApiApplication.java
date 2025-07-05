package com.deliverytech.delivey_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.deliverytech.delivey_api.service.RestaurantService;


@SpringBootApplication
public class DeliveyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveyApiApplication.class, args);
		

	
	}
	@Bean
    public CommandLineRunner initData(RestaurantService restaurantService) {
        return args -> {
             restaurantService.initializeMockDataIfEmpty(); 
             System.out.println("Dados iniciais verificados/inseridos.");
        };
	}

}
