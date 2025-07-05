package com.deliverytech.delivey_api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deliverytech.delivey_api.model.Restaurant;
import com.deliverytech.delivey_api.model.RestaurantDTO;
import com.deliverytech.delivey_api.repository.RestaurantRepository;

import jakarta.transaction.Transactional;


@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<RestaurantDTO>findAll(){
        List<Restaurant> mockRestaurant = List.of(
            new Restaurant("Restaurante do bolsonaro", "Rua da paixão, 123"),
            new Restaurant("Restaurante do lula", "Rua da paixão, 123")
        );
        return mockRestaurant.stream()
        .map(RestaurantDTO::new)
        .collect(Collectors.toList());
    }

    @Transactional
    public RestaurantDTO save(Restaurant r){
        Restaurant saved = restaurantRepository.save(r);
        return new RestaurantDTO(saved);
    }

    @Transactional
public void initializeMockDataIfEmpty() {
    // Isso assume que RestaurantRepository tem um método count()
    if (restaurantRepository.count() == 0) {
        System.out.println("Inserindo dados iniciais no H2...");
        save(new Restaurant("Burger Queen", "Rua do Hamburguer, 123"));
        save(new Restaurant("Pastelaria do China", "Av. Oriental, 456"));
    } else {
        System.out.println("Banco de dados H2 já possui dados, pulando inicialização.");
    }
}

}
