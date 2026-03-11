package com.tihuz.ecommerce_backend.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ProductCreationRequest {

    @NotBlank(message = "PRODUCT_NOTNULL")
    @Size(max = 100, message = "PRODUCT_INVALID2")
    @Size(min = 3, message = "PRODUCT_INVALID")
    String name;

    BigDecimal price;
    BigDecimal priceSale;
    @NotNull
    @Min(0)
    Integer quantity;

    @NotNull(message = "CATE_NOTNULL")
    Long categoryId;

    List<ProductImageRequest> images;


}
