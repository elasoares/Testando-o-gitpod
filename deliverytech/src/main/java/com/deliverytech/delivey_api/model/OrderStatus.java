package com.deliverytech.delivey_api.model;

// Enum para representar os diferentes status de um pedido
public enum OrderStatus {
    PENDING,      // Pedido aguardando confirmação
    CONFIRMED,    // Pedido confirmado pelo restaurante
    PREPARING,    // Pedido sendo preparado
    OUT_FOR_DELIVERY, // Pedido saiu para entrega
    DELIVERED,    // Pedido entregue
    CANCELED      // Pedido cancelado
}