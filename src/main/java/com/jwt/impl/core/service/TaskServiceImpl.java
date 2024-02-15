package com.jwt.impl.core.service;

import com.jwt.impl.core.exceptions.InvalidDateTimeException;
import com.jwt.impl.core.exceptions.InvalidTaskStatusException;
import com.jwt.impl.core.persistance.entity.Task;
import com.jwt.impl.core.persistance.entity.TaskStatus;
import com.jwt.impl.core.persistance.entity.User;
import com.jwt.impl.core.persistance.repository.TaskRepository;
import com.jwt.impl.core.persistance.repository.UserRepository;
import com.jwt.impl.exceptions.InvalidTaskDescriptionException;
import com.jwt.impl.exceptions.NonexistentTaskException;
import com.jwt.impl.rest.payload.request.CreateTaskDto;
import com.jwt.impl.rest.payload.request.GetTaskDto;
import com.jwt.impl.rest.payload.request.UpdateTaskDto;
import com.jwt.impl.security.UserPrincipal;
import com.jwt.impl.utils.TaskValidation;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<GetTaskDto> getAllUserTasks() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(userPrincipal.getId())
                .map(user -> taskRepository.findAllTasksByAssignee(user)
                        .stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Override
    public List<GetTaskDto> getAllUserSortedTasks(String sortProperty, String sortDirection) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortProperty);
        return userRepository.findById(userPrincipal.getId())
                .map(user -> taskRepository.findAllTasksByAssignee(user, sort)
                        .stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Override
    @Transactional
    public void createTask(CreateTaskDto createTaskDto) {

        if (TaskValidation.isDescriptionValid(createTaskDto.description())) {
            Task task = new Task();
            task.setTaskName(createTaskDto.taskName());
            task.setDescription(createTaskDto.description());
            task.setStatus(convertTaskStatus(createTaskDto.status()));
            task.setStartDateTime(convertToLocalDate(createTaskDto.startDateTime()));
            task.setDueDateTime(convertToLocalDate(createTaskDto.dueDateTime()));
            UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            task.setAssignee(user);
            taskRepository.save(task);
        } else
            throw new InvalidTaskDescriptionException("Task description cannot be null");
    }

    @Override
    @Transactional
    public void updateTask(UpdateTaskDto updateTaskDto) {
        Task targetTask = taskRepository.findById(updateTaskDto.taskId())
                .orElseThrow(() -> new NonexistentTaskException("Task doesn't exist"));

        if (!TaskValidation.isDescriptionValid(updateTaskDto.description())) {
            throw new InvalidTaskDescriptionException("Task description cannot be null or invalid");
        }

        targetTask.setTaskName(updateTaskDto.taskName());
        targetTask.setDescription(updateTaskDto.description());
        targetTask.setStatus(updateTaskDto.status());
        targetTask.setDueDateTime(convertToLocalDate(updateTaskDto.dueDateTime()));
        targetTask.setStartDateTime(convertToLocalDate(updateTaskDto.startDateTime()));

        taskRepository.save(targetTask);
    }

    @Override
    @Transactional
    public void deleteTask(Integer taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task != null)
            taskRepository.delete(task);
        else
            throw new NonexistentTaskException("Task doesn't exist");
    }

    private LocalDateTime convertToLocalDate(String dateTimeString) {
        System.out.println(dateTimeString);
        if (dateTimeString == null || dateTimeString.isBlank()) {
            throw new IllegalArgumentException("Date string cannot be null or blank");
        }
        try {
            return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new InvalidDateTimeException("Invalid date format: " + dateTimeString);
        }
    }

    private static TaskStatus convertTaskStatus(String status) {
        if (status == null) {
            throw new InvalidTaskStatusException("Task status is null");
        }
        try {
            return TaskStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidTaskStatusException("Invalid task status: " + status);
        }
    }

    private GetTaskDto convertToDto(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String formattedStartDateTime = (task.getStartDateTime() != null) ? task.getStartDateTime().format(formatter) : null;
        String formattedDueDateTime = (task.getDueDateTime() != null) ? task.getDueDateTime().format(formatter): null;

        return new GetTaskDto(
                task.getTaskId(),
                task.getTaskName(),
                task.getDescription(),
                formattedStartDateTime,
                formattedDueDateTime,
                task.getStatus().name()
        );
    }
}
