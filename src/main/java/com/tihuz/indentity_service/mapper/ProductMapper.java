package com.tihuz.indentity_service.mapper;


import com.tihuz.indentity_service.dto.request.ProductCreationRequest;
import com.tihuz.indentity_service.dto.request.ProductUpdateRequest;
import com.tihuz.indentity_service.dto.response.ProductResponse;
import com.tihuz.indentity_service.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.jpa.repository.Query;

@Mapper(componentModel = "spring")
public interface ProductMapper {




    Product toProduct(ProductCreationRequest request);


    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ProductResponse toProductResponse (Product product);

    void updateProduct(@MappingTarget Product product, ProductUpdateRequest request);
}
