package com.tihuz.ecommerce_backend.entity;

import com.tihuz.ecommerce_backend.base.BaseEntity;
import com.tihuz.ecommerce_backend.enums.OrderStatus;
import com.tihuz.ecommerce_backend.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String userId;

    @Column(nullable = false)
    String recipientName;

    @Column(nullable = false)
    String recipientPhone;

    @Column(nullable = false)
    String shippingAddress;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    @Column(nullable = false)
    BigDecimal totalPrice;

    @Enumerated( EnumType.STRING)
    @Column(nullable = false)
    OrderStatus status;

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    List<OrderItem> items= new ArrayList<>();
}
