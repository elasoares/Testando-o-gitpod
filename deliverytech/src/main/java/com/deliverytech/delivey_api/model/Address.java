package com.deliverytech.delivey_api.model;

import jakarta.persistence.Embeddable; // Importar para @Embeddable
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable // Indica que esta classe pode ser incorporada em outras entidades
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    private String street;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
    private String complement;
}