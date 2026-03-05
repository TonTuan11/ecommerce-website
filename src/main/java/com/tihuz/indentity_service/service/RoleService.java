package com.tihuz.indentity_service.service;


import com.tihuz.indentity_service.dto.request.RoleRequest;
import com.tihuz.indentity_service.dto.response.RoleResponse;
import com.tihuz.indentity_service.entity.Permission;
import com.tihuz.indentity_service.exception.AppException;
import com.tihuz.indentity_service.exception.ErrorCode;
import com.tihuz.indentity_service.exception.ErrorContext;
import com.tihuz.indentity_service.mapper.RoleMapper;
import com.tihuz.indentity_service.repository.PermissionRepository;
import com.tihuz.indentity_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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



    public RoleResponse create(RoleRequest request) {

        // 1. Permission từ request
        // dùng Set để loại bỏ duplicate
        // và dùng các phép toán tập hợp removeAll
        Set<String> requested = new HashSet<>(request.getPermissions());

        // 2.Tìm Permission tồn tại trong DB
        var foundPermissions = permissionRepository.findAllById(requested);

        String.valueOf(foundPermissions);

        Set<String> found = foundPermissions
                .stream()
                .map(Permission::getName) // với mỗi object permission lấy ra tên của nó
                .collect(Collectors.toSet()); //  thu thập lại thành 1 Set
                                                // là chuyển từ List<Permission> sang Set<String> (chứa tên)


        // 3. Permission bị thiếu
        requested.removeAll(found);

        // nếu requested còn phần từ thì nghĩa là đang có permission không tồn tại trong db
        if (!requested.isEmpty()) {

            ErrorContext.set(requested);

            throw new AppException(ErrorCode.PERMISSION_NOTEXISTED);
        }

        // 4. Tạo role (CHỈ chạy khi không có lỗi)
        var role = roleMapper.toRole(request);
        role.setPermissions(new HashSet<>(foundPermissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }


    public List<RoleResponse> getAll()
    {
        var roles= roleRepository.findAll();

        return roles.stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void delete(String role)
    {
        roleRepository.deleteById(role);
    }


}
