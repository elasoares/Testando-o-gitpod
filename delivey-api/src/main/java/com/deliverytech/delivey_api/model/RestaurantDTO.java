package com.deliverytech.delivey_api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantDTO {
    private Long id;
    private String name;
    private String address;

   
    public RestaurantDTO(Restaurant restaurant){
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
    }
    
     public RestaurantDTO(Long id, String name, String address){
        this.id = id;
        this.name = name;
        this.address = address;
    }

}
