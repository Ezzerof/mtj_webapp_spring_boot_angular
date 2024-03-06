package com.jwt.impl.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.impl.core.persistance.entity.User;
import com.jwt.impl.core.service.AuthenticationServiceImpl;
import com.jwt.impl.rest.payload.request.ChangePasswordRequest;
import com.jwt.impl.rest.payload.request.SignInRequest;
import com.jwt.impl.rest.payload.request.SignUpRequest;
import com.jwt.impl.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = AuthControllerTest.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationServiceImpl service;
    @MockBean
    private JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private SignInRequest signInRequest;
    private SignUpRequest signUpRequest;
    private ChangePasswordRequest changePasswordRequest;

    @BeforeEach
    public void init() {
        this.user = new User(
                "Username",
                "Adrian",
                "Dan",
                "Surname",
                23,
                "email@gmail.com",
                "passworD123!",
                "Course",
                "1234567898"
        );
        signInRequest = new SignInRequest("email@gmail.com", "passworD123!");
        signUpRequest = new SignUpRequest(
                "Username",
                "Adrian",
                "Dan",
                "Surname",
                23,
                "email@gmail.com",
                "passworD123!",
                "Course",
                "1234567898"
        );
    }

    @Test
    public void AuthController_SignUp_ReturnUser() throws Exception {
        doNothing().when(service).userSignUp(signUpRequest);

        mockMvc.perform(post("/api/v1/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void AuthController_Authenticate_ReturnUser() throws Exception {
        when(service.authenticate(any(SignInRequest.class))).thenReturn(user);
        when(jwtService.generateToken(anyString())).thenReturn("token");

        ResultActions response = mockMvc.perform(post("/api/v1/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInRequest)));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
