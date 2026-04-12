package com.tihuz.ecommerce_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tihuz.ecommerce_backend.enums.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = false)
public class OrderRequest
{
    String recipientName;

    String recipientPhone;

    String shippingAddress;

    PaymentMethod  paymentMethod;

}
