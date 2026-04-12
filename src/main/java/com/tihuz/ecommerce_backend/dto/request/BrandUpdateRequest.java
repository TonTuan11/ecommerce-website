package com.tihuz.ecommerce_backend.dto.request;

import com.tihuz.ecommerce_backend.enums.BrandStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandUpdateRequest
{

    String name;
    String description;
    String logo;
    Integer position;
    BrandStatus status;
}
