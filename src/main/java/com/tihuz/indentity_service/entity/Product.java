package com.tihuz.indentity_service.entity;


import com.tihuz.indentity_service.base.BaseEntity;
import com.tihuz.indentity_service.enums.CategoryStatus;
import com.tihuz.indentity_service.enums.ProductStatus;
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
public class Product extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false,unique = true)
    String name;

    @Column(nullable = false)
    BigDecimal price;

    @Column(nullable = true)
    BigDecimal priceSale;

    @Column(nullable = false,unique = true)
    String slug;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="category_id",nullable = false)
    Category category;


    @OneToMany(
            mappedBy = "product",  // filed nằm trong entity cần map
            cascade = CascadeType.ALL,  // không cần phải gọi repository cho entity con
            orphanRemoval = true       //xoá image khi update list
    )
    List<ProductImage> images=new ArrayList<>();  // new ArrayList là để cho mỗi product có 1 list ảnh riêng

                                                    //object graph,quan hệ trong RAM



    @Column(nullable = false)
    Integer quantity;

    @Enumerated(EnumType.STRING)
    ProductStatus status;


    // set mặc định khi tạo thì status là ACTIVE
    @PrePersist
    public void onCreate() {

        if (status == null)
        {
            status= ProductStatus.ACTIVE;
        }
    }


}
