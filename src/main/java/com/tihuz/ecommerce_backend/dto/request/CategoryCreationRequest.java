package com.tihuz.ecommerce_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryCreationRequest
{

    @NotBlank(message = "CATE_NOTNULL")
    @Size(max = 100, message = "CATE_INVALID2")
    @Size(min = 3, message = "CATE_INVALID")
    String name;

    Long parentId;

}

//UI shows full object, API only needs ID — simpler, safer, easier to control.