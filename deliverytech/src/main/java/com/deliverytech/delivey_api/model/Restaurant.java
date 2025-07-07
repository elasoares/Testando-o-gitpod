package com.deliverytech.delivey_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private Double rating; 
    private boolean active; 

    // Construtor para facilitar a criação sem ID (para o service)
    public Restaurant(String name, String address, String phoneNumber, Double rating, boolean active) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.active = active;
    }

    // Construtor para o initializeMockDataIfEmpty para evitar o rating e active no construtor
    public Restaurant(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.rating = 0.0; // Valor padrão
        this.active = true; // Valor padrão
    }
}