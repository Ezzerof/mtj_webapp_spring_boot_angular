package com.jwt.impl.rest;

import com.jwt.impl.core.persistance.entity.Task;
import com.jwt.impl.core.service.TaskService;
import com.jwt.impl.rest.payload.request.CreateTaskDto;
import com.jwt.impl.rest.payload.request.GetTaskDto;
import com.jwt.impl.rest.payload.request.UpdateTaskDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/api/v1/tasks")
    public ResponseEntity<List<GetTaskDto>> getAllTasksForUser() {
        List<GetTaskDto> userTasks = taskService.getAllTasksFromUser();
        return ResponseEntity.ok(userTasks);
    }
    @PostMapping("/api/v1/tasks")
    public ResponseEntity<Task> addTask(@RequestBody CreateTaskDto createTaskDto) {
        taskService.createTask(createTaskDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/api/v1/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Integer taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/api/v1/tasks/{taskId}")
    public ResponseEntity<?> modifyTask(@RequestBody UpdateTaskDto updateTaskDto) {
        taskService.updateTask(updateTaskDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
