package com.tihuz.indentity_service.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
// đánh dấu class này là 1 table
@Entity
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.UUID )
     String id;
     String username;
     String password;
     String firstname;
     String lastname;
     LocalDate dob;

     @ManyToMany
     Set<Role> roles;


}
