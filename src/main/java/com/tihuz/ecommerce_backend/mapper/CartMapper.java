package com.tihuz.ecommerce_backend.mapper;

import com.tihuz.ecommerce_backend.dto.request.CartCreationRequest;
import com.tihuz.ecommerce_backend.dto.response.CartResponse;
import com.tihuz.ecommerce_backend.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

//Use CartItemMapper to map the list inside CartResponse
@Mapper(componentModel = "spring",uses = CartItemMapper.class)

public interface CartMapper
{
    Cart toCart(CartCreationRequest request);

    @Mapping(source = "id", target = "cartId")
    CartResponse toCartResponse(Cart cart);
}
