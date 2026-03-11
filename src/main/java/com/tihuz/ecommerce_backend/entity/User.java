package com.tihuz.ecommerce_backend.entity;


import com.tihuz.ecommerce_backend.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.UUID )
     String id;
    @Column(nullable = false, unique = true)
     String username;

    @Column(nullable = false)
     String password;

    @Email
    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
     String firstname;

    @Column(nullable = false)
     String lastname;

    @Column(nullable = false)
    LocalDate dob;


     @ManyToMany
     Set<Role> roles;


}
