package com.deliverytech.delivey_api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data 
@NoArgsConstructor
@AllArgsConstructor 
public class ClientDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private boolean active; 

 
    public ClientDTO(Client c) { 
        this.id = c.getId();
        this.name = c.getName();
        this.email = c.getEmail();
        this.phoneNumber = c.getPhoneNumber();
        this.active = c.isActive(); 
    }

    public ClientDTO(String name, String email, String phoneNumber, boolean active) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.active = active;
    }
}