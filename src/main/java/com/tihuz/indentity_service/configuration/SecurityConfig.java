package com.tihuz.indentity_service.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;


@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true )
@Configuration // annotation đánh dấu class là nơi định nghĩa cấu hình, method có @Bean đăng ký chúng vào ApplicationContext.
@EnableWebSecurity  // This annotation enables Spring Security in the application.
@EnableMethodSecurity  // annotaion để phân quyền ở tầng method (Preauthorize, Pos,..)
@RequiredArgsConstructor


/*Cấu hình chi tiết cho Spring Security, bao gồm:

  -Quy tắc phân quyền truy cập các API (ai được phép vào endpoint nào).

  -Cấu hình xác thực JWT (giải mã, chuyển scope thành quyền).

  -Cấu hình bảo vệ endpoint, bật Resource Server, set decoder & converter.

  -Định nghĩa bean PasswordEncoder, JwtDecoder, v.v.*/

public class SecurityConfig {

    // Các endpoint cho phép truy cập công khai (không cần xác thực) với phương thức POST.
    private final String[] PUBLIC_ENDPOINTS={ "/auth/**",};
    private final String[] PUBLIC_ENDPOINTS_GET={ "/products/**","/categories/**","/brands/**"};
    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private CustomJwtDecoder customJwtDecoder;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception
    {

        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // API thuần JSON → tắt CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // Cấu hình phân quyền truy cập
                .authorizeHttpRequests(request-> request
                        .requestMatchers(HttpMethod.POST,PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET,PUBLIC_ENDPOINTS_GET).permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        // swagger
                        .requestMatchers(
                                "/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // All other requests require authentication (login).
                        .anyRequest().authenticated());


        // Cấu hình ứng dụng thành OAuth2 Resource Server, dùng JWT làm phương thức xác thực
        // Thiết lập decoder để giải mã token JWT và converter để chuyển đổi scope thành authorities
        httpSecurity.oauth2ResourceServer
                (oauth2 -> oauth2
                        .jwt(
                                jwtConfigurer -> jwtConfigurer
                                             .decoder(customJwtDecoder)  //Decode and validate the token.
                                             .jwtAuthenticationConverter(jwtAuthenticationConverter())) //convert Jwt → Authentication (quyền/authorities).

                        //Call JwtAuthenticationEntryPoint → Return 401 Unauthorized when authentication fails.
                        .authenticationEntryPoint( jwtAuthenticationEntryPoint)
                );
        // Return the built configuration.
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