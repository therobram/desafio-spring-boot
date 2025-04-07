package com.nuevo.taskmanager.service;

import com.nuevo.taskmanager.dto.TaskRequestDTO;
import com.nuevo.taskmanager.dto.TaskResponseDTO;
import java.util.List;

public interface TaskService {
    TaskResponseDTO createTask(TaskRequestDTO dto, String username);
    TaskResponseDTO getTaskById(Long id);
    List<TaskResponseDTO> getAllTasks(String username);
    TaskResponseDTO updateTask(Long id, TaskRequestDTO dto, String username);
    void deleteTask(Long id, String username);
}
