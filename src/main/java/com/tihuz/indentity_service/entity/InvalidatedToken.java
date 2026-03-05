package com.tihuz.indentity_service.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
// đánh dấu class này là 1 table
@Entity
public class InvalidatedToken {

    @Id
     String id; // jti của token

    Date expiryTime; // thời điểm token này tự hết hạn (dùng để cleanup)



}
