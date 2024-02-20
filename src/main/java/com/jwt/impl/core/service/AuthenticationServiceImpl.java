package com.jwt.impl.core.service;


import com.jwt.impl.core.exceptions.EmailAlreadyExistException;
import com.jwt.impl.core.exceptions.UsernameAlreadyExistException;
import com.jwt.impl.core.persistance.entity.User;
import com.jwt.impl.core.persistance.repository.UserRepository;
import com.jwt.impl.rest.payload.request.SignInRequest;
import com.jwt.impl.rest.payload.request.SignUpRequest;
import com.jwt.impl.utils.RegisterValidation;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RegisterValidation registerValidation;
//    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.registerValidation = new RegisterValidation();
    }

    @Override
    public void userSingUp(SignUpRequest singUpRequest) {
        User user = new User();

        if (!isUsernameAvailable(singUpRequest.username()))
            throw new UsernameAlreadyExistException("The username already exists.");

        if (!isEmailAvailable(singUpRequest.email()))
            throw new EmailAlreadyExistException("Email address already exists.");

        if (registerValidation.validateUser(singUpRequest)) {
            user.setEmail(singUpRequest.email());
            user.setUsername(singUpRequest.username());
            user.setFirstName(singUpRequest.firstName());
            user.setMiddleName(singUpRequest.middleName());
            user.setLastName(singUpRequest.lastName());
            user.setAge(singUpRequest.age());
            user.setCourseName(singUpRequest.courseName());
            user.setPhoneNumber(singUpRequest.phoneNumber());
            user.setPicture(singUpRequest.picture());
            user.setPassword(passwordEncoder.encode(singUpRequest.password()));
            userRepository.save(user);
        }
    }

    public User authenticate(SignInRequest input) {
        User user;

        if (isUsernameAvailable(input.username()))
            throw new UsernameAlreadyExistException("The username doesn't exists.");

        user = userRepository.findByUsername(input.username()).get();

        if (!registerValidation.isPasswordValid(input.password())) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.username(),
                            input.password()
                    )
            );
        }
        return user;
    }

    @Override
    public Boolean changePassword(String username, String currentPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent())
            return false;

        User user = userOpt.get();

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;
        }

        if (!registerValidation.isPasswordValid(newPassword)) {
            return false;
        }

        String hashedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedNewPassword);
        userRepository.save(user);

        return true;
    }

    @Override
    public boolean deleteAccount(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }
        return false;
    }

    private boolean isUsernameAvailable(String username) {
        return !userRepository.findByUsername(username).isPresent();
    }

    private boolean isEmailAvailable(String email) {
        return !userRepository.findByEmail(email).isPresent();
    }

}
