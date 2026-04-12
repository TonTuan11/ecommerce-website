package com.tihuz.ecommerce_backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse
{
    Long productId;

    String productName;

    BigDecimal price;

    BigDecimal priceSale;

    Integer quantity;


    BigDecimal subTotal;
}
