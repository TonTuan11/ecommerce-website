package com.tihuz.indentity_service.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
// đánh dấu class này là 1 table
@Entity
public class Permission {

    @Id
     String name;
     String description;

}
