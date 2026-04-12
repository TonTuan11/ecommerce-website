package com.tihuz.ecommerce_backend.controller;

import com.tihuz.ecommerce_backend.dto.request.OrderRequest;
import com.tihuz.ecommerce_backend.dto.response.OrderResponse;
import com.tihuz.ecommerce_backend.service.OrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrderController {

    OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@RequestBody @Valid OrderRequest request)
    {
        String userId = SecurityContextHolder.getContext()
                                             .getAuthentication()
                                             .getName();
        return ResponseEntity.ok(orderService.checkout(userId,request));
    }

    @GetMapping("/my-orders")
    public List<OrderResponse> getOrderForUser()
    {
        String userId = SecurityContextHolder.getContext()
                                             .getAuthentication()
                                             .getName();
        return orderService.getOrdersForUser(userId);
    }
}
