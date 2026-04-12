package com.tihuz.ecommerce_backend.controller;

import com.tihuz.ecommerce_backend.dto.response.ApiResponse;
import com.tihuz.ecommerce_backend.dto.response.ProductImageResponse;
import com.tihuz.ecommerce_backend.service.ProductImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/product-image")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProductImageController {

    ProductImageService productImageService;

    // Endpoint accepts file uploads via multipart/form-data
    @PostMapping( value = "upload/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<ProductImageResponse> upload( @PathVariable Long productId, @RequestParam("files") List<MultipartFile> files)
    {
        return productImageService.upload(productId,files);
    }

    @PutMapping("/{productId}/images/{imageId}/thumbnail")
    public ApiResponse<Void> setThumbnail( @PathVariable Long productId, @PathVariable Long imageId )
    {
        productImageService.setThumbnail(productId,imageId);
      return ApiResponse.<Void>builder()
                        .message("Change success")
                        .build();
    }

    @GetMapping("/{productId}")
    public ApiResponse<List<ProductImageResponse>> getImages(@PathVariable Long productId)
    {
        List<ProductImageResponse> images = productImageService.getImages(productId);
        return ApiResponse.<List<ProductImageResponse>>builder()
                          .code(1000)
                          .message("Success")
                          .result(images)
                          .build();
    }

    @GetMapping("/image/{productId}/{fileName}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable Long productId, @PathVariable String fileName) throws IOException
    {
        InputStream imageStream = productImageService.getImageStream(productId, fileName);

        MediaType mediaType = fileName.toLowerCase().endsWith(".webp") ? MediaType.valueOf("image/webp")
                : fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg") ? MediaType.IMAGE_JPEG
                : fileName.toLowerCase().endsWith(".png") ? MediaType.IMAGE_PNG
                : MediaType.APPLICATION_OCTET_STREAM;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentDispositionFormData("inline", fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(imageStream));
    }
}
