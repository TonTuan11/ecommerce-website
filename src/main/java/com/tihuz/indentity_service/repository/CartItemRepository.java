package com.tihuz.indentity_service.repository;

import com.tihuz.indentity_service.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


}
