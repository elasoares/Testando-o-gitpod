package com.deliverytech.delivey_api.repository;

import com.deliverytech.delivey_api.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    List<Client> findByActiveTrue();

    List<Client> findByNameContainingIgnoreCase(String name);

    boolean existsByEmail(String email);
}