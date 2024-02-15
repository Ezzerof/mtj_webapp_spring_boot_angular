package com.jwt.impl.core.persistance.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer taskId;
    private String taskName;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime dueDateTime;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User assignee;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User user) {
        this.assignee = user;
    }

    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(LocalDateTime dueDate) {
        this.dueDateTime = dueDate;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDate) {
        this.startDateTime = startDate;
    }
}
