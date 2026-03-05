package com.tihuz.indentity_service.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
// đánh dấu class này là 1 table
@Entity
public class Role {

    @Id
     String name;
     String description;

    @ManyToMany(fetch = FetchType.LAZY)   //tránh load permission không cần thiết ,  tránh chậm + N+1 query
    @ToString.Exclude  //Tránh StackOverflowError, Lombok không in vòng lặp Role ↔ Permission
    @EqualsAndHashCode.Exclude // Tránh so sánh vòng lặp, Rất quan trọng khi dùng Set
    @JsonIgnore //Tránh vòng lặp JSON khi trả API
     Set<Permission> permissions=new HashSet<>();


}
