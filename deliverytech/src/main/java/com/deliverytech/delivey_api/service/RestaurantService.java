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
    private RestaurantRepository repository;

    public List<RestaurantDTO>findAll(){
        return repository.findAll()
        .stream()
        .map(RestaurantDTO::new)
        .collect(Collectors.toList());
    }

    @Transactional
    public RestaurantDTO save(Restaurant r){
        return new RestaurantDTO(repository.save(r));
    }



}
