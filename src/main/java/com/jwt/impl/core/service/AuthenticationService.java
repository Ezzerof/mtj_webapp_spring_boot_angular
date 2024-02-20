package com.jwt.impl.core.service;

import com.jwt.impl.core.persistance.entity.User;
import com.jwt.impl.rest.payload.request.DeleteAccountRequest;
import com.jwt.impl.rest.payload.request.SignInRequest;
import com.jwt.impl.rest.payload.request.SignUpRequest;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {

    void userSingUp(SignUpRequest signUpRequest);

    User authenticate(SignInRequest input);
    Boolean changePassword(String username, String currentPassword, String newPassword);
    boolean deleteAccount(Authentication authentication);
}
