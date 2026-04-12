package com.tihuz.ecommerce_backend.mapper;

import com.tihuz.ecommerce_backend.dto.request.OrderRequest;
import com.tihuz.ecommerce_backend.dto.response.OrderResponse;
import com.tihuz.ecommerce_backend.entity.Order;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface OrderMapper
{

    Order toOrder(OrderRequest orderRequest);
   // @Mapping(target = "status", expression = "java(order.getStatus().name())")
    OrderResponse toResponse(Order order);
}
