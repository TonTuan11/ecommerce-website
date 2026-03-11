package com.tihuz.ecommerce_backend.mapper;

import com.tihuz.ecommerce_backend.dto.request.CartCreationRequest;
import com.tihuz.ecommerce_backend.dto.response.CartResponse;
import com.tihuz.ecommerce_backend.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// vì trong CartResponse nó có chứ List<CartItemResponse>
// nên phải dùng uses= CartItemMapper.class để nó map sang
@Mapper(componentModel = "spring",uses = CartItemMapper.class)

public interface CartMapper {

    Cart toCart(CartCreationRequest request);

    @Mapping(source = "id", target = "cartId")
    CartResponse toCartResponse(Cart cart);
}
