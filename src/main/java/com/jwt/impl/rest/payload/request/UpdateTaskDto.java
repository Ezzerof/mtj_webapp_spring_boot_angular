package com.jwt.impl.rest.payload.request;

import com.jwt.impl.core.persistance.entity.TaskStatus;

public record UpdateTaskDto(Integer taskId, String taskName, String description,
                            String startDateTime, String dueDateTime, TaskStatus status) {
}
