package com.deliverytech.delivey_api.controller;

import com.deliverytech.delivey_api.model.Restaurant;
import com.deliverytech.delivey_api.model.RestaurantDTO;
import com.deliverytech.delivey_api.service.RestaurantService;

import jakarta.validation.Valid;

import com.deliverytech.delivey_api.exception.ResourceNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<RestaurantDTO> createRestaurant(@Valid @RequestBody RestaurantDTO restaurantDTO) {
        try {
            Restaurant restaurant = new Restaurant();
            restaurant.setName(restaurantDTO.getName());
            restaurant.setAddress(restaurantDTO.getAddress());
            restaurant.setPhoneNumber(restaurantDTO.getPhoneNumber());
            restaurant.setCategory(restaurantDTO.getCategory()); // Adicionado o category aqui
            restaurant.setRating(restaurantDTO.getRating());
            restaurant.setActive(restaurantDTO.isActive());
            RestaurantDTO newRestaurant = restaurantService.createRestaurant(restaurant);
            return ResponseEntity.status(HttpStatus.CREATED).body(newRestaurant);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao cadastrar restaurante: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping
    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        return restaurantService.findRestaurantById(id)
                .map(restaurant -> new ResponseEntity<>(restaurant, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable Long id, @Valid @RequestBody RestaurantDTO restaurantDTO) {
        try {
            Restaurant restaurant = new Restaurant();
            restaurant.setName(restaurantDTO.getName());
            restaurant.setAddress(restaurantDTO.getAddress());
            restaurant.setPhoneNumber(restaurantDTO.getPhoneNumber());
            restaurant.setCategory(restaurantDTO.getCategory());
            restaurant.setRating(restaurantDTO.getRating());
            restaurant.setActive(restaurantDTO.isActive());
            return restaurantService.updateRestaurant(id, restaurant)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao atualizar restaurante: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        try {
            restaurantService.deleteRestaurant(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}