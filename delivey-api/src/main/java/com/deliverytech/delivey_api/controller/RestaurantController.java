package com.deliverytech.delivey_api.controller;

import com.deliverytech.delivey_api.model.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivey_api.service.RestaurantService;

@RequestMapping("/api/v1/restaurants")
@RestController
public class RestaurantController {
    @Autowired 
    private RestaurantService restaurantService;

    @GetMapping
    public List<RestaurantDTO> getAllRestaurant(){
        return restaurantService.findAll();
    }
}
