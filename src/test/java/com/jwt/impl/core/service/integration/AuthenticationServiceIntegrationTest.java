package com.jwt.impl.core.service.integration;

import com.jwt.impl.core.persistance.entity.User;
import com.jwt.impl.core.persistance.repository.UserRepository;
import com.jwt.impl.core.service.AuthenticationService;
import com.jwt.impl.rest.payload.request.ChangePasswordRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthenticationServiceIntegrationTest {

    @Autowired
    private UserRepository repository;
    @Autowired
    private AuthenticationService service;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private User testUser;
    private ChangePasswordRequest changePasswordRequest;

    @BeforeEach
    void setUp() {
        testUser = new User(
                "UsernameTest",
                "Adrian",
                "Dan",
                "Surname",
                23,
                "testEmail@gmail.com",
                "passworD123!",
                "Course",
                "1234567898"
        );

        testUser.setPassword(passwordEncoder.encode("passworD123!"));

        repository.save(testUser);

        changePasswordRequest = new ChangePasswordRequest(
                "passworD123!",
                "testPass123!",
                "testEmail@gmail.com"
        );
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    @Transactional
    public void whenChangePassword_ThenReturnTrue() {
        assertTrue(repository.findByEmail(testUser.getEmail()).isPresent());

        assertTrue(service.changePassword(changePasswordRequest));

        Optional<User> updatedUserOpt = repository.findByEmail("testEmail@gmail.com");
        assertTrue(updatedUserOpt.isPresent());
        assertTrue(passwordEncoder.matches("testPass123!", updatedUserOpt.get().getPassword()));
    }
}
