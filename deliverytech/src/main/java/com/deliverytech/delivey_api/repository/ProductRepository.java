package com.deliverytech.delivey_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.deliverytech.delivey_api.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    // --- Este é o método que está faltando ou incorreto ---
    Optional<Product> findByRestaurantIdAndNameIgnoreCase(Long restaurantId, String name);
    // --- Fim do método faltando ---

    // Outros métodos que eu sugeri anteriormente, se você não os tiver:
    List <Product> findByRestaurantId(Long restaurantId);
    List<Product> findByRestaurantIdAndAvailableTrue(Long restaurantId);
    List<Product> findByPriceGreaterThan(Double price);
}
