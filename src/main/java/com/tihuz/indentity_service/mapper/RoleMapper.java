package com.tihuz.indentity_service.mapper;


import com.tihuz.indentity_service.dto.request.PermissionRequest;
import com.tihuz.indentity_service.dto.request.RoleRequest;
import com.tihuz.indentity_service.dto.response.PermissionResponse;
import com.tihuz.indentity_service.dto.response.RoleResponse;
import com.tihuz.indentity_service.entity.Permission;
import com.tihuz.indentity_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// báo đây là 1 Mapper quản lý bởi Spring
@Mapper(componentModel = "spring")
public interface RoleMapper {


@Mapping( target = "permissions",ignore = true)
Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);


}
