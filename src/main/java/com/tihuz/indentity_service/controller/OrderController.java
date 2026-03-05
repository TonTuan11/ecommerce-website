package com.tihuz.indentity_service.controller;

import com.tihuz.indentity_service.dto.response.ApiResponse;
import com.tihuz.indentity_service.dto.response.OrderResponse;
import com.tihuz.indentity_service.entity.Order;
import com.tihuz.indentity_service.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.Mapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrderController {

    OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout() {
        String userId = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return ResponseEntity.ok(orderService.checkout(userId));
    }
}
