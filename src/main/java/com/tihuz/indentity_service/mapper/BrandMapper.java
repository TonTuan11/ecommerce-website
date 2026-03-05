package com.tihuz.indentity_service.mapper;


import com.tihuz.indentity_service.dto.request.BrandCreationRequest;
import com.tihuz.indentity_service.dto.request.BrandUpdateRequest;
import com.tihuz.indentity_service.dto.response.BrandResponse;
import com.tihuz.indentity_service.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BrandMapper {

//    @Mapping(source = "name", target = "name")
//    @Mapping(source = "description", target = "description")
//    @Mapping(source = "logo", target = "logo")
//    @Mapping(source = "position", target = "position")



    Brand toBrand(BrandCreationRequest request);


    BrandResponse toBrandResponse(Brand brand);


    void updateBrand(@MappingTarget Brand brand, BrandUpdateRequest request);

}
