package com.tihuz.ecommerce_backend.service;

import com.tihuz.ecommerce_backend.dto.request.RoleRequest;
import com.tihuz.ecommerce_backend.dto.response.RoleResponse;
import com.tihuz.ecommerce_backend.entity.Permission;
import com.tihuz.ecommerce_backend.exception.AppException;
import com.tihuz.ecommerce_backend.exception.ErrorCode;
import com.tihuz.ecommerce_backend.exception.ErrorContext;
import com.tihuz.ecommerce_backend.mapper.RoleMapper;
import com.tihuz.ecommerce_backend.repository.PermissionRepository;
import com.tihuz.ecommerce_backend.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Service
public class RoleService {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

//    public RoleResponse create(RoleRequest request)
//    {
//        var role= roleMapper.toRole(request);
//
//
//        var permissions= permissionRepository.findAllById(request.getPermissions());
//
//        role.setPermissions(new HashSet<>(permissions));
//
//        role=roleRepository.save(role);
//        return  roleMapper.toRoleResponse(role);
//
//    }



    public RoleResponse create(RoleRequest request)
    {

        Set<String> requested = new HashSet<>(request.getPermissions());

        // Find permission in the database
        var foundPermissions = permissionRepository.findAllById(requested);

        Set<String> found = foundPermissions
                .stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());

        // Permission does not exist in the database
        requested.removeAll(found);

        if (!requested.isEmpty())
        {
            ErrorContext.set(requested);
            throw new AppException(ErrorCode.PERMISSION_NOTEXISTED);
        }

        // 4. Tạo role (CHỈ chạy khi không có lỗi)
        var role = roleMapper.toRole(request);
        role.setPermissions(new HashSet<>(foundPermissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleResponse> getAll()
    {
        var roles= roleRepository.findAll();

        return roles.stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String role)
    {
        roleRepository.deleteById(role);
    }


}
