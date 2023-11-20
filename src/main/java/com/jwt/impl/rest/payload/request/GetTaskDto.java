package com.jwt.impl.rest.payload.request;

public record GetTaskDto(Integer taskId, String taskName, String description, String startDateTime, String dueDateTime, String status) {
}
