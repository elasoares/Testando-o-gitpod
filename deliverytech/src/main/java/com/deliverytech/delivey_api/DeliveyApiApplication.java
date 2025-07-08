package com.deliverytech.delivey_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.deliverytech.delivey_api.service.ClientService;
import com.deliverytech.delivey_api.service.CostumerOrderService;
import com.deliverytech.delivey_api.service.ProductService;
import com.deliverytech.delivey_api.service.RestaurantService;



@SpringBootApplication
public class DeliveyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveyApiApplication.class, args);
		
	}
	@Bean
    public CommandLineRunner run(ClientService clientService, RestaurantService restaurantService, ProductService productService, CostumerOrderService costumerOrderService) {
        return args -> {
            // A ordem importa: clientes e restaurantes precisam existir antes de produtos,
            // e todos precisam existir antes dos pedidos.
            clientService.initializeMockDataIfEmpty();
            restaurantService.initializeMockDataIfEmpty();
            productService.initializeMockDataIfEmpty();
            costumerOrderService.initializeMockDataIfEmpty(); // Inicializa os dados de pedidos
        };
    }
}
