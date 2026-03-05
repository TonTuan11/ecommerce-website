package com.tihuz.indentity_service.mapper;


import com.tihuz.indentity_service.dto.request.PermissionRequest;

import com.tihuz.indentity_service.dto.response.PermissionResponse;

import org.mapstruct.Mapper;


import com.tihuz.indentity_service.entity.Permission;
import org.mapstruct.Mapping;

// báo đây là 1 Mapper quản lý bởi Spring
@Mapper(componentModel = "spring")
public interface PermissionMapper {

//       @Mapping(source = "name", target = "name")
//    @Mapping(source = "description", target = "description")

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);


}
