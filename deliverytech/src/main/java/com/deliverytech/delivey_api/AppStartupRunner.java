/* package com.deliverytech.delivey_api;

import com.deliverytech.delivey_api.service.ClientService;
import com.deliverytech.delivey_api.service.ProductService; // Novo
import com.deliverytech.delivey_api.service.RestaurantService; // Novo
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements CommandLineRunner {

    @Autowired
    private RestaurantService restaurantService; // Injete o RestaurantService

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductService productService; // Injete o ProductService

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n--- Iniciando Runner de inicialização de dados ---");

        // CHAME O RESTAURANT SERVICE PRIMEIRO!
        restaurantService.initializeMockDataIfEmpty();

        // Agora, se quiser, pode chamar os outros (eles podem depender de restaurantes)
        clientService.initializeMockDataIfEmpty();
        productService.initializeMockDataIfEmpty();

        System.out.println("--- Runner de inicialização de dados concluído. ---");
    }
}

 */