package com.jwt.impl.rest.payload.request;

public record CreateTaskDto(String taskName, String description, String dueDateTime, String startDateTime, String status) {

}
