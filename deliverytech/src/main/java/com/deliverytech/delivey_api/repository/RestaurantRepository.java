package com.deliverytech.delivey_api.repository;

import com.deliverytech.delivey_api.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByName(String name); // Para validação de nome único
    List<Restaurant> findByActiveTrue(); // Para buscar restaurantes ativos
    List<Restaurant> findByActiveFalse(); // Para buscar restaurantes inativos
}