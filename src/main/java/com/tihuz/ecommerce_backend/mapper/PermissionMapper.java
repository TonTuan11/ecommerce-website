package com.tihuz.ecommerce_backend.mapper;

import com.tihuz.ecommerce_backend.dto.request.PermissionRequest;

import com.tihuz.ecommerce_backend.dto.response.PermissionResponse;

import org.mapstruct.Mapper;


import com.tihuz.ecommerce_backend.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);


}
