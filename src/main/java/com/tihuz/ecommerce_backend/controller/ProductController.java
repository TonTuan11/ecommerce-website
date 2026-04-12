package com.tihuz.ecommerce_backend.controller;

import com.tihuz.ecommerce_backend.dto.request.ProductCreationRequest;
import com.tihuz.ecommerce_backend.dto.request.ProductFilterRequest;
import com.tihuz.ecommerce_backend.dto.request.ProductUpdateRequest;
import com.tihuz.ecommerce_backend.dto.response.ApiResponse;
import com.tihuz.ecommerce_backend.dto.response.ProductResponse;
import com.tihuz.ecommerce_backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProductController {

    ProductService productService;

    @PostMapping()
    ApiResponse<ProductResponse> createProduct(@RequestBody @Valid ProductCreationRequest request)
    {
        return ApiResponse.<ProductResponse>builder()
                          .result(  productService.create(request))
                          .build();
    }

    @GetMapping()
    ApiResponse<Page<ProductResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10")int size
    )
    {
        return ApiResponse.<Page<ProductResponse>>builder()
                          .result(productService.getAll(page,size))
                          .build();
    }

    @GetMapping("/{slug}")
    ApiResponse<ProductResponse> getProductBySlug(@PathVariable String slug)
    {
        return ApiResponse.<ProductResponse>builder()
                          .result(productService.getProductBySlug(slug))
                          .build();
    }

    @GetMapping("/i/{id}")
    ApiResponse<ProductResponse> getProductById(@PathVariable Long id)
    {
        return ApiResponse.<ProductResponse>builder()
                          .result(productService.getProductById(id))
                          .build();
    }

    @PutMapping("/{slug}")
    ApiResponse<ProductResponse> updateProduct(@PathVariable String slug, @RequestBody ProductUpdateRequest request)
    {
        return ApiResponse.<ProductResponse>builder()
                          .result(productService.updateProduct(slug,request))
                          .build();
    }

    @DeleteMapping("/{slug}")
    ApiResponse<Void> deleteProduct(@PathVariable String slug)
    {
        productService.deleteProduct(slug);
        return ApiResponse.<Void>builder()
                          .message("Product deleted successfully")
                          .build();
    }

    @GetMapping("/filter")
    ApiResponse<Page<ProductResponse>> filterProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required= false) String sort
    )
    {
        ProductFilterRequest request=new ProductFilterRequest();
        request.setCategoryId(categoryId);
        request.setKeyword(keyword);
        request.setMinPrice(minPrice);
        request.setMaxPrice(maxPrice);

        return ApiResponse.<Page<ProductResponse>>builder()
                          .result(productService.filterProducts(request,page,size,sort))
                          .build();
    }

}
