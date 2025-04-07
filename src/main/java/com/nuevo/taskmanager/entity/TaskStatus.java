package com.nuevo.taskmanager.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ESTADOS_TAREA")
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Constructor vacío requerido por JPA
    public TaskStatus() {
    }

    // Constructor personalizado para facilitar la creación
    public TaskStatus(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
