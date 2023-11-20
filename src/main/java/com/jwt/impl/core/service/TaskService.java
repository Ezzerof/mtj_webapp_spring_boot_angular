package com.jwt.impl.core.service;

import com.jwt.impl.core.persistance.entity.Task;
import com.jwt.impl.rest.payload.request.CreateTaskDto;
import com.jwt.impl.rest.payload.request.GetTaskDto;
import com.jwt.impl.rest.payload.request.UpdateTaskDto;

import java.util.List;

public interface TaskService {

    List<GetTaskDto> getAllTasksFromUser();

    void createTask(CreateTaskDto createTaskDto);

    void updateTask(UpdateTaskDto updateTaskDto);

    void deleteTask(Integer taskId);
}
