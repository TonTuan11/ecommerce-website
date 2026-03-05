package com.tihuz.indentity_service.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductFilterRequest {


    Long categoryId;
    String keyword;
    BigDecimal minPrice;
    BigDecimal maxPrice;
}
