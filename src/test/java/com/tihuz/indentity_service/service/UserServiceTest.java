package com.tihuz.indentity_service.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tihuz.indentity_service.dto.request.UserCreationRequest;
import com.tihuz.indentity_service.dto.response.UserResponse;
import com.tihuz.indentity_service.entity.User;
import com.tihuz.indentity_service.exception.AppException;
import com.tihuz.indentity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "tiz2")

@FieldDefaults(level = AccessLevel.PRIVATE)
@TestPropertySource("/test.properties")  // cấu hình riêng cho môi trường test bằng file "test.properties"
public class UserServiceTest {

    @Autowired
    UserService userService;       // service thật, không mock ( để test logic )

    @MockitoBean
    UserRepository userRepository;     // repository giả (mock)


     UserCreationRequest request;
     UserResponse userResponse;
     User user;
     ObjectMapper objectMapper;

    @BeforeEach
    void initData() {


        LocalDate dob = LocalDate.of(2003, 10, 25);


        //request là dữ liệu client gửi lên.
        request = UserCreationRequest.builder()
                .username("tiz2")
                .firstname("Tiz")
                .lastname("Nguyen")
                .password("12345678")
                .dob(dob)
                .build();

//        userResponse = UserResponse.builder()
//                .id("d3a3baff4dc4")
//                .username("tiz2")
//                .firstname("Tiz")
//                .lastname("Nguyen")
//                .dob(dob)
//                .build();


        //user là entity giả mà repository sẽ trả về khi gọi save().
        user=User.builder()
                .id("d3a3baff4dc4")
                .username("tiz2")
                .firstname("Tiz")
                .lastname("Nguyen")
                .dob(dob)
                .build();
    }


    @Test
    void createUser_validRequest_success()
    {
        //GIVEN
        when(userRepository.existsByUsername(anyString()))
                .thenReturn(false);   // giả lập: username chưa tồn tại

        when(userRepository.save(any())).thenReturn(user);   // giả lập: khi lưu thì trả về entity user đã chuẩn bị

        //WHEN - gọi method cần test

       var response= userService.createUser(request);


        //THEN - kiểm tra kết quả
        Assertions.assertNotNull(response);  // đảm bảo không null
        Assertions.assertEquals("d3a3baff4dc4", response.getId());
        Assertions.assertEquals("tiz2", response.getUsername()); // từ request, chứ không lấy từ entity mock


    }


    @Test
    void createUser_userExisted_fail()
    {
        //GIVEN
        when(userRepository.existsByUsername(anyString()))
                .thenReturn(true);  // giả lập: username đã tồn tại

        //WHEN


        var exception= Assertions.assertThrows(AppException.class,
                ()-> userService.createUser(request));

      //THEN
        Assertions.assertEquals(1002, exception.getErrorCode().getCode());



    }


    @Test
    @WithMockUser(username ="tiz2")
    void getMyInfo_valid_success() {

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));


        var response = userService.getMyInfo();

        Assertions.assertEquals("tiz2",response.getUsername());
        Assertions.assertEquals("d3a3baff4dc4",response.getId());
//        assertThat(response.getUsername()).isEqualTo("tiz2");
//        assertThat(response.getId()).isEqualTo("d3a3baff4dc4");
    }

    @Test
    @WithMockUser(username ="tiz2")
    void getMyInfo_userNotFound_error() {

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));


        var exception= Assertions.assertThrows(AppException.class,
                ()-> userService.getMyInfo());


        Assertions.assertEquals(1005, exception.getErrorCode().getCode());


    }



}
