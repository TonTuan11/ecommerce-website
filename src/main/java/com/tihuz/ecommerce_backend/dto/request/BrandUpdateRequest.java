package com.tihuz.ecommerce_backend.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandUpdateRequest {

    String name;
    String description;
    String logo;
    Integer position;
    Boolean status;
}
