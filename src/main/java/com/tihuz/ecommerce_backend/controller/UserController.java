package com.tihuz.ecommerce_backend.controller;

import com.tihuz.ecommerce_backend.dto.response.ApiResponse;
import com.tihuz.ecommerce_backend.dto.request.UserCreationRequest;
import com.tihuz.ecommerce_backend.dto.request.UserUpdateRequest;
import com.tihuz.ecommerce_backend.dto.response.UserResponse;
import com.tihuz.ecommerce_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {

     UserService userService;
    @PostMapping
    ApiResponse <UserResponse> createUser(@RequestBody @Valid  UserCreationRequest request)
    {
        log.info("Controller: Create User");
        var result= userService.createUser(request);
        return ApiResponse.<UserResponse>builder()
                          .result(result)
                          .build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    // @PreAuthorize("hasAuthority('APPROVE_DATA')")
    @GetMapping
    List<UserResponse> getAllUsers ()
    {
        // Each request passes through security filters.
        // Authentication is extracted, validated, and stored in SecurityContextHolder.
        // The controller retrieves it to access user details.

        var authentication= SecurityContextHolder.getContext().getAuthentication();
        log.info("Username :{}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return userService.getAllUser();
    }


    //returnObject.username → username của user vừa lấy trong DB
    //authentication.name → username trong token JWT (người đang login)
    // @PostAuthorize(" hasRole('ADMIN') or returnObject.id == authentication.name")
    // SELECT BY ID
//    @PreAuthorize("hasRole('ADMIN')")
//    @PostAuthorize("returnObject.id.equals(authentication.name)")
    @PreAuthorize("hasRole('ADMIN') or #id.equals(authentication.name)")
    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable String userId)
    {
        return userService.getUser(userId);
    }

    @GetMapping("/me")
    ApiResponse <UserResponse> getMyInfo()
    {
        return ApiResponse.<UserResponse>builder()
                          .result(userService.getMyInfo())
                          .build();
    }

    @PutMapping("/{userId}")
    UserResponse updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request)
    {
        return userService.updateUser(userId,request);
    }

    @DeleteMapping ("/{userId}")
    String deleteUser( @PathVariable String userId)
    {
          userService.deleteUser(userId);
          return  "User has been deleted";
    }

}
