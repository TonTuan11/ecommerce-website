package com.tihuz.ecommerce_backend.entity;

import com.tihuz.ecommerce_backend.base.BaseEntity;
import com.tihuz.ecommerce_backend.enums.CategoryStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.ArrayList;
import java.util.List;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "categories")

public class Category extends BaseEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;
    @Column(nullable = false,unique = true)
    String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    Category parent;


    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    List<Category> children = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    CategoryStatus status;

    @PrePersist
    public void onCreate()
    {
        if (status == null)
        {
            status=CategoryStatus.ACTIVE;
        }
    }
}
