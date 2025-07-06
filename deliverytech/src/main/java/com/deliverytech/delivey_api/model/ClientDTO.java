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
    private String telephone;
    private boolean active; 

 
    public ClientDTO(Client c) { 
        this.id = c.getId();
        this.name = c.getName();
        this.email = c.getEmail();
        this.telephone = c.getTelephone();
        this.active = c.isActive(); 
    }

    public ClientDTO(String name, String email, String telephone, boolean active) {
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.active = active;
    }
}