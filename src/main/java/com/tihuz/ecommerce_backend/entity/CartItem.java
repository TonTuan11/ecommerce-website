package com.tihuz.ecommerce_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    Cart cart;

    // ManyToOne vì: nhiều CartItem có thể tham chiếu cùng 1 Product
    // 1 product có thể nằm trong nhiều cart
    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @Column(nullable=false)
    Integer quantity;

}
