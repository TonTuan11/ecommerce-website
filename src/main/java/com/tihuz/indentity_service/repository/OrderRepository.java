package com.tihuz.indentity_service.repository;

import com.tihuz.indentity_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
