package com.tihuz.ecommerce_backend.repository;

import com.tihuz.ecommerce_backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
