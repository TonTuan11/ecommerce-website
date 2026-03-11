package com.tihuz.ecommerce_backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
//annotation tạo constructor
@NoArgsConstructor
@AllArgsConstructor
//annotation tạo object nhanh hơn
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    String token;
    boolean authenticated;
}
