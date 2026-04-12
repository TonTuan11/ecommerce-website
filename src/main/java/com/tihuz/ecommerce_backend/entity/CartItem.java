package com.tihuz.ecommerce_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class CartItem
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @ManyToOne
    @JoinColumn(name = "cart_id",nullable=false)
    Cart cart;


    @ManyToOne
    @JoinColumn(name = "product_id",nullable=false)
    Product product;

    @Column(nullable=false)
    Integer quantity;

}
