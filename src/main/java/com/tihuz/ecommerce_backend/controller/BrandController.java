package com.tihuz.ecommerce_backend.controller;


import com.tihuz.ecommerce_backend.dto.response.ApiResponse;
import com.tihuz.ecommerce_backend.dto.request.BrandCreationRequest;
import com.tihuz.ecommerce_backend.dto.request.BrandUpdateRequest;
import com.tihuz.ecommerce_backend.dto.response.BrandResponse;
import com.tihuz.ecommerce_backend.repository.BrandRepository;
import com.tihuz.ecommerce_backend.service.BrandService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class BrandController {


    BrandService brandService;
    BrandRepository brandRepository;

   @PostMapping
    ApiResponse<BrandResponse> createBrand(@RequestBody @Valid BrandCreationRequest request)
   {
       return ApiResponse.<BrandResponse>builder()
               .result(brandService.createBrand(request))
               .build();
   }


   @PutMapping("/{name}")
    ApiResponse<BrandResponse> updateBrand(@PathVariable String name, @RequestBody BrandUpdateRequest request)
   {
       return ApiResponse.<BrandResponse>builder()
               .result(brandService.updateBrand(name,request))
               .build();
   }


   @GetMapping
    List<BrandResponse> getAll()
   {
       return brandService.getAll();

   }


   @Transactional
   @DeleteMapping("/{name}")
    String deleteBrand( @PathVariable String name)
   {
       brandService.deleteBrand(name);
      return "delete access";
   }


   @GetMapping("/{name}")
    BrandResponse getBrand(@PathVariable String name)
   {
       return brandService.getBrand(name);
   }

}
