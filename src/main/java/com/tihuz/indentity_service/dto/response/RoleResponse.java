package com.tihuz.indentity_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
//annotation tạo constructor
@NoArgsConstructor
@AllArgsConstructor
//annotation tạo object nhanh hơn
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {

    String name;
    String description;
    Set<PermissionResponse> permissions=Set.of();
}
