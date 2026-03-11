package com.tihuz.ecommerce_backend.configuration;

import com.nimbusds.jose.JOSEException;
import com.tihuz.ecommerce_backend.dto.request.IntrospectRequest;
import com.tihuz.ecommerce_backend.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

//Nhiệm vụ: chặn mọi request, gọi introspect để kiểm tra token (đã revoke/chưa), sau đó mới decode JWT bằng NimbusJwtDecoder.
@Component  //Create the object and manage it as a Spring Bean.
@FieldDefaults(level = AccessLevel.PRIVATE)

public class CustomJwtDecoder implements JwtDecoder {

    // signerKey
    @Value("${jwt.signerKey}")
     String signerKey;

    @Autowired     // inject bean from the container
     AuthenticationService authenticationService;

    //  JWT decoder using HMAC-SHA512; initialized once and reused
     NimbusJwtDecoder nimbusJwtDecoder=null;

    @Override
    public Jwt decode(String token) throws JwtException {
        // 1) Call method introspectResponse: verify signature, expiration, and revocation status.
        try
        {
            var response = authenticationService.introspectResponse(IntrospectRequest
                    .builder()
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

        // 2) Initialize NimbusJwtDecoder (lazy) to decode claims → build Authentication
        if(Objects.isNull(nimbusJwtDecoder))    //Create decoder only once.
        {
            // Create a key using the "HMAC-SHA512" algorithm from signerKey.
            SecretKeySpec spec=new SecretKeySpec(signerKey.getBytes(),"HS512");

            // Trả về bộ decoder JWT với cấu hình khóa và thuật toán đã cho
            nimbusJwtDecoder= NimbusJwtDecoder
                    .withSecretKey(spec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();

        }
        // 3) Decode JWT (đã qua bước introspect) → Return Jwt to Spring Security
        return nimbusJwtDecoder.decode(token);
    }
}
