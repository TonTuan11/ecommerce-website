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
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
     Order order;

    // không join bảng Product vì sau này nếu giá đổi thì hiển thị bị sai
     Long productId;

     String productName;

     BigDecimal price;

     Integer quantity;

     BigDecimal subTotal;
}
