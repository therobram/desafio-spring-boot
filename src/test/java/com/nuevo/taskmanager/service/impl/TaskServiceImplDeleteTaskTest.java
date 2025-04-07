package com.nuevo.taskmanager.service.impl;

import com.nuevo.taskmanager.entity.Task;
import com.nuevo.taskmanager.entity.User;
import com.nuevo.taskmanager.mapper.TaskMapper;
import com.nuevo.taskmanager.repository.TaskRepository;
import com.nuevo.taskmanager.repository.TaskStatusRepository;
import com.nuevo.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceImplDeleteTaskTest {

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
    void shouldDeleteTaskSuccessfully() {
        // Arrange
        Task task = new Task();
        task.setId(1L);

        User user = new User();
        user.setUsername("john");

        task.setUser(user);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        taskService.deleteTask(1L, "john");

        // Assert
        verify(taskRepository).delete(task);
    }

    @Test
    void shouldThrowIfTaskNotFound() {
        // Arrange
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                taskService.deleteTask(99L, "john")
        );
        assertEquals("Tarea no encontrada", exception.getMessage());
    }

    @Test
    void shouldThrowIfUserIsNotOwner() {
        // Arrange
        Task task = new Task();
        User owner = new User();
        owner.setUsername("john");
        task.setUser(owner);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                taskService.deleteTask(1L, "not-john")
        );
        assertEquals("No autorizado para eliminar esta tarea", exception.getMessage());

        verify(taskRepository, never()).delete(any());
    }
}
