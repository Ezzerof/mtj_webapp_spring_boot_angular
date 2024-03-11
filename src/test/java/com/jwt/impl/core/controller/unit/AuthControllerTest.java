package com.jwt.impl.core.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.impl.core.persistance.entity.User;
import com.jwt.impl.core.service.AuthenticationService;
import com.jwt.impl.core.service.AuthenticationServiceImpl;
import com.jwt.impl.core.service.TaskReminderService;
import com.jwt.impl.rest.AuthController;
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
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService service;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private TaskReminderService taskReminderService;
    @Autowired
    private ObjectMapper objectMapper;
    private User user;
    private SignInRequest signInRequest;
    private SignUpRequest signUpRequest;
    private Authentication authentication;
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
        changePasswordRequest = new ChangePasswordRequest(
                "oldPassword",
                "newPassword",
                "admin@gmail.com"
        );
    }

    @Test
    public void AuthController_SignUp_ReturnUser() throws Exception {
        doNothing().when(service).userSignUp(signUpRequest);

        mockMvc.perform(post("/api/v1/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void AuthController_Authenticate_ReturnUser() throws Exception {
        String generatedToken = "token";
        when(service.authenticate(any(SignInRequest.class))).thenReturn(user);
        when(jwtService.generateToken(anyString())).thenReturn(generatedToken);

        mockMvc.perform(post("/api/v1/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.token").value(generatedToken));

    }

    @Test
    public void AuthController_DeleteAccount_AccountSuccessfullyDeleted() throws Exception {
        when(service.deleteAccount(authentication)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/auth/delete-account")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.message").value("Account successfully deleted."));
    }

    @Test
    public void AuthController_DeleteAccount_AccountNotFound() throws Exception {
        when(service.deleteAccount(authentication)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/auth/delete-account")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.error").value("Account not found."));
    }

    @Test
    public void AuthController_ChangePassword_200() throws Exception {
        when(service.changePassword(any(ChangePasswordRequest.class))).thenReturn(true);

        mockMvc.perform(post("/api/v1/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest)))
                        .andExpect(status().isOk());
    }

    @Test
    public void AuthController_ChangePassword_400() throws Exception {
        when(service.changePassword(any(ChangePasswordRequest.class))).thenReturn(false);

        mockMvc.perform(post("/api/v1/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest)))
                        .andExpect(status().isBadRequest());
    }
}
