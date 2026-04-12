        package com.tihuz.ecommerce_backend.configuration;

        import com.nimbusds.jose.JOSEException;
        import com.tihuz.ecommerce_backend.dto.request.IntrospectRequest;
        import com.tihuz.ecommerce_backend.service.AuthenticationService;
        import lombok.AccessLevel;
        import lombok.experimental.FieldDefaults;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
        import org.springframework.security.oauth2.jwt.Jwt;
        import org.springframework.security.oauth2.jwt.JwtDecoder;
        import org.springframework.security.oauth2.jwt.JwtException;
        import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
        import org.springframework.stereotype.Component;

        import javax.crypto.spec.SecretKeySpec;
        import java.text.ParseException;


        //Task: stop request, call introspect to validate token (revoked or not), then decode JWT using NimbusJwtDecoder.
        @Component  //Create the object and manage it as a Spring Bean.
        @FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)

        public class CustomJwtDecoder implements JwtDecoder {

            // inject bean from the container
             AuthenticationService authenticationService;

            //  JWT decoder using HMAC-SHA512; initialized once and reused
             NimbusJwtDecoder nimbusJwtDecoder;


             public CustomJwtDecoder(  @Value("${jwt.signerKey}")
                                       String signerKey,AuthenticationService authenticationService )
             {

                 this.authenticationService=authenticationService;

                 //thread-safe
                 this.nimbusJwtDecoder= NimbusJwtDecoder
                         .withSecretKey(new SecretKeySpec(signerKey.getBytes(),"HS512"))
                         .macAlgorithm(MacAlgorithm.HS512)
                         .build();
             }

            @Override
            public Jwt decode(String token) throws JwtException
            {
                // Call method introspectResponse: verify signature, expiration, and revocation status.
                try
                {
                    var response = authenticationService.introspectResponse(IntrospectRequest.
                            builder()
                            .token(token)
                            .build());

                    if(!response.isValid())
                        // Invalid or revoked token → block
                        throw new JwtException("Token Invalid");

                }
                catch (JOSEException | ParseException  e)
                {
                    throw new JwtException(e.getMessage());
                }
                // Decode JWT  → Return Jwt to Spring Security
                return nimbusJwtDecoder.decode(token);
            }
        }
