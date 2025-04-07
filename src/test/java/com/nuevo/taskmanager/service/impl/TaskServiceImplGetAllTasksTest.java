package com.nuevo.taskmanager.service.impl;

import com.nuevo.taskmanager.dto.TaskResponseDTO;
import com.nuevo.taskmanager.entity.Task;
import com.nuevo.taskmanager.entity.TaskStatus;
import com.nuevo.taskmanager.entity.User;
import com.nuevo.taskmanager.mapper.TaskMapper;
import com.nuevo.taskmanager.repository.TaskRepository;
import com.nuevo.taskmanager.repository.TaskStatusRepository;
import com.nuevo.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy; // ✅ IMPORTANTE
import static org.mockito.Mockito.*;

class TaskServiceImplGetAllTasksTest {

    private TaskServiceImpl taskService;
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private TaskStatusRepository statusRepository;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        userRepository = mock(UserRepository.class);
        statusRepository = mock(TaskStatusRepository.class);
        taskService = new TaskServiceImpl(taskRepository, userRepository, statusRepository, new TaskMapper());
    }

    @Test
    void shouldReturnTasksForUser() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("john");

        TaskStatus status = new TaskStatus();
        status.setId(1L);
        status.setName("PENDING");

        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Tarea 1");
        task1.setDescription("Desc 1");
        task1.setUser(user);
        task1.setStatus(status);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Tarea 2");
        task2.setDescription("Desc 2");
        task2.setUser(user);
        task2.setStatus(status);

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(taskRepository.findByUserId(1L)).thenReturn(Arrays.asList(task1, task2));

        // Act
        List<TaskResponseDTO> tasks = taskService.getAllTasks("john");

        // Assert
        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Tarea 1");
        assertThat(tasks.get(1).getTitle()).isEqualTo("Tarea 2");
        verify(userRepository).findByUsername("john");
        verify(taskRepository).findByUserId(1L);
    }

    @Test
    void shouldThrowIfUserNotFound() {
        // Arrange
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> taskService.getAllTasks("unknown")) // ✅ CORREGIDO
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuario no encontrado");

        verify(userRepository).findByUsername("unknown");
        verifyNoInteractions(taskRepository);
    }
}
