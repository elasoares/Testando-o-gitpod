package com.deliverytech.delivey_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.deliverytech.delivey_api.model.Product;
import com.deliverytech.delivey_api.model.Restaurant;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    Optional<Product> findByRestaurantIdAndNameIgnoreCase(Long restaurantId, String name);

    List<Product> findByRestaurant(Restaurant restaurant);
    List <Product> findByRestaurantId(Long restaurantId);
    List<Product> findByRestaurantIdAndAvailableTrue(Long restaurantId);
    List<Product> findByPriceGreaterThan(Double price);
}
