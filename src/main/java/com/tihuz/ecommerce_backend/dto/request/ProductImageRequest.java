package com.tihuz.ecommerce_backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductImageRequest
{

String url;
Boolean isThumbnail;
}
