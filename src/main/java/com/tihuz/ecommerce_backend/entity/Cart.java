package com.tihuz.ecommerce_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    User user;

    // cascade = CascadeType.ALL, cha lưu thì các item con cũng lưu, xóa, cập nhật theo
    //orphanRemoval = true khi xóa phần tử con thì xóa cả trong db
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,orphanRemoval = true)
    List<CartItem> items= new ArrayList<>();
}
