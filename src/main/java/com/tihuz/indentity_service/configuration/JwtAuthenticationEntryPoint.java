package com.tihuz.indentity_service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tihuz.indentity_service.dto.response.ApiResponse;
import com.tihuz.indentity_service.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {


    private final ObjectMapper objectMapper;   // thuộc tính

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    
    //Spring Security sẽ gọi commence(...) mỗi khi có AuthenticationException (chưa login, token sai, token hết hạn, …).
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        ErrorCode errorCode=ErrorCode.UNAUTHENTICATED;

        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);


        // Trả về respone theo code và message
        ApiResponse<?> apiResponse= ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();



        //Convert ApiResponse thành JSON → ghi vào body response → đẩy response xuống client ngay

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer(); //ép response gửi ngay dữ liệu từ buffer xuống client.
                                //Thường thì write() chỉ ghi vào buffer trong bộ nhớ, chưa chắc đã đẩy xuống client.
    }



}
