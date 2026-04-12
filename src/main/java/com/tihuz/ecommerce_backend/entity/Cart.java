package com.tihuz.ecommerce_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Cart
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    User user;

    // cascade = CascadeType.ALL, Child entities are automatically created, updated, and deleted with the parent
    //orphanRemoval = true, Delete child entities when removed
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,orphanRemoval = true)
    List<CartItem> items= new ArrayList<>();
}
