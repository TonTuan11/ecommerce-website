package com.tihuz.indentity_service.configuration;

import com.nimbusds.jose.JOSEException;
import com.tihuz.indentity_service.dto.request.IntrospectRequest;
import com.tihuz.indentity_service.service.AuthenticationService;
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
@Component
public class CustomJwtDecoder implements JwtDecoder {


    // Lấy giá trị khóa bí mật để ký JWT từ file cấu hình application.yml hoặc application.properties
    @Value("${jwt.signerKey}")
    private String signerKey;

    @Autowired
    private AuthenticationService authenticationService;

    // Bộ giải mã JWT dùng HMAC-SHA512; tạo 1 lần rồi tái sử dụng
    private NimbusJwtDecoder nimbusJwtDecoder=null;

    @Override
    public Jwt decode(String token) throws JwtException {
        // 1) Gọi introspect: kiểm tra chữ ký/hết hạn/đã revoke hay chưa
        try {
            var response = authenticationService.introspectResponse(IntrospectRequest.builder()
                            .token(token)
                    .build());

            if(!response.isValid())
                //  Token không hợp lệ hoặc đã bị revoke → chặn ngay
                throw new JwtException("Token Invalid");

        }
        catch (JOSEException | ParseException  e) {
            throw new JwtException(e.getMessage());
        }

        // 2) Khởi tạo NimbusJwtDecoder (lazy) để decode claims → xây dựng Authentication

        if(Objects.isNull(nimbusJwtDecoder))
        {

            // Tạo key bí mật với thuật toán HMAC SHA512 từ chuỗi ký bí mật
            SecretKeySpec spec=new SecretKeySpec(signerKey.getBytes(),"HS512");


            // Trả về bộ decoder JWT với cấu hình khóa và thuật toán đã cho
            nimbusJwtDecoder= NimbusJwtDecoder
                    .withSecretKey(spec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();

        }
        // 3) Decode JWT (đã qua bước introspect) → trả Jwt cho Spring Security
        return nimbusJwtDecoder.decode(token);
    }
}
