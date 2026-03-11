package com.tihuz.ecommerce_backend.controller;


import com.tihuz.ecommerce_backend.dto.response.ApiResponse;
import com.tihuz.ecommerce_backend.dto.request.RoleRequest;
import com.tihuz.ecommerce_backend.dto.response.PermissionResponse;
import com.tihuz.ecommerce_backend.dto.response.RoleResponse;
import com.tihuz.ecommerce_backend.service.RoleService;
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
