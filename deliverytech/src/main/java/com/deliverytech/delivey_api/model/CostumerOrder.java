package com.deliverytech.delivey_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostumerOrder {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Client cliente;

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurant restaurant;

    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime dateOrder = LocalDateTime.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> item;

    @Embedded
    private Address deliveryAddress;
}