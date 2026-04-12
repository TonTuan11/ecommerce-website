package com.tihuz.ecommerce_backend.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tihuz.ecommerce_backend.dto.request.AuthenticationRequest;
import com.tihuz.ecommerce_backend.dto.request.IntrospectRequest;
import com.tihuz.ecommerce_backend.dto.request.LogoutRequest;
import com.tihuz.ecommerce_backend.dto.request.RefreshRequest;
import com.tihuz.ecommerce_backend.dto.response.AuthenticationResponse;
import com.tihuz.ecommerce_backend.dto.response.IntrospectResponse;
import com.tihuz.ecommerce_backend.entity.InvalidatedToken;
import com.tihuz.ecommerce_backend.entity.Role;
import com.tihuz.ecommerce_backend.entity.User;
import com.tihuz.ecommerce_backend.exception.AppException;
import com.tihuz.ecommerce_backend.exception.ErrorCode;
import com.tihuz.ecommerce_backend.repository.InvalidatedTokenRepository;
import com.tihuz.ecommerce_backend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

/*
Handle authentication and JWT token management:
        - Authenticate username/password (password is hashed).
        - Generate a JWT on successful login (include roles in the token).
        - Validate the token and check expiration.
        */
public class AuthenticationService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESH_DURATION;


    public IntrospectResponse introspectResponse(IntrospectRequest request)
            throws JOSEException, ParseException
    {
        var token = request.getToken();
        boolean isValid = true;

        try
        {
            // Use the verifyToken method
            verifyToken(token, false);
        }
        catch (AppException e)
        {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    // Login -> create token
    public AuthenticationResponse authenticate(AuthenticationRequest request)
    {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));

        // check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            throw new AppException(ErrorCode.FAIL_PASSWORD);
        }
        // Create token for the user
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();

    }


    //  Logout: Extract jti and exp → store in blacklist (InvalidatedToken).
    public void logout(LogoutRequest request) throws ParseException, JOSEException
    {
        try
        {
            var signToken = verifyToken(request.getToken(), true);  // sẽ ném nếu invalid/expired/revoked

            // Extract the JWTID (jti) from the token
            String jit = signToken.getJWTClaimsSet().getJWTID();

            // Extract expirationTime (exp) from the token
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            // build entity InvalidatedToken
            InvalidatedToken invalidatedToken = InvalidatedToken
                                                    .builder()
                                                    .id(jit)
                                                    .expiryTime(expiryTime)
                                                    .build();
            // lưu vào repo
            invalidatedTokenRepository.save(invalidatedToken);
        }
        catch (AppException e)
        {
            log.info("Token already expired");
        }
    }


    //Refresh Token
    public AuthenticationResponse refreshToken(RefreshRequest request)
            throws ParseException, JOSEException {

        var signJwt = verifyToken(request.getToken(), true); // nếu lấy ra được thì token còn hiệu thực
        var jit = signJwt.getJWTClaimsSet().getJWTID();
        var expiryTime = signJwt.getJWTClaimsSet().getExpirationTime();

        // thu hồi token, vô hiệu hóa (như cơ chế logout)
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

        //tìm theo  username
        //        var username=signJwt.getJWTClaimsSet().getSubject();
        //
        //
        //        var user=userRepository.findByUsername(username)
        //                .orElseThrow(
        //                        () -> new AppException(ErrorCode.UNAUTHENTICATED)
        //                );


        //tìm theo userId

        var userid = signJwt.getJWTClaimsSet().getSubject();

        var user = userRepository.findById(userid)
                .orElseThrow(
                        () -> new AppException(ErrorCode.UNAUTHENTICATED)
                );

        var token = generateToken(user);


        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }


    // Verify: valid signature, not expired, and not revoked.
    private SignedJWT verifyToken(String token, boolean isRefresh)
            throws JOSEException, ParseException {

        //Create verifier token
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        //Parse the token string ->  SignedJWT object.
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Lấy claim expiration
        Date expiryTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                .toInstant().plus(REFRESH_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();


        var verified = signedJWT.verify(verifier); // check sign HMAC

        if (!verified || expiryTime.before(new Date()))
            throw new AppException(ErrorCode.UNAUTHENTICATED); // Invalid signature or expired ->fail

        // If the jti is revoked → fail
        if (invalidatedTokenRepository
                .existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }


    // Create token JWT (SignedJWT with HS512) , có jti/exp/issuer/scope
    private String generateToken(User user) {

        // Use the HMAC-SHA-512 hashing algorithm.
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);


        // Claims
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())   //  ID user //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                .issuer("tihuz.com")
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS)))  // Expire after VALID_DURATION (seconds)
                .jwtID(UUID.randomUUID().toString())  //jti is used for revocation.
                .claim("scope", buildScope(user)) // User role and permissions.
                .build();


        // Create Payload (body token)
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // Pack the header and payload into a JWSObject.
        JWSObject jwsObject = new JWSObject(header,payload);

        // Sign the token
        try {
            // Create a MACSigner, Sign with SIGNER_KEY.
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

            // Convert JWT ( header, payload, signature) -> Compact Serialization (3 phần nối bằng dấu chấm)
            // This string is JWT token, returned to the client or used for authentication.
            return jwsObject.serialize();

        } catch (JOSEException e)
        {
            log.error("Can not create token");
            throw new RuntimeException(e);
        }
    }


    // Lấy tập hợp roles ( nối thành 1 chuỗi cách nhau bởi khoảng trắng )
//    private String buildScope(User user)
//    {
//        StringJoiner stringJoiner = new StringJoiner(" ");
//
//        if (!CollectionUtils.isEmpty(user.getRoles()))
//            user.getRoles()
//                    .forEach(role ->
//                                {
//                                    stringJoiner.add("ROLE_" + role.getName());
//                                    if (!CollectionUtils.isEmpty(role.getPermissions()))
//                                        role.getPermissions()
//                                                .forEach(permission ->
//                                                        stringJoiner.add(permission.getName()));
//                                });
//        return stringJoiner.toString();
//    }


    private String buildScope(User user)
    {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
        {
            user.getRoles().forEach(role -> stringJoiner.add("ROLE_" + role.getName()));
        }

        Set<String> permissions = getUserPermissions(user);
        if (!CollectionUtils.isEmpty(permissions))
        {
            permissions.forEach(stringJoiner::add);
        }
        return stringJoiner.toString();
    }



    public Set<String> getUserPermissions(User user)
    {
        Set<String> permissions= new HashSet<>();
        if(user.getRoles()!=null)
        {
            for (Role role: user.getRoles())
            {
                if(role.getPermissions()!=null)
                {
                    role.getPermissions().forEach(p-> permissions.add(p.getName()));
                }
            }
        }
        return permissions;
    }



}