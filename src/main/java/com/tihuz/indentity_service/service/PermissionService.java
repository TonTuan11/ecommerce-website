package com.tihuz.indentity_service.service;

import com.tihuz.indentity_service.entity.Permission;

import com.tihuz.indentity_service.dto.request.PermissionRequest;
import com.tihuz.indentity_service.dto.response.PermissionResponse;
import com.tihuz.indentity_service.mapper.PermissionMapper;
import com.tihuz.indentity_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)


@Service
public class PermissionService {

    PermissionRepository permissionRepository;
   PermissionMapper permissionMapper;

//  public PermissionResponse create(PermissionRequest request){
//
//      Permission permission = permissionMapper.toPermission(request);
//      permission= permissionRepository.save(permission);
//      return permissionMapper.toPermissionResponse(permission);
//    }


    public PermissionResponse   create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);

        // thử log để chắc chắn
        System.out.println("Before save: " + permission.getName());

        permissionRepository.save(permission);

        return permissionMapper.toPermissionResponse(permission);
    }

 public List<PermissionResponse> getAll()
    {

        var permission= permissionRepository.findAll();
        return permission.stream().map(permissionMapper::toPermissionResponse).toList();

    }

    public void delete(String permission)
    {
        permissionRepository.deleteById(permission);
    }
}
