package com.tihuz.indentity_service.dto.request;

import com.tihuz.indentity_service.enums.ProductStatus;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {

    String name;

    BigDecimal price;
    BigDecimal priceSale;

    @Min(0)
    Integer quantity;

    Long categoryId;
    ProductStatus status;
}
