package com.tihuz.ecommerce_backend.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tihuz.ecommerce_backend.dto.response.ApiResponse;
import com.tihuz.ecommerce_backend.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // use ObjectMapper to convert Java objects -> Json
    ObjectMapper objectMapper;   // Assign value once

    // Spring Security calls commence(...) whenever an AuthenticationException occurs (unauthenticated, invalid token, expired token, etc.).
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        ErrorCode errorCode=ErrorCode.UNAUTHENTICATED;
        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // return response
        ApiResponse<?> apiResponse= ApiResponse
                .builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        //Convert ApiResponse to JSON → Write to the response body → send the response to the client
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer(); // Send buffered data to the client.
        //write() only writes to the in-memory buffer and may not send it to the client yet
    }
}
