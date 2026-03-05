package com.tihuz.indentity_service.mapper;

import com.tihuz.indentity_service.dto.response.CartItemResponse;
import com.tihuz.indentity_service.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// spring quản lý bean
@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "price")
    @Mapping(source = "product.priceSale", target = "priceSale")
//    @Mapping(source = "product.images", target = "images")
    CartItemResponse toResponse(CartItem cartItem);
}