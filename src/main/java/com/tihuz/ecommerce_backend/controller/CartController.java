package com.tihuz.ecommerce_backend.controller;

import com.tihuz.ecommerce_backend.dto.request.CartCreationRequest;
import com.tihuz.ecommerce_backend.dto.request.CartUpdateRequest;
import com.tihuz.ecommerce_backend.dto.response.ApiResponse;
import com.tihuz.ecommerce_backend.dto.response.CartResponse;
import com.tihuz.ecommerce_backend.service.CartService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CartController {

    CartService cartService;


    @PostMapping("/add")
    ApiResponse<CartResponse> addToCart(@RequestBody @Valid CartCreationRequest request)
    {
        return  ApiResponse.<CartResponse>builder()
                .result(cartService.addToCart(request))
                .build();
    }


    @GetMapping
    List<CartResponse> getAll()
    {
        return cartService.getAll();
    }


    @GetMapping("/me")
    ApiResponse<CartResponse> getForUser()
    {
        return ApiResponse.<CartResponse>builder()
                .result( cartService.getForUser())
                .build();


    }

    @PutMapping
    ApiResponse<CartResponse> updateItem(@RequestBody CartUpdateRequest request)
    {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.updateItem(request))
                .build();

    }


    @DeleteMapping("/{productId}")
    ApiResponse<CartResponse> deleteCart(@PathVariable Long productId)
    {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.deleteItem(productId))
                .build();

    }


    @DeleteMapping
    String clearCartItem()
    {
         cartService.clearCartItem();
         return "Clear access";
    }



}
