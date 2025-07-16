package com.deliverytech.delivey_api.repository;

import com.deliverytech.delivey_api.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByNameIgnoreCase(String name);

    List<Restaurant> findByCategory(String category);

    List<Restaurant> findByActiveTrue();
}