package com.tihuz.indentity_service.mapper;

import com.tihuz.indentity_service.dto.request.CartCreationRequest;
import com.tihuz.indentity_service.dto.response.CartResponse;
import com.tihuz.indentity_service.entity.Cart;
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
