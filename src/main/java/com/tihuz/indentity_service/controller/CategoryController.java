package com.tihuz.indentity_service.controller;


import com.tihuz.indentity_service.dto.response.ApiResponse;
import com.tihuz.indentity_service.dto.request.CategoryCreationRequest;
import com.tihuz.indentity_service.dto.request.CategoryUpdateRequest;
import com.tihuz.indentity_service.dto.response.CategoryResponse;

import com.tihuz.indentity_service.service.CategoryService;
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


    //CREATE
    @PostMapping
    ApiResponse<CategoryResponse> createCate(@RequestBody @Valid CategoryCreationRequest request)
    {
        //var result= categoryService.createCate(request);

        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.createCate(request))
                .build();
    }


    //GET ALL
    @GetMapping
    ApiResponse <Page<CategoryResponse>> getAllCate(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10")int size,
            @RequestParam(defaultValue = "id,asc") String sort
    )
    {

        return ApiResponse.<Page<CategoryResponse>> builder()
                .result(  categoryService.getAll(page,size,sort))
                .build();
    }

    //GET BY SLUG
    @GetMapping("/{slug}")
     ApiResponse <CategoryResponse> getCate(@PathVariable  String slug)
    {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.getCate(slug))
                .build();


    }

    // GET TREE
    @GetMapping("/tree")
    ApiResponse <List<CategoryResponse>>  getTree()
    {
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.getTree())
                .build();


    }


    //UPDATE
    @PutMapping("/{slug}")
    ApiResponse< CategoryResponse> updateCate(@PathVariable String slug,@RequestBody CategoryUpdateRequest request)

    {
        return ApiResponse.<CategoryResponse>builder()
                .result(  categoryService.updateCate(slug,request))
                .build();

    }


    // DELETE
//    @Transactional
    @DeleteMapping("/{slug}")
   ApiResponse <Void> deleteCate(@PathVariable String slug)
    {

        categoryService.deleteCate(slug);
        return  ApiResponse.<Void>builder()
                .message("Category deleted successfully")
                .build();

    }


}
