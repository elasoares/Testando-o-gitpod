package com.deliverytech.delivey_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliverytech.delivey_api.model.CostumerOrder;
import com.deliverytech.delivey_api.model.OrderItem;
import java.util.List;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
    List<OrderItem> findByOrder(CostumerOrder order);
}
