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
        List<Restaurant> restaurant = restaurantRepository.findAll();
        return restaurant.stream()
        .map(RestaurantDTO::new)
        .collect(Collectors.toList());
    }

    @Transactional
    public RestaurantDTO save(Restaurant r){
        Restaurant saved = restaurantRepository.save(r);
        return new RestaurantDTO(saved);
    }



}
