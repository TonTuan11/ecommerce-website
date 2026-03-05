package com.tihuz.indentity_service.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class BrandCreationRequest {

    @NotBlank(message = "BRAND_NOTNULL")
    @Size(min=1,message = "BRAND_INVALID")
    @Size(max =255,message = "BRAND_INVALID2")
    String name;
    String description;
    String logo;
    Integer position;



}
