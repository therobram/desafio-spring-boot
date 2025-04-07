package com.nuevo.taskmanager.service.impl;

import com.nuevo.taskmanager.dto.TaskRequestDTO;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private TaskStatusRepository statusRepository;
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        userRepository = mock(UserRepository.class);
        statusRepository = mock(TaskStatusRepository.class);
        taskService = new TaskServiceImpl(taskRepository, userRepository, statusRepository, new TaskMapper());

    }

    @Test
    void shouldCreateTaskSuccessfully() {
        // Arrange
        String username = "testuser";

        TaskRequestDTO request = new TaskRequestDTO();
        request.setTitle("Nueva tarea");
        request.setDescription("Descripción");
        request.setStatusId(1L);

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername(username);

        TaskStatus mockStatus = new TaskStatus();
        mockStatus.setId(1L);
        mockStatus.setName("PENDING");

        Task mockSavedTask = new Task();
        mockSavedTask.setId(100L);
        mockSavedTask.setTitle(request.getTitle());
        mockSavedTask.setDescription(request.getDescription());
        mockSavedTask.setStatus(mockStatus);
        mockSavedTask.setUser(mockUser);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(statusRepository.findById(1L)).thenReturn(Optional.of(mockStatus));
        when(taskRepository.save(any(Task.class))).thenReturn(mockSavedTask);

        // Act
        TaskResponseDTO response = taskService.createTask(request, username);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getTitle()).isEqualTo("Nueva tarea");
        assertThat(response.getStatus()).isEqualTo("PENDING");
        assertThat(response.getUsername()).isEqualTo("testuser");

        verify(taskRepository, times(1)).save(any(Task.class));
    }
}
