package com.tihuz.indentity_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
//annotation tạo constructor
@NoArgsConstructor
@AllArgsConstructor
//annotation tạo object nhanh hơn
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionResponse {

    String name;
    String description;
}
