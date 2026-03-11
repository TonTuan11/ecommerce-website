package com.tihuz.ecommerce_backend.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;


// class này dùng để nhận request từ client khi tạo user (tránh thao tác trực tiếp trên entity)
@Data
//annotation tạo constructor
@NoArgsConstructor
@AllArgsConstructor

//annotation tạo object nhanh hơn
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {

    String name;
    String description;
    Set<String> permissions;
    // không dùng Set<Permission> mà dùng Set<String>
    // vì trong request không nên chứa entity của database
    // nếu dùng thì client phải gửi Json dạng:
    //    "permissions": [
    //    {
    //      "name": "USER_CREATE",
    //      "description": "create user"
    //    }
    //  ]

    // nó không hợp lí vì làm sao client tự gửi nguyên cái object của permission được
    // có thể gửi fake description
    //có thể gửi Permission không tồn tại
    //có thể gửi Permission không tồn tại

    // chỉ nên gửi một tập chuỗi String là tên của permission
    // dễ kiểm tra và debug
}
