package com.tihuz.ecommerce_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tihuz.ecommerce_backend.enums.ProductStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {

    Long id;
    String name;
    BigDecimal price;
    BigDecimal priceSale;
    Integer quantity;
    String slug;

    Long categoryId;
    String categoryName;

    List<ProductImageResponse> images;
    String thumbnail;


    ProductStatus status;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
