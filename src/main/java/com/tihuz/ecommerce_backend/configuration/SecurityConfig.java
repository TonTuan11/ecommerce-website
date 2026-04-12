package com.tihuz.ecommerce_backend.configuration;

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
@Configuration
@EnableWebSecurity  // This annotation enables Spring Security in the application.
@EnableMethodSecurity  //  Annotation for method-level authorization  (using Preauthorize, Pos,..)
@RequiredArgsConstructor

/*Detailed configuration for Spring Security:
  -API access authorization
  - Configure JWT authentication (decode token, map scopes to authorities).
  -Configure endpoint security, enable Resource Server, and set decoder & converter.
 */

public class SecurityConfig {

     // Publicly accessible endpoints.
     String[] PUBLIC_ENDPOINTS={ "/users","/auth/**",};
     String[] PUBLIC_ENDPOINTS_GET={ "/products/**","/categories/**","/brands/**","/product-image/image/**"};

    @Autowired
     CorsConfigurationSource corsConfigurationSource;
     JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
     CustomJwtDecoder customJwtDecoder;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception
    {

        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // turnoff CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // Configure access authorization
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


        // Configure the application as an OAuth2 Resource Server using JWT.
        httpSecurity.oauth2ResourceServer
                (oauth2 -> oauth2
                        .jwt(                // Configure JWT authentication (decode token, map scopes to authorities)
                                jwtConfigurer -> jwtConfigurer
                                             .decoder(customJwtDecoder)  //Decode and validate the token.
                                             .jwtAuthenticationConverter(jwtAuthenticationConverter())) //convert Jwt → Authentication (quyền/authorities).

                        //Call JwtAuthenticationEntryPoint → Return 401 Unauthorized when authentication fails.
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                );
        // Return the built configuration.
        return httpSecurity.build();
    }


    // Convert claim "scope" (vd: "ADMIN USER") to GrantedAuthority.
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter()
    {
        //Flow:  JwtGrantedAuthoritiesConverter reads the "scope" field from JWT claims.
        //        "ROLE_ADMIN read write" → into a list of GrantedAuthority

        JwtGrantedAuthoritiesConverter converter  = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthConverter =new JwtAuthenticationConverter();
        // Using variable "converter" convert to "scope" -> authority
        jwtAuthConverter .setJwtGrantedAuthoritiesConverter(converter);
        return  jwtAuthConverter ;

    }

//    @Bean
//    JwtDecoder jwtDecoder()
//    {
//
//        SecretKeySpec spec=new SecretKeySpec(signerKey.getBytes(),"HS512");
//    return NimbusJwtDecoder.
//            withSecretKey(spec)
//            .macAlgorithm(MacAlgorithm.HS512)
//            .build();
//    }

}