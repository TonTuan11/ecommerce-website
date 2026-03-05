package com.tihuz.indentity_service.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tihuz.indentity_service.dto.request.UserCreationRequest;
import com.tihuz.indentity_service.dto.response.UserResponse;
import com.tihuz.indentity_service.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



import java.time.LocalDate;


import static org.mockito.Mockito.when;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@SpringBootTest  // chạy nguyên context Spring Boot (load đầy đủ bean)
@AutoConfigureMockMvc    // tự động cấu hình MockMvc để giả lập HTTP request
@TestPropertySource("/test.properties")   // cấu hình riêng cho môi trường test bằng file "test.properties"
class UserControllerTest {

    @Autowired
     MockMvc mvc;      // MockMvc cho phép giả lập call API đến controller

    @MockitoBean
     UserService userService;            // thay thế UserService trong context bằng 1 mock (fake object)

     UserCreationRequest request;      // request giả lập gửi từ client
     UserResponse userResponse;   // response giả lập trả về từ service
     ObjectMapper objectMapper;   // convert Object <-> JSON

    @BeforeEach //tự động chạy trước mỗi test case.
    void initData() {


        // Dữ liệu chung cho các test case
        LocalDate dob = LocalDate.of(2003, 10, 25);

        // Request giả lập (client gửi lên)
        request = UserCreationRequest.builder()
                .username("tiz2")
                .firstname("Tiz")
                .lastname("Nguyen")
                .password("12345678")
                .dob(dob)
                .build();


        // Response giả lập (service trả về)
        userResponse = UserResponse.builder()
                .id("d3a3baff4dc4")
                .username("tiz2")
                .firstname("Tiz")
                .lastname("Nguyen")
                .dob(dob)
                .build();
    }


    // CreateUser success
    @Test
    void createUser_validRequest_success() throws Exception {
        // GIVEN
        objectMapper = new ObjectMapper();

        // cần đăng ký module này để ObjectMapper hiểu kiểu LocalDate
        objectMapper.registerModule(new JavaTimeModule());


        // khi service.createUser được gọi với bất kỳ tham số nào -> giả lập trả về userResponse
        when(userService.createUser(ArgumentMatchers.any()))
                .thenReturn(userResponse);

        // WHEN - gọi API POST /users
        // THEN - kiểm tra kết quả trả về
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)      // gửi dạng JSON
                        .content(objectMapper.writeValueAsString(request)))       // request body
                .andExpect(status().isOk())// request body
                .andExpect(jsonPath("code").value(1000))                  // field "code" = 1000
                .andExpect(jsonPath("result.id").value("d3a3baff4dc4"));  // result.id đúng giá trị

    }


    //Valid username
    @Test
    void createUser_usernameInvalid_fail() throws Exception {

        // GIVEN - đổi request thành username quá ngắn (invalid)
        request.setUsername("tiz");
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());


        // WHEN - gọi API POST /users
        // THEN - vì username invalid -> controller sẽ fail validation
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())                                     // HTTP 400
                .andExpect(jsonPath("code").value(1003))          // code lỗi = 1003
                .andExpect(jsonPath("message").value("Username must be at least 4 character"));
                            // message trả về đúng như validate
    }


    // Valid Password
    @Test
    void createUser_passwordInvalid_fail() throws Exception {
        //GIVEN-
        request.setPassword("12345");
        objectMapper=new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        //WHEN-THEN
        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(1004))
                .andExpect(jsonPath("message").value("Password must be at least 6 character"));

    }



    //NotNull DOB
    @Test
    void createUser_dobNull_fail() throws Exception {

        request.setDob(null);
         objectMapper=new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(1009))
                .andExpect(jsonPath("message").value("DOB must not be null"));

    }



    // Valid DOB
    @Test
    void createUser_dobInvalid_fail() throws Exception {
        LocalDate dob = LocalDate.of(20009, 10, 25);

        //GIVEN
        request.setDob(dob);
        objectMapper=new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());


        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(1008))
                .andExpect(jsonPath("message").value("Your age must be at least 16"));


    }


}



