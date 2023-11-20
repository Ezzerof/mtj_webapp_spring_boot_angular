package com.jwt.impl.rest;

import com.jwt.impl.core.persistance.entity.User;
import com.jwt.impl.core.service.AuthenticationService;
import com.jwt.impl.rest.payload.request.SignInRequest;
import com.jwt.impl.rest.payload.request.SignUpRequest;
import com.jwt.impl.rest.payload.response.SignInResponse;
import com.jwt.impl.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public AuthController(AuthenticationService userService, JwtService jwtService) {
        this.authenticationService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/api/v1/auth/sing-up")
    public ResponseEntity<Void> singUp(@RequestBody SignUpRequest singUpRequest) {
        authenticationService.userSingUp(singUpRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v1/auth/sign-in")
    public ResponseEntity<SignInResponse> authenticate(@RequestBody SignInRequest singInRequest) {
        User authenticatedUser = authenticationService.authenticate(singInRequest);
        String jwtToken = jwtService.generateToken(authenticatedUser.getEmail());
        SignInResponse signInResponse = new SignInResponse(jwtToken);
        return ResponseEntity.ok(signInResponse);
    }

}
