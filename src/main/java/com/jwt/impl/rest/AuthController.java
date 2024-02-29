package com.jwt.impl.rest;

import com.jwt.impl.core.persistance.entity.User;
import com.jwt.impl.core.service.AuthenticationService;
import com.jwt.impl.rest.payload.request.*;
import com.jwt.impl.rest.payload.response.SignInResponse;
import com.jwt.impl.rest.payload.response.TokenRefreshResponse;
import com.jwt.impl.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public AuthController(AuthenticationService userService, JwtService jwtService) {
        this.authenticationService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/api/v1/auth/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest singUpRequest) {
        authenticationService.userSignUp(singUpRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v1/auth/sign-in")
    public ResponseEntity<SignInResponse> authenticate(@RequestBody SignInRequest singInRequest) {
        User authenticatedUser = authenticationService.authenticate(singInRequest);
        String jwtToken = jwtService.generateToken(authenticatedUser.getEmail());
        SignInResponse signInResponse = new SignInResponse(jwtToken);
        return ResponseEntity.ok(signInResponse);
    }

    @PostMapping("/api/v1/auth/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest tokenRefreshRequest) {
        String requestRefreshToken = tokenRefreshRequest.refreshToken();

        if (jwtService.validateRefreshToken(requestRefreshToken)) {
            String email = jwtService.extractEmail(requestRefreshToken);
            String newAccessToken = jwtService.generateToken(email);
            String newRefreshToken = jwtService.generateRefreshToken(email);
            return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken, newRefreshToken));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid refresh token");
        }
    }

    @PostMapping("/api/v1/auth/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {

        boolean isPasswordChanged = authenticationService.changePassword(changePasswordRequest);

        if (isPasswordChanged) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Password could not be changed"));
        }
    }

    @DeleteMapping("/api/v1/auth/delete-account")
    public ResponseEntity<?> deleteAccount(Authentication authentication) {
        boolean isDeleted = authenticationService.deleteAccount(authentication);
        if (isDeleted) {
            return ResponseEntity.ok(Map.of("message", "Account successfully deleted."));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Account not found."));
        }
    }

}
