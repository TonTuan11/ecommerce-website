package com.tihuz.ecommerce_backend.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;


// class này dùng để nhận request từ client khi tạo user (tránh thao tác trực tiếp trên entity)
@Data
//annotation tạo constructor
@NoArgsConstructor
@AllArgsConstructor

//annotation tạo object nhanh hơn
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionRequest {

    String name;
    String description;
}
