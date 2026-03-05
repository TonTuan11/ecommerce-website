package com.tihuz.indentity_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartUpdateRequest {

    Long productId;
    Integer quantity;
}
