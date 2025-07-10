package com.deliverytech.delivey_api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.deliverytech.delivey_api.validation.ValidTelephone;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {
    private Long id;
    private String name;
    private String address;
    @NotBlank(message = "O campo telefone é obrigatório.")
    @ValidTelephone
    private String phoneNumber;
    private Double rating;
    private boolean active; 

    public RestaurantDTO(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
        this.phoneNumber = restaurant.getPhoneNumber();
        this.rating = restaurant.getRating();
        this.active = restaurant.isActive();
    }
}