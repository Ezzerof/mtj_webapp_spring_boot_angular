package com.jwt.impl.core.service;

import com.jwt.impl.core.exceptions.EmailAlreadyExistException;
import com.jwt.impl.core.exceptions.UserNotFoundException;
import com.jwt.impl.core.exceptions.UsernameAlreadyExistException;
import com.jwt.impl.core.persistance.entity.User;
import com.jwt.impl.core.persistance.repository.UserRepository;
import com.jwt.impl.rest.payload.request.SignInRequest;
import com.jwt.impl.rest.payload.request.SignUpRequest;
import com.jwt.impl.utils.RegisterValidation;
import com.jwt.impl.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
    private SignUpRequest signUpRequest;
    private SignUpRequest signUpRequestWithoutMidName;
    private SignInRequest signInRequest;
    private User userWithMidName;
    private User userWithoutMidName;

    @BeforeEach
    public void init() {
        this.userWithMidName = new User(
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
        this.userWithMidName = new User(
                "Username",
                "Adrian",
                "",
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
        signUpRequestWithoutMidName = new SignUpRequest(
                "Username",
                "Adrian",
                "",
                "Surname",
                23,
                "email@gmail.com",
                "passworD123!",
                "Course",
                "1234567898"
        );
    }

    @Test
    public void testUserSignUp_WithMidName_Successful() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userWithMidName);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        service.userSignUp(signUpRequest);

        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    public void testUserSignUp_WithoutMidName_Successful() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userWithoutMidName);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        service.userSignUp(signUpRequestWithoutMidName);

        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    public void testUserSignUp_ThrowsUsernameAlreadyExistException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userWithMidName));

        assertThrows(UsernameAlreadyExistException.class, () -> {
            service.userSignUp(signUpRequest);
        });

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    public void testUserSignUp_ThrowsEmailAlreadyExistException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userWithMidName));

        assertThrows(EmailAlreadyExistException.class, () -> {
            service.userSignUp(signUpRequest);
        });

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testAuthenticate() {
        Authentication authentication = mock(Authentication.class);
        UserPrincipal principal = new UserPrincipal(
                userWithMidName.getId(),
                userWithMidName.getUsername(),
                userWithMidName.getEmail(),
                userWithMidName.getPassword());

        when(authentication.getPrincipal()).thenReturn(principal);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userWithMidName));

        User authenticatedUser = service.authenticate(signInRequest);
        String expectedAuthenticatedUserUsername = userWithMidName.getUsername();
        String actualAuthUsername = authenticatedUser.getUsername();

        verify(userRepository, times(1)).findByEmail(anyString());
        assertNotNull(authenticatedUser);
        assertEquals(expectedAuthenticatedUserUsername, actualAuthUsername);
    }

    @Test
    public void testAuthenticate_invalidData() {
        Authentication authentication = mock(Authentication.class);
        UserPrincipal principal = new UserPrincipal(
                userWithMidName.getId(),
                userWithMidName.getUsername(),
                userWithMidName.getEmail(),
                userWithMidName.getPassword());

        when(authentication.getPrincipal()).thenReturn(principal);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.authenticate(signInRequest));

        verify(userRepository, times(1)).findByEmail(anyString());
    }
}
