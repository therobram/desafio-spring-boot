package com.nuevo.taskmanager.mapper;

import com.nuevo.taskmanager.dto.TaskResponseDTO;
import com.nuevo.taskmanager.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponseDTO toDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().getName(),
                task.getUser().getUsername()
        );
    }
}
