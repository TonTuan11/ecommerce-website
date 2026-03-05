package com.tihuz.indentity_service.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.tihuz.indentity_service.enums.CategoryStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
//annotation tạo constructor
@NoArgsConstructor
@AllArgsConstructor
//annotation tạo object nhanh hơn
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponse {

    Long id;
    String name;
    String slug;

    Long parentId;
    String parentName;

    CategoryStatus status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    List<CategoryResponse> children;

}

// Vì sao Response không trả parent?
//
// + Tránh lazy loading
// + Tránh infinite recursion