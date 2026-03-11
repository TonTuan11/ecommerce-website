package com.tihuz.ecommerce_backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;


// class này dùng để nhận request từ client khi tạo user (tránh thao tác trực tiếp trên entity)
//đóng gói dữ liệu trả về cho client một cách an toàn, rõ ràng và tách biệt với entity

@Data
//annotation tạo constructor
@NoArgsConstructor
@AllArgsConstructor
//annotation tạo object nhanh hơn
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE) // set cho các thuộc tính là private
public class UserResponse {

     String id;
     String username;
     String email;
     String firstname;
     String lastname;
     LocalDate dob;

     Set<RoleResponse> roles;
}
