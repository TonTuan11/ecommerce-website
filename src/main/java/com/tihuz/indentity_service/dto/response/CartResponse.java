package com.tihuz.indentity_service.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {

     Long cartId;
     List<CartItemResponse> items;
     BigDecimal totalPrice;
}
