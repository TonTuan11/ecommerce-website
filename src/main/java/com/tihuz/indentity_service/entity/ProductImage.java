package com.tihuz.indentity_service.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product_image")
@Entity
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String url;

    Boolean isThumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "product_id")
    Product product;
}
