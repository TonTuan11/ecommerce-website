package com.tihuz.ecommerce_backend.entity;

import com.tihuz.ecommerce_backend.base.BaseEntity;
import com.tihuz.ecommerce_backend.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "products")
public class Product extends BaseEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false,unique = true)
    String name;

    @Column(nullable = false)
    BigDecimal price;

    BigDecimal priceSale;

    @Column(nullable = false,unique = true)
    String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="category_id",nullable = false)
    Category category;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id",nullable = false)
    Brand brand;


    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    List<ProductImage> images=new ArrayList<>();  // Each product has its own image list

    @Column(nullable = false)
    Integer quantity;

    @Enumerated(EnumType.STRING)
    ProductStatus status;


    @PrePersist
    public void onCreate()
    {
        if (status == null)
        {
            status= ProductStatus.ACTIVE;
        }
    }


}
