package com.tihuz.ecommerce_backend.mapper;


import com.tihuz.ecommerce_backend.dto.request.ProductCreationRequest;
import com.tihuz.ecommerce_backend.dto.request.ProductUpdateRequest;
import com.tihuz.ecommerce_backend.dto.response.ProductResponse;
import com.tihuz.ecommerce_backend.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {




    Product toProduct(ProductCreationRequest request);


    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ProductResponse toProductResponse (Product product);

    void updateProduct(@MappingTarget Product product, ProductUpdateRequest request);
}
