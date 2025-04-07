package com.nuevo.taskmanager.repository;

import com.nuevo.taskmanager.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    TaskStatus findByName(String name);
}
