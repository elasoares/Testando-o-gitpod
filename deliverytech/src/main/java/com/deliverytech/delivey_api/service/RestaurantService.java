package com.deliverytech.delivey_api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivey_api.model.Restaurant;
import com.deliverytech.delivey_api.model.RestaurantDTO;
import com.deliverytech.delivey_api.repository.RestaurantRepository;
import com.deliverytech.delivey_api.exception.ResourceNotFoundException;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository repository;

    @Transactional
    public Restaurant saveRestaurant(Restaurant restaurant) {
        if (repository.findByNameIgnoreCase(restaurant.getName()).isPresent()) {
            throw new IllegalArgumentException("O nome do restaurante '" + restaurant.getName() + "' já está em uso.");
        }
        return repository.save(restaurant);
    }

    @Transactional(readOnly = true)
    public Optional<Restaurant> findRestaurantById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public RestaurantDTO createRestaurant(Restaurant restaurant) {
        Restaurant savedRestaurant = saveRestaurant(restaurant);
        return new RestaurantDTO(savedRestaurant);
    }

    @Transactional(readOnly = true)
    public List<RestaurantDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(RestaurantDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<RestaurantDTO> findRestaurantDTOById(Long id) {
        return repository.findById(id)
                .map(RestaurantDTO::new);
    }

    @Transactional
    public Optional<RestaurantDTO> updateRestaurant(Long id, Restaurant updatedRestaurant) {
        Restaurant restaurant = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Restaurante com ID " + id + " não encontrado."));

        Optional<Restaurant> existingRestaurantWithName = repository.findByNameIgnoreCase(updatedRestaurant.getName());
        if (existingRestaurantWithName.isPresent() && !existingRestaurantWithName.get().getId().equals(id)) {
            throw new IllegalArgumentException("O nome do restaurante '" + updatedRestaurant.getName() + "' já está em uso por outro restaurante.");
        }

        restaurant.setName(updatedRestaurant.getName());
        restaurant.setAddress(updatedRestaurant.getAddress());
        restaurant.setPhoneNumber(updatedRestaurant.getPhoneNumber());
        restaurant.setRating(updatedRestaurant.getRating());
        restaurant.setCategory(updatedRestaurant.getCategory());

        Restaurant savedRestaurant = repository.save(restaurant);
        return Optional.of(new RestaurantDTO(savedRestaurant));
    }

    @Transactional
    public boolean deactivateRestaurant(Long id) {
        return repository.findById(id).map(restaurant -> {
            if (restaurant.isActive()) {
                restaurant.setActive(false);
                repository.save(restaurant);
                return true;
            }
            return false;
        }).orElseThrow(() -> new ResourceNotFoundException("Restaurante com ID " + id + " não encontrado para inativação."));
    }

    @Transactional
    public boolean activateRestaurant(Long id) {
        return repository.findById(id).map(restaurant -> {
            if (!restaurant.isActive()) {
                restaurant.setActive(true);
                repository.save(restaurant);
                return true;
            }
            return false;
        }).orElseThrow(() -> new ResourceNotFoundException("Restaurante com ID " + id + " não encontrado para ativação."));
    }

    @Transactional
    public void deleteRestaurant(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Restaurante com ID " + id + " não encontrado para deleção.");
        }
        repository.deleteById(id);
    }

    @Transactional
    public void initializeMockDataIfEmpty() {
        if (repository.count() == 0) {
            System.out.println("SERVICE: Inserindo dados iniciais de Restaurantes no H2...");
            
            createRestaurant(new Restaurant(
                "Pizzaria Dev",
                "Rua das Flores, 100",
                "11987654321",
                "Pizzaria",
                4.8,
                true
            ));
            createRestaurant(new Restaurant(
                "Burger Code",
                "Avenida dos Dados, 200",
                "22912345678",
                "Hamburgueria",
                4.5,
                true
            ));
            createRestaurant(new Restaurant(
                "Cantina Java",
                "Travessa do Loop, 300",
                "33998765432",
                "Italiana",
                4.2,
                false
            ));
            System.out.println("SERVICE: Restaurantes iniciais inseridos: " + repository.count());
        } else {
            System.out.println("SERVICE: Banco de dados H2 já possui restaurantes, pulando inicialização.");
        }
    }
}