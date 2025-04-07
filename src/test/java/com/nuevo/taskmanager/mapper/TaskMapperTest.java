package com.nuevo.taskmanager.mapper;

import com.nuevo.taskmanager.dto.TaskResponseDTO;
import com.nuevo.taskmanager.entity.Task;
import com.nuevo.taskmanager.entity.TaskStatus;
import com.nuevo.taskmanager.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskMapperTest {

    private TaskMapper taskMapper;

    @BeforeEach
    void setUp() {
        taskMapper = new TaskMapper(); // No necesita inyección, es simple
    }

    @Test
    void shouldMapTaskToTaskResponseDTO() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("johndoe");

        TaskStatus status = new TaskStatus();
        status.setId(1L);
        status.setName("PENDING");

        Task task = new Task();
        task.setId(100L);
        task.setTitle("Tarea de prueba");
        task.setDescription("Descripción de la tarea");
        task.setUser(user);
        task.setStatus(status);

        // Act
        TaskResponseDTO dto = taskMapper.toDTO(task);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(100L);
        assertThat(dto.getTitle()).isEqualTo("Tarea de prueba");
        assertThat(dto.getDescription()).isEqualTo("Descripción de la tarea");
        assertThat(dto.getUsername()).isEqualTo("johndoe");
        assertThat(dto.getStatus()).isEqualTo("PENDING");
    }
}
