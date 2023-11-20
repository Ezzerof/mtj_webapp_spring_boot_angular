package com.jwt.impl.core.persistance.repository;

import com.jwt.impl.core.persistance.entity.Task;
import com.jwt.impl.core.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAllTasksByAssignee(User user);

    void deleteByTaskId(Integer taskId);

    Task findTaskByAssignee(User user);

}
