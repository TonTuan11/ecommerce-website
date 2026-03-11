package com.tihuz.ecommerce_backend.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductFilterRequest {


    Long categoryId;
    String keyword;
    BigDecimal minPrice;
    BigDecimal maxPrice;
}
