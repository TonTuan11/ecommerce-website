package com.tihuz.ecommerce_backend.mapper;

import com.tihuz.ecommerce_backend.dto.request.CategoryCreationRequest;
import com.tihuz.ecommerce_backend.dto.request.CategoryUpdateRequest;
import com.tihuz.ecommerce_backend.dto.response.CategoryResponse;
import com.tihuz.ecommerce_backend.entity.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper
{
//    @Mapping(source = "name", target = "name")
////  //  @Mapping(source = "slug", target = "slug")
//    @Mapping(source = "position", target = "position")
//    @Mapping(source = "status", target = "status")


    Category toCate (CategoryCreationRequest request);

    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.name", target = "parentName")
    CategoryResponse toCateResponse(Category category );


    // Ignore null values — keep existing data in DB.
    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    void updateCate(@MappingTarget Category category, CategoryUpdateRequest request);


}
