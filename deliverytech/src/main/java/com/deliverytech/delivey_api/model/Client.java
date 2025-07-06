package com.deliverytech.delivey_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data // Gera getters, setters, toString, equals, hashCode (Lombok)
@NoArgsConstructor // Construtor sem argumentos (Lombok)
@AllArgsConstructor // Construtor com todos os argumentos (Lombok)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String telephone;
    private boolean active; 

 
    public Client(String name, String email, String telephone, boolean active) {
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.active = active;
    }
}