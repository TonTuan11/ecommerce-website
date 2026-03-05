package com.tihuz.indentity_service.controller;


import com.tihuz.indentity_service.dto.response.ApiResponse;
import com.tihuz.indentity_service.dto.request.RoleRequest;
import com.tihuz.indentity_service.dto.response.PermissionResponse;
import com.tihuz.indentity_service.dto.response.RoleResponse;
import com.tihuz.indentity_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")

// thay vì dùng  @Autowired cho từng biến thì dùng RequiredArgsConstructor
@RequiredArgsConstructor

// makeFinal = true field không khai báo thì thành final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@Slf4j
public class RoleController {

    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request)
    {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

@GetMapping
    ApiResponse<List<RoleResponse>> getAll()
    {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    ApiResponse<PermissionResponse> delete( @PathVariable String role)
    {
        roleService.delete(role);
        return ApiResponse.<PermissionResponse>builder().build();
    }

}
