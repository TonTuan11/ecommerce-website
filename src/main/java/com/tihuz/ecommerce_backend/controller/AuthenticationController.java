package com.tihuz.ecommerce_backend.controller;

import com.nimbusds.jose.JOSEException;
import com.tihuz.ecommerce_backend.dto.request.*;
import com.tihuz.ecommerce_backend.dto.response.ApiResponse;
import com.tihuz.ecommerce_backend.dto.response.AuthenticationResponse;
import com.tihuz.ecommerce_backend.dto.response.IntrospectResponse;
import com.tihuz.ecommerce_backend.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticated(@RequestBody AuthenticationRequest request)
    {
        var result=   authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                          .result(result)
                          .build();

    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticated(@RequestBody IntrospectRequest request) throws ParseException, JOSEException
    {
          var  result = authenticationService.introspectResponse(request);
          return ApiResponse.<IntrospectResponse>builder()
                            .result(result)
                            .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException
    {
         authenticationService.logout(request);
         return ApiResponse.<Void>builder()
                           .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refresh (@RequestBody RefreshRequest request) throws ParseException, JOSEException
    {
        var result=   authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

}
