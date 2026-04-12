package com.tihuz.ecommerce_backend.dto.response;

import com.tihuz.ecommerce_backend.enums.OrderStatus;
import com.tihuz.ecommerce_backend.enums.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse
{

    Long id;
    String userId;
    String recipientName;
    String recipientPhone;
    String shippingAddress;
    String paymentMethod;
    BigDecimal totalPrice;
    String status;
    List<OrderItemResponse> items;
}