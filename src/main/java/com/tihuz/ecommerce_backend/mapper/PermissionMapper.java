package com.tihuz.ecommerce_backend.mapper;


import com.tihuz.ecommerce_backend.dto.request.PermissionRequest;

import com.tihuz.ecommerce_backend.dto.response.PermissionResponse;

import org.mapstruct.Mapper;


import com.tihuz.ecommerce_backend.entity.Permission;

// báo đây là 1 Mapper quản lý bởi Spring
@Mapper(componentModel = "spring")
public interface PermissionMapper {

//       @Mapping(source = "name", target = "name")
//    @Mapping(source = "description", target = "description")

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);


}
