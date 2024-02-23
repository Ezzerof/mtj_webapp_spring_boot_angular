package com.jwt.impl.core.repository;

import com.jwt.impl.core.persistance.entity.User;
import com.jwt.impl.core.persistance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
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
                "1234567898");
        this.userWithoutMidName = new User(
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
    public void saveUser_withMidName_ReturnUser() {
        User savedUser = userRepository.save(userWithMidName);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void saveUser_withoutMidName_ReturnUser() {
        User savedUser = userRepository.save(userWithoutMidName);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getMiddleName()).isEqualTo("");
    }

    @Test
    public void getAllUsers_ReturnUser() {
        userRepository.save(userWithMidName);
        userRepository.save(userWithoutMidName);

        List<User> retrievedUsers = userRepository.findAll();

        assertThat(retrievedUsers.size()).isEqualTo(2);
    }

    @Test
    public void getUserByUsername_ReturnUser() {
        userRepository.save(userWithMidName);
        User retrievedUser = userRepository.findByUsername(userWithMidName.getUsername()).get();

        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getUsername()).isEqualTo(userWithMidName.getUsername());
    }

    @Test
    public void getUserByEmail_ReturnUser() {
        userRepository.save(userWithMidName);
        User retrievedUser = userRepository.findByEmail(userWithMidName.getEmail()).get();

        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getEmail()).isEqualTo(userWithMidName.getEmail());
    }

    @Test
    public void deleteUser() {
        userRepository.save(userWithMidName);

        User deletionUser = userRepository.findByUsername(userWithMidName.getUsername()).get();

        userRepository.delete(deletionUser);

        assertThat(userRepository.findByUsername(userWithMidName.getUsername())).isEmpty();
    }

    @Test
    public void updateUser() {
        userRepository.save(userWithMidName);

        userWithMidName.setUsername("Admin");

        User updatedUser = userRepository.save(userWithMidName);
        String oldUsername = userWithMidName.getUsername();
        String updatedUsername = updatedUser.getUsername();

        assertThat(userRepository.findAll().size()).isEqualTo(1);
        assertThat(oldUsername).isEqualTo(updatedUsername);
    }
}
