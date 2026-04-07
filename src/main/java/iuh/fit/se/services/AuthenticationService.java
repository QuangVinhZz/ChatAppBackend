package iuh.fit.se.services;

import iuh.fit.se.dtos.request.AuthenticationRequest;
import iuh.fit.se.dtos.request.IntrospectRequest;
import iuh.fit.se.dtos.response.AuthenticationResponse;
import iuh.fit.se.dtos.response.IntrospectResponse;
import iuh.fit.se.entities.AccountCredential;
import iuh.fit.se.entities.User;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse refreshToken(iuh.fit.se.dtos.request.RefreshTokenRequest request);
    IntrospectResponse introspect(IntrospectRequest request);
    String generateAccessToken(User user, AccountCredential accountCredential);
    String generateRefreshToken(User user, AccountCredential accountCredential);
    void logout(iuh.fit.se.dtos.request.LogoutRequest request);
}
