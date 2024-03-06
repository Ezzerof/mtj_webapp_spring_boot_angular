package com.jwt.impl.core.repository;

import com.jwt.impl.core.persistance.entity.Task;
import com.jwt.impl.core.persistance.entity.TaskStatus;
import com.jwt.impl.core.persistance.entity.User;
import com.jwt.impl.core.persistance.repository.TaskRepository;
import com.jwt.impl.core.persistance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TaskRepositoryTest {
    @Autowired
    private TaskRepository repository;
    @Autowired
    private UserRepository userRepository;
    private Task task1;
    private User user;
    private User invalidUser;

    @BeforeEach
    public void init() {
        user = new User(
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
        invalidUser = new User(
                "Username2",
                "Adrian",
                "",
                "Surname",
                23,
                "email2@gmail.com",
                "passworD123!",
                "Course",
                "1234567898"
        );
        userRepository.save(user);
        userRepository.save(invalidUser);
        task1 = new Task(
                1,
                "taskName",
                "description",
                LocalDateTime.now(),
                LocalDateTime.now(),
                TaskStatus.TO_DO,
                user
        );
    }

    @Test
    public void findAllTasks_validUser_listOfTasks() {
        repository.save(task1);

        List<Task> usersTasks = repository.findAllTasksByAssignee(user);

        String expectedTaskName = "taskName";
        String actualTaskName = usersTasks.get(0).getTaskName();

        assertNotNull(usersTasks);
        assertEquals(1, usersTasks.size());
        assertEquals(actualTaskName, expectedTaskName);
    }

    @Test
    public void findAllTasks_invalidUser_emptyList() {
        repository.save(task1);

        List<Task> usersTasks = repository.findAllTasksByAssignee(invalidUser);

        assertTrue(usersTasks.isEmpty());
    }

    @Test
    public void deleteTask_validTaskId() {
        repository.save(task1);

        repository.deleteByTaskId(task1.getTaskId());

        List<Task> usersTasks = repository.findAllTasksByAssignee(user);

        assertTrue(usersTasks.isEmpty());
    }

    @Test
    public void deleteTask_invalidTaskId() {
        repository.save(task1);

        repository.deleteByTaskId(task1.getTaskId()+1);

        List<Task> usersTasks = repository.findAllTasksByAssignee(user);

        assertFalse(usersTasks.isEmpty());
    }
}
