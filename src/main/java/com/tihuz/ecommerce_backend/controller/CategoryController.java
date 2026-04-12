package com.tihuz.ecommerce_backend.controller;


import com.tihuz.ecommerce_backend.dto.response.ApiResponse;
import com.tihuz.ecommerce_backend.dto.request.CategoryCreationRequest;
import com.tihuz.ecommerce_backend.dto.request.CategoryUpdateRequest;
import com.tihuz.ecommerce_backend.dto.response.CategoryResponse;
import com.tihuz.ecommerce_backend.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CategoryController {

    CategoryService categoryService;

    @PostMapping
    ApiResponse<CategoryResponse> createCate(@RequestBody @Valid CategoryCreationRequest request)
    {
        return ApiResponse.<CategoryResponse>builder()
                          .result(categoryService.createCate(request))
                          .build();
    }

    @GetMapping
    ApiResponse <Page<CategoryResponse>> getAllCate(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10")int size,
            @RequestParam(defaultValue = "id,asc") String sort
    )
    {
        return ApiResponse.<Page<CategoryResponse>> builder()
                          .result( categoryService.getAll(page,size,sort))
                          .build();
    }

    @GetMapping("/{slug}")
     ApiResponse <CategoryResponse> getCate(@PathVariable  String slug)
    {
        return ApiResponse.<CategoryResponse>builder()
                          .result(categoryService.getCate(slug))
                          .build();
    }

    @GetMapping("/tree")
    ApiResponse <List<CategoryResponse>>  getTree()
    {
        return ApiResponse.<List<CategoryResponse>>builder()
                          .result(categoryService.getTree())
                          .build();
    }

    @PutMapping("/{slug}")
    ApiResponse< CategoryResponse> updateCate(@PathVariable String slug,@RequestBody CategoryUpdateRequest request)
    {
        return ApiResponse.<CategoryResponse>builder()
                          .result(  categoryService.updateCate(slug,request))
                          .build();
    }

    @DeleteMapping("/{slug}")
   ApiResponse <Void> deleteCate(@PathVariable String slug)
    {
        categoryService.deleteCate(slug);
        return  ApiResponse.<Void>builder()
                           .message("Category deleted successfully")
                           .build();
    }

}
