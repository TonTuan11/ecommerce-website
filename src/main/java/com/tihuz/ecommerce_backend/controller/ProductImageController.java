package com.tihuz.ecommerce_backend.controller;


import com.tihuz.ecommerce_backend.dto.response.ApiResponse;
import com.tihuz.ecommerce_backend.dto.response.ProductImageResponse;
import com.tihuz.ecommerce_backend.service.ProductImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product-image")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProductImageController {

    ProductImageService productImageService;


    @PostMapping(
            value = "upload/{productId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE      //post này chỉ nhận multipart/form-data
    )
    public List<ProductImageResponse> upload(
            @PathVariable Long productId,
            // không dùng request body vì đây là Multipart không phải json
            @RequestParam("files") List<MultipartFile> files  // lấy tất cả các file có name = "files" trong form-data
    )
    {
        return productImageService.upload(productId,files);
    }


    @PutMapping("/{productId}/images/{imageId}/thumbnail")
    public ApiResponse<Void> setThumbnail(

            @PathVariable Long productId,
            @PathVariable Long imageId
    )
    {
        productImageService.setThumbnail(productId,imageId);
      return ApiResponse.<Void>builder()
              .message("Change success")
              .build();

    }


}
