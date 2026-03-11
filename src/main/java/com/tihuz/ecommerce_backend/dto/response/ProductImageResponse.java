package com.tihuz.ecommerce_backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductImageResponse {

Long id;
String url;
Boolean isThumbnail;
}
