package com.tihuz.indentity_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tihuz.indentity_service.entity.Category;
import com.tihuz.indentity_service.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.w3c.dom.stylesheets.LinkStyle;

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
