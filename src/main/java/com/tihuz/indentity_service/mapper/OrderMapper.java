package com.tihuz.indentity_service.mapper;

import com.tihuz.indentity_service.dto.response.OrderResponse;
import com.tihuz.indentity_service.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface OrderMapper {
    // Vì entity status là enum
    //      DTO status là String
    // nên mapping như này
    @Mapping(target = "status", expression = "java(order.getStatus().name())")
    OrderResponse toResponse(Order order);
}
