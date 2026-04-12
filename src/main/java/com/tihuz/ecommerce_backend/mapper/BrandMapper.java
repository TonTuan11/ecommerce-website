package com.tihuz.ecommerce_backend.mapper;

import com.tihuz.ecommerce_backend.dto.request.BrandCreationRequest;
import com.tihuz.ecommerce_backend.dto.request.BrandUpdateRequest;
import com.tihuz.ecommerce_backend.dto.response.BrandResponse;
import com.tihuz.ecommerce_backend.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BrandMapper
{
//    @Mapping(source = "name", target = "name")
//    @Mapping(source = "description", target = "description")
//    @Mapping(source = "logo", target = "logo")
//    @Mapping(source = "position", target = "position")

    Brand toBrand(BrandCreationRequest request);


    BrandResponse toBrandResponse(Brand brand);


    void updateBrand(@MappingTarget Brand brand, BrandUpdateRequest request);

}
