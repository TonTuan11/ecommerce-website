package com.tihuz.indentity_service.controller;


import com.tihuz.indentity_service.dto.response.ApiResponse;
import com.tihuz.indentity_service.dto.request.UserCreationRequest;
import com.tihuz.indentity_service.dto.request.UserUpdateRequest;
import com.tihuz.indentity_service.dto.response.UserResponse;
import com.tihuz.indentity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// API controller chính


@RestController
@RequestMapping("/users")

// thay vì dùng  @Autowired cho từng biến thì dùng RequiredArgsConstructor
@RequiredArgsConstructor

// makeFinal = true field không khai báo thì thành final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@Slf4j

public class UserController {


    // Spring tự động tạo ra (inject) một đối tượng từ RequiredArgsConstructor thay vì dùng Autowired
     UserService userService;




    // Tạo User
    // nhận UserCreationRequest từ client -> sang entity -> gọi Userservice để xử lí
    @PostMapping
    // khai báo Valid để cho Spring biết cần Validation object này
    ApiResponse <UserResponse> createUser(@RequestBody @Valid  UserCreationRequest request)
    {
        log.info("Controller: Create User");
        var result= userService.createUser(request);

        return ApiResponse.<UserResponse>builder()
                .result(result)
                .build();

    //    ApiResponse <UserResponse> apiResponse=new ApiResponse<>();
//        apiResponse.setResult(userService.createUser(request));
//       return apiResponse;

    }

    // lấy all user
    @GetMapping
    List<UserResponse> getAllUsers ()
    {
        //Cơ chế của SecurityContextHolder

//        mỗi request gửi đến backend sẽ đi qua một chuỗi filter.
//        trong đó, filter xác thực (thường là JwtAuthenticationFilter hoặc UsernamePasswordAuthenticationFilter) sẽ:
//        1 Lấy token (hoặc thông tin đăng nhập) từ request.
//        2 Xác thực thành công → tạo ra một object Authentication.
//        3 Lưu Authentication vào SecurityContextHolder.getContext()

        var authentication= SecurityContextHolder.getContext().getAuthentication();
        log.info("Username :{}", authentication.getName());
      authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return userService.getAllUser();
    }



    //lấy 1 user
    // {userId} biến động (path variable) lấy từ URL
    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable String userId)      //  @PathVariable lấy giá trị của {userId} từ URL và gán vào userId
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



    // UPDATE

    @PutMapping("/{userId}")
    UserResponse updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request)
    {

        return userService.updateUser(userId,request);
    }


    // DELETE
    @DeleteMapping ("/{userId}")
    String deleteUser( @PathVariable String userId)
    {
          userService.deleteUser(userId);
          return  "User has been deleted";
    }



}
