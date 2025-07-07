package com.deliverytech.delivey_api.model;

import lombok.*;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Double price;
    private boolean available;
    private Long restaurantId;
    

    public ProductDTO(Product p) {
        this.name = p.getName();
        this.description = p.getDescription();
        this.category = p.getCategory();
        this.price = p.getPrice();
        this.available = p.isAvailable();
        if(p.getRestaurant() != null){
            this.restaurantId = p.getRestaurant().getId();
        }
    }

    
}