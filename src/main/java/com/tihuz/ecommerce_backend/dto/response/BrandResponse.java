package com.tihuz.ecommerce_backend.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandResponse {

    String name;
    String slug;
    String logo;

    String description;
    Integer position;
    Boolean status;


}
