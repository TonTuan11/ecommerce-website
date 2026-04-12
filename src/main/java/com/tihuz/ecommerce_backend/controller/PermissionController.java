package com.tihuz.ecommerce_backend.controller;


import com.tihuz.ecommerce_backend.dto.response.ApiResponse;
import com.tihuz.ecommerce_backend.dto.request.PermissionRequest;
import com.tihuz.ecommerce_backend.dto.response.PermissionResponse;
import com.tihuz.ecommerce_backend.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

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
        return ApiResponse.<PermissionResponse>builder()
                          .build();
    }

}
