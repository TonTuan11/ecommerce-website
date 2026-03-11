package com.tihuz.ecommerce_backend.repository;

import com.tihuz.ecommerce_backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


}
