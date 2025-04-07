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
import com.nuevo.taskmanager.service.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskStatusRepository statusRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository,
                           UserRepository userRepository,
                           TaskStatusRepository statusRepository,
                           TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskResponseDTO createTask(TaskRequestDTO dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        TaskStatus status = statusRepository.findById(dto.getStatusId())
                .orElseThrow(() -> new RuntimeException("Estado no válido"));

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setUser(user);
        task.setStatus(status);

        return taskMapper.toDTO(taskRepository.save(task));
    }

    @Override
    public TaskResponseDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        return taskMapper.toDTO(task);
    }

    @Override
    public List<TaskResponseDTO> getAllTasks(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return taskRepository.findByUserId(user.getId())
                .stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO dto, String username) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        if (!task.getUser().getUsername().equals(username)) {
            throw new RuntimeException("No autorizado para modificar esta tarea");
        }

        TaskStatus status = statusRepository.findById(dto.getStatusId())
                .orElseThrow(() -> new RuntimeException("Estado no válido"));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(status);

        return taskMapper.toDTO(taskRepository.save(task));
    }

    @Override
    public void deleteTask(Long id, String username) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        if (!task.getUser().getUsername().equals(username)) {
            throw new RuntimeException("No autorizado para eliminar esta tarea");
        }

        taskRepository.delete(task);
    }
}
