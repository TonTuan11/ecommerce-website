package com.tihuz.indentity_service.dto.response;

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
