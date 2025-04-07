package com.nuevo.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TaskRequestDTO {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título no puede superar los 100 caracteres")
    private String title;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String description;

    @NotNull(message = "El ID del estado es obligatorio")
    private Long statusId;

    // @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    // ✅ Constructor vacío (necesario para frameworks como Spring)
    public TaskRequestDTO() {
    }
    
    public TaskRequestDTO(String title, String description, Long statusId) {
        this.title = title;
        this.description = description;
        this.statusId = statusId;
    }

    // ✅ Constructor completo (útil para los tests)
    public TaskRequestDTO(String title, String description, Long statusId, Long userId) {
        this.title = title;
        this.description = description;
        this.statusId = statusId;
        this.userId = userId;
    }

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

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
