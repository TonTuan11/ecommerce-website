package com.tihuz.ecommerce_backend.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tihuz.ecommerce_backend.dto.response.ApiResponse;
import com.tihuz.ecommerce_backend.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor  //Create constructor only for final or @NonNull fields.
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {


    // use ObjectMapper to convert Java objects -> Json
    private final ObjectMapper objectMapper;   // Assign value once

//    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//    }

    
    //Spring Security sẽ gọi commence(...) mỗi khi có AuthenticationException (chưa login, token sai, token hết hạn, …).
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        ErrorCode errorCode=ErrorCode.UNAUTHENTICATED;

        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);


        // Trả về respone theo code và message
        ApiResponse<?> apiResponse= ApiResponse
                                    .builder()
                                    .code(errorCode.getCode())
                                    .message(errorCode.getMessage())
                                    .build();



        //Convert ApiResponse to JSON → Write to the response body → send the response to the client
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer(); //ép response gửi ngay dữ liệu từ buffer xuống client.
                                //Thường thì write() chỉ ghi vào buffer trong bộ nhớ, chưa chắc đã đẩy xuống client.
    }



}
