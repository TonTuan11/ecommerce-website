package com.tihuz.indentity_service.dto.request;

import com.tihuz.indentity_service.enums.CategoryStatus;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder

public class CategoryUpdateRequest {

    @Size(max = 100, message = "CATE_INVALID2")
    @Size(min = 3, message = "CATE_INVALID")
    String name;

    Long parentId;
    CategoryStatus status;
}
