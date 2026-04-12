package com.tihuz.ecommerce_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
public class OrderItem
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;

    // Avoid joining Product to preserve historical price accuracy
    @Column(nullable = false)
     Long productId;

    @Column(nullable = false)
     String productName;

    @Column(nullable = false)
     BigDecimal price;

    @Column(nullable = false)
     Integer quantity;

    @Column(nullable = false)
     BigDecimal subTotal;
}
