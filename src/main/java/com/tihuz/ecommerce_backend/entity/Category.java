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

public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false,unique = true)
    String name;
    @Column(nullable = false,unique = true)
    String slug;

    @ManyToOne(fetch = FetchType.LAZY)  // fetch ảnh hưởng thời điểm query  LAZY = tải khi cần , gọi getter thì nó mới query.
    @JoinColumn(name = "parent_id")  //FK
    Category parent;   // có thể xem parent là dữ liệu


    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    List<Category> children = new ArrayList<>();   // có thể xem children là cách view dữ liệu được lấy ra từ parent

    @Enumerated(EnumType.STRING)
    CategoryStatus status;

    @PrePersist
    public void onCreate() {

        if (status == null)
        {
            status=CategoryStatus.ACTIVE;
        }
    }


}
