package com.tihuz.ecommerce_backend.controller;


import com.tihuz.ecommerce_backend.dto.response.ApiResponse;
import com.tihuz.ecommerce_backend.dto.request.PermissionRequest;
import com.tihuz.ecommerce_backend.dto.response.PermissionResponse;
import com.tihuz.ecommerce_backend.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")

// thay vì dùng  @Autowired cho từng biến thì dùng RequiredArgsConstructor
@RequiredArgsConstructor

// makeFinal = true field không khai báo thì thành final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@Slf4j
public class PermissonController {

    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request)
    {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

@GetMapping
    ApiResponse<List<PermissionResponse>> getAll()
    {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<PermissionResponse> delete( @PathVariable String permission)
    {
        permissionService.delete(permission);
        return ApiResponse.<PermissionResponse>builder().build();
    }

}
