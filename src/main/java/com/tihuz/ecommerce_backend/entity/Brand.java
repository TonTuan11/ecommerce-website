package com.tihuz.ecommerce_backend.entity;

import com.tihuz.ecommerce_backend.base.BaseEntity;
import com.tihuz.ecommerce_backend.enums.BrandStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE)
@Builder
@Table(name = "brands")
public class Brand  extends BaseEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false, unique = true)
    String slug;

    String description;

    Integer position;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    BrandStatus status;

    @PrePersist
    public void onCreate()
    {
        if (status == null)
        {
            status= BrandStatus.ACTIVE;
        }
    }

}
