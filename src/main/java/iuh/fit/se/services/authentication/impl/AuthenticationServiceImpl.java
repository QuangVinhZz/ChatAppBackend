package iuh.fit.se.services.authentication.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import iuh.fit.se.dtos.request.AuthenticationRequest;
import iuh.fit.se.dtos.request.IntrospectRequest;
import iuh.fit.se.dtos.response.AuthenticationResponse;
import iuh.fit.se.dtos.response.IntrospectResponse;
import iuh.fit.se.entities.AccountCredential;
import iuh.fit.se.entities.User;
import iuh.fit.se.entities.enums.HttpCode;
import iuh.fit.se.entities.enums.TokenType;
import iuh.fit.se.exceptions.AppException;
import iuh.fit.se.mapper.AccountMapper;
import iuh.fit.se.mapper.UserMapper;
import iuh.fit.se.repositories.AccountCredentialRepository;
import iuh.fit.se.repositories.RoleRepository;
import iuh.fit.se.repositories.UserRepository;
import iuh.fit.se.services.authentication.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    AccountMapper accountMapper;
    UserMapper userMapper;
    AccountCredentialRepository accountCredentialRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @NonFinal
    @Value("${jwt.secret-key}")
    String SECRET_KEY;

    @NonFinal
    @Value("${jwt.access-token-time}")
    long ACCESS_TOKEN_TIME;

    @NonFinal
    @Value("${jwt.refresh-token-time}")
    long REFRESH_TOKEN_TIME;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        AccountCredential accountCredential = accountCredentialRepository.findByCredential(request.getIdentifier());
        if(accountCredential == null) throw new AppException(HttpCode.ACCOUNT_NOT_FOUND);
        if(!accountCredential.getIsVerified()) throw new AppException(HttpCode.DISABLE_ACCOUNT);
        if(!passwordEncoder.matches(request.getPassword(), accountCredential.getPassword()))
            throw new AppException(HttpCode.PASSWORD_INCORRECT);
        User user = userRepository.findById(accountCredential.getUser().getId())
                .orElseThrow(()-> new NullPointerException("User not found!"));

        String accessToken = generateAccessToken(user, accountCredential);
        String refreshToken = generateRefreshToken(user, accountCredential);

        return AuthenticationResponse.builder()
                .authenticated(true)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(TokenType.BEARER.getTokenType())
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token);
        } catch (Exception e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    @Override
    public String generateAccessToken(User user, AccountCredential accountCredential){
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(accountCredential.getCredential())
                .issuer("user664dntp.dev")
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(ACCESS_TOKEN_TIME, ChronoUnit.MINUTES)))
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString())
                .build();
        return signToken(jwtClaimsSet);
    }

    public String generateRefreshToken(User user, AccountCredential accountCredential){
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(accountCredential.getCredential())
                .issuer("user664dntp.dev")
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(REFRESH_TOKEN_TIME, ChronoUnit.MINUTES)))
                .jwtID(UUID.randomUUID().toString())
                .build();
        return signToken(claimsSet);
    }

    private String signToken(JWTClaimsSet claimsSet){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public SignedJWT verifyToken(String token){
        try {
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            boolean verified = signedJWT.verify(verifier);

            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if(!(verified && expiryTime.after(new Date())))
                throw new AppException(HttpCode.UNAUTHENTICATED);
//            if(redisService.isTokenInvalidated(signedJWT.getJWTClaimsSet().getJWTID()))
//                throw new AppException(HttpCode.UNAUTHENTICATED);
            return signedJWT;
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
    private String buildScope(User user){
        StringJoiner roles = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> roles.add(role.getName()));
        return roles.toString();
    }
}
