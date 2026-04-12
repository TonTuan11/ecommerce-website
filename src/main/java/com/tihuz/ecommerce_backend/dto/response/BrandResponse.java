package com.tihuz.ecommerce_backend.dto.response;

import com.tihuz.ecommerce_backend.enums.BrandStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandResponse
{

    String name;
    String slug;
    String logo;

    String description;
    Integer position;
    BrandStatus status;


}
