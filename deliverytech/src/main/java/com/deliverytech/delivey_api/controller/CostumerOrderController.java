package com.deliverytech.delivey_api.controller;

import com.deliverytech.delivey_api.model.CostumerOrder;
import com.deliverytech.delivey_api.model.OrderStatus;
import com.deliverytech.delivey_api.service.CostumerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class CostumerOrderController {

    @Autowired
    private CostumerOrderService costumerOrderService;

    @PostMapping
    public ResponseEntity<CostumerOrder> createOrder(@RequestBody CostumerOrder order) {
        // Validação básica do corpo da requisição (pode ser mais robusta com @Valid)
        if (order.getClient() == null || order.getClient().getId() == null ||
            order.getRestaurant() == null || order.getRestaurant().getId() == null ||
            order.getItems() == null || order.getItems().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
        CostumerOrder createdOrder = costumerOrderService.createOrder(order);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CostumerOrder>> getAllOrders() {
        List<CostumerOrder> orders = costumerOrderService.findAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CostumerOrder> getOrderById(@PathVariable Long id) {
        return costumerOrderService.findOrderById(id)
                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}/status") // Endpoint para atualizar apenas o status
    public ResponseEntity<CostumerOrder> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        CostumerOrder updatedOrder = costumerOrderService.updateOrderStatus(id, status);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        costumerOrderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}