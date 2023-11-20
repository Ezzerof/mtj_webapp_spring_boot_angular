package com.jwt.impl.core.service;

import com.jwt.impl.core.persistance.entity.User;
import com.jwt.impl.rest.payload.request.SignInRequest;
import com.jwt.impl.rest.payload.request.SignUpRequest;

public interface AuthenticationService {

    void userSingUp(SignUpRequest signUpRequest);

    User authenticate(SignInRequest input);
}
