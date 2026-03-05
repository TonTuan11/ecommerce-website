package com.tihuz.indentity_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    Long productId;

    String productName;

    BigDecimal price;

    BigDecimal priceSale;

    Integer quantity;

//    String images;

    BigDecimal subTotal;
}
