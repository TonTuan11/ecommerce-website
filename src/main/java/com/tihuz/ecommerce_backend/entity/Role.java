package com.tihuz.ecommerce_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "role")
public class Role {

    @Id
     String name;
     String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude  //Prevent Role ↔ Permission loop (log/ console)
    @EqualsAndHashCode.Exclude // prevent loops in equals(), hashCode()
    @JsonIgnore // prevent loops in Json API
     Set<Permission> permissions=new HashSet<>();


}
