package com.jwt.impl.core.repository;

import com.jwt.impl.core.persistance.entity.Task;
import com.jwt.impl.core.persistance.entity.User;
import com.jwt.impl.core.persistance.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveUser_ReturnUser() {
//        User user = new User(
//                "Username",
//                "Adrian",
//                "Dan",
//                "Surname",
//                23,
//                "email@gmail.com",
//                "passworD123!",
//                "Course",
//                "1234567898"
//        );
    }
}
