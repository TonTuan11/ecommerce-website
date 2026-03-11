package com.tihuz.ecommerce_backend.entity;


import com.tihuz.ecommerce_backend.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE)
@Builder
@Table(name = "brands")
public class Brand  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    String name;
    String slug;
    String description;
    String logo;
    Integer position;
    Boolean status;


    @PrePersist
    public void onCreate()
    {
        if(status==null){status=true;}
    }


    @PreUpdate
    public void onUpdate()
    {
        status=true;
    }


}
