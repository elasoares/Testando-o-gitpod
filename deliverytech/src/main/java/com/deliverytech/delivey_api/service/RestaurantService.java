package com.deliverytech.delivey_api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Use Spring's Transactional

import com.deliverytech.delivey_api.model.Restaurant;
import com.deliverytech.delivey_api.model.RestaurantDTO;
import com.deliverytech.delivey_api.repository.RestaurantRepository;
import com.deliverytech.delivey_api.exception.ResourceNotFoundException;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository repository;

    // THIS IS THE CRUCIAL METHOD FOR initializeMockDataIfEmpty()
    // It must accept a Restaurant entity and return the saved Restaurant entity.
    @Transactional
    public Restaurant saveRestaurant(Restaurant restaurant) {
        // Optional: Add unique name validation here if this is the primary save method
        if (repository.findByNameIgnoreCase(restaurant.getName()).isPresent()) {
            throw new IllegalArgumentException("O nome do restaurante '" + restaurant.getName() + "' já está em uso.");
        }
        return repository.save(restaurant);
    }

    // The method used by ProductService to retrieve a Restaurant ENTITY
    @Transactional(readOnly = true)
    public Optional<Restaurant> findRestaurantById(Long id) { // <--- Returns Optional<Restaurant> (ENTITY)
        return repository.findById(id);
    }

    // Your existing methods below:

    @Transactional
    public RestaurantDTO createRestaurant(Restaurant restaurant) {
        // Can use saveRestaurant internally after validations, or keep separate
        Restaurant savedRestaurant = saveRestaurant(restaurant); // Reuse the saving logic
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
            // Use the saveRestaurant method that returns the entity
            saveRestaurant(Restaurant.builder().name("Pizzaria Dev").address("Rua das Flores, 100").phoneNumber("11987654321").rating(4.8).active(true).products(new ArrayList<>()).build());
            saveRestaurant(Restaurant.builder().name("Burger Code").address("Avenida dos Dados, 200").phoneNumber("22912345678").rating(4.5).active(true).products(new ArrayList<>()).build());
            saveRestaurant(Restaurant.builder().name("Cantina Java").address("Travessa do Loop, 300").phoneNumber("33998765432").rating(4.2).active(false).products(new ArrayList<>()).build());
            System.out.println("SERVICE: Restaurantes iniciais inseridos: " + repository.count());
        } else {
            System.out.println("SERVICE: Banco de dados H2 já possui restaurantes, pulando inicialização.");
        }
    }
}