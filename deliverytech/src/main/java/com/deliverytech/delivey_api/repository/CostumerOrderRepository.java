package com.deliverytech.delivey_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliverytech.delivey_api.model.Client;
import com.deliverytech.delivey_api.model.CostumerOrder;
import com.deliverytech.delivey_api.model.OrderStatus;

import java.util.List;


public interface CostumerOrderRepository extends JpaRepository<CostumerOrder, Long> {
     List<CostumerOrder> findByClientAndStatus(Client client, OrderStatus status);

    List<CostumerOrder> findByClientId(Long clientId); 
    List<CostumerOrder> findByStatus(OrderStatus status);
}
