package com.tihuz.indentity_service.configuration;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;


@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true )
@Configuration // annotation đánh dấu class là nơi định nghĩa cấu hình, method có @Bean đăng ký chúng vào ApplicationContext.
@EnableWebSecurity  // annotation này Kích hoạt Spring Security trong ứng dụng.
@EnableMethodSecurity  // annotaion để phân quyền ở tầng method (Preauthorize, Pos,..)
@RequiredArgsConstructor     //chỉ inject các field final


/*Cấu hình chi tiết cho Spring Security, bao gồm:

  -Quy tắc phân quyền truy cập các API (ai được phép vào endpoint nào).

  -Cấu hình xác thực JWT (giải mã, chuyển scope thành quyền).

  -Cấu hình bảo vệ endpoint, bật Resource Server, set decoder & converter.

  -Định nghĩa bean PasswordEncoder, JwtDecoder, v.v.*/

public class SecurityConfig {

    // Các endpoint cho phép truy cập công khai (không cần xác thực) với phương thức POST.
    private final String[] PUBLIC_ENDPOINTS={ "/users","/auth/token","/auth/introspect","/auth/logout","/auth/refresh",};



    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private CustomJwtDecoder customJwtDecoder;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                // Cấu hình phân quyền truy cập
                .authorizeHttpRequests(request-> request

                        // Cho phép public POST tới các endpoint trong PUBLIC_ENDPOINTS
                                 .requestMatchers(HttpMethod.POST,PUBLIC_ENDPOINTS).permitAll()
                                .requestMatchers("/uploads/**").permitAll()


                                // Chỉ cho phép user có role ADMIN được GET /users
                              //  .requestMatchers(HttpMethod.GET, "/users").hasRole(Role.ADMIN.name())

                                // Các request khác đều cần xác thực (đăng nhập)
                        .anyRequest().authenticated());



        // Cấu hình ứng dụng thành OAuth2 Resource Server, dùng JWT làm phương thức xác thực
        // Thiết lập decoder để giải mã token JWT và converter để chuyển đổi scope thành authorities
        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(customJwtDecoder)  //giải mã & kiểm tra token.

                                .jwtAuthenticationConverter(jwtAuthenticationConverter())) // sẽ chuyển Jwt → Authentication (quyền/authorities).

                        //gọi JwtAuthenticationEntryPoint → khi có lỗi xác thực trả 401 Unauthorized.
                        .authenticationEntryPoint( jwtAuthenticationEntryPoint)


        )

        // API thuần JSON → tắt CSRF
                            .csrf(AbstractHttpConfigurer::disable);

        // Trả về cấu hình đã xây dựng
        return httpSecurity.build();
    }



    // Chuyển claim "scope" (vd: "ADMIN USER") thành GrantedAuthority.
    // setAuthorityPrefix("ROLE_"): "ADMIN" -> "ROLE_ADMIN"
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter()
    {
        //Luồng:  Dùng JwtGrantedAuthoritiesConverter để lấy field scope từ JWT claim
        //        Tách chuỗi "ROLE_ADMIN read write" → thành list các GrantedAuthority

        // Bóc authorities từ claim "scope" (chuỗi cách nhau bởi space)
        JwtGrantedAuthoritiesConverter converter  = new JwtGrantedAuthoritiesConverter();

        // đã build "ROLE_" sẵn trong token → không cần prefix thêm
        converter .setAuthorityPrefix("" );  //bỏ "SCOPE_"

        JwtAuthenticationConverter jwtAuthConverter =new JwtAuthenticationConverter();

        // Sử dụng converter trên để chuyển đổi các scope thành authority
        jwtAuthConverter .setJwtGrantedAuthoritiesConverter(converter);

        return  jwtAuthConverter ;

    }



    /**
     * Bean JwtDecoder để giải mã và xác thực token JWT.
     * Cấu hình sử dụng thuật toán HS512 (HMAC-SHA512) với khóa bí mật.
     */

//    @Bean
//    JwtDecoder jwtDecoder()
//    {
//        // Tạo key bí mật với thuật toán HMAC SHA512 từ chuỗi ký bí mật
//        SecretKeySpec spec=new SecretKeySpec(signerKey.getBytes(),"HS512");
//
//        // Trả về bộ decoder JWT với cấu hình khóa và thuật toán đã cho
//    return NimbusJwtDecoder.
//            withSecretKey(spec)
//            .macAlgorithm(MacAlgorithm.HS512)
//            .build();
//    }


    /**
     * Bean PasswordEncoder dùng mã hóa mật khẩu toàn ứng dụng.
     * Ở đây dùng BCrypt với độ mạnh 10 (số rounds mã hóa).
     */

//    @Bean
//    PasswordEncoder passwordEncoder()
//    {
//        // BCrypt strength 10
//        return new BCryptPasswordEncoder(10);
//    }

}