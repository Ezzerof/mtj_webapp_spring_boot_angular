package com.jwt.impl.core.service;

import com.jwt.impl.core.persistance.entity.User;
import com.jwt.impl.core.persistance.repository.UserRepository;
import com.jwt.impl.rest.payload.request.SignUpRequest;
import com.jwt.impl.utils.RegisterValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private RegisterValidation registerValidation;
    @InjectMocks
    private AuthenticationServiceImpl service;


    @Test
    public void testUserSignUp_WithMiddleName_Successful() {
        SignUpRequest request = new SignUpRequest(
                "testUsername",
                "Dan",
                "Andrew",
                "Williams",
                23,
                "testEmail@gmail.com",
                "Admin123!",
                "Cloud Computing",
                "1234567899"
        );

        User user = new User();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        service.userSingUp(request);
        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
    }
}
