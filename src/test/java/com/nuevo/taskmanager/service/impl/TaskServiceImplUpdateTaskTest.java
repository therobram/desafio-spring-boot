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
import com.nuevo.taskmanager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TaskServiceImplUpdateTaskTest {

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
    void shouldUpdateTaskSuccessfully() {
        // Arrange
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Old title");
        existingTask.setDescription("Old desc");

        User user = new User();
        user.setUsername("john");
        existingTask.setUser(user);

        TaskStatus oldStatus = new TaskStatus();
        oldStatus.setId(1L);
        oldStatus.setName("PENDING");

        TaskStatus newStatus = new TaskStatus();
        newStatus.setId(2L);
        newStatus.setName("IN_PROGRESS");

        existingTask.setStatus(oldStatus);

        TaskRequestDTO dto = new TaskRequestDTO("New title", "New desc", 2L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(statusRepository.findById(2L)).thenReturn(Optional.of(newStatus));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        TaskResponseDTO updated = taskService.updateTask(1L, dto, "john");

        // Assert
        assertThat(updated.getTitle()).isEqualTo("New title");
        assertThat(updated.getDescription()).isEqualTo("New desc");
        assertThat(updated.getStatus()).isEqualTo("IN_PROGRESS");
        verify(taskRepository).save(existingTask);
    }

    @Test
    void shouldThrowIfUserIsNotOwner() {
        Task task = new Task();
        User owner = new User();
        owner.setUsername("john");
        task.setUser(owner);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskRequestDTO dto = new TaskRequestDTO("Test", "Test", 1L);

        assertThrows(RuntimeException.class, () ->
            taskService.updateTask(1L, dto, "not-john")
        );
    }
}
