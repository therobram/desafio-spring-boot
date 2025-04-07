package com.nuevo.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;

public class TaskCreateRequest {

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String status;

    // Getters y Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
