package com.tihuz.ecommerce_backend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Date;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class InvalidatedToken {

    @Id
     String id; // ID (jti) token

    Date expiryTime; // thời điểm token này tự hết hạn (dùng để cleanup)



}
