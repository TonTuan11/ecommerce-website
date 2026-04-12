package com.tihuz.ecommerce_backend.mapper;

import com.tihuz.ecommerce_backend.dto.response.CartItemResponse;
import com.tihuz.ecommerce_backend.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper
{

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "price")
    @Mapping(source = "product.priceSale", target = "priceSale")
//    @Mapping(source = "product.images", target = "images")
    CartItemResponse toResponse(CartItem cartItem);
}