package com.tihuz.indentity_service.entity;

import com.tihuz.indentity_service.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    // không join vì hóa đơn thì không được thay đổi
    // lưu snapshot tại thời điểm checkout
    String userId;

    BigDecimal totalPrice;

    // dùng String, nếu dùng ORDINAL sau này đổi thứ tự bị loạn
    // String an toàn hơn
    @Enumerated( EnumType.STRING)
    OrderStatus status;

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    List<OrderItem> items= new ArrayList<>();
}
