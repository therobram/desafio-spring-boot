package com.nuevo.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuevo.taskmanager.dto.TaskRequestDTO;
import com.nuevo.taskmanager.dto.TaskResponseDTO;
import com.nuevo.taskmanager.exception.GlobalExceptionHandler;
import com.nuevo.taskmanager.exception.NotFoundException;
import com.nuevo.taskmanager.exception.UnauthorizedException;
import com.nuevo.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private TaskResponseDTO sampleResponse;
    private Authentication mockAuth;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(new GlobalExceptionHandler()) // 👈 importantísimo
                .build();
        objectMapper = new ObjectMapper();

        sampleResponse = new TaskResponseDTO(1L, "Tarea", "Descripción", "PENDING", "testuser");
        mockAuth = new TestingAuthenticationToken("testuser", null);
    }

    @Test
    void shouldReturnListOfTasks() throws Exception {
        List<TaskResponseDTO> tasks = Arrays.asList(
                sampleResponse,
                new TaskResponseDTO(2L, "Otra tarea", "Otra descripción", "IN_PROGRESS", "testuser")
        );

        when(taskService.getAllTasks("testuser")).thenReturn(tasks);

        mockMvc.perform(get("/tasks").principal(mockAuth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].title", is("Tarea")));
    }

    @Test
    void shouldReturnTaskById() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Tarea"));
    }

    @Test
    void shouldCreateTask() throws Exception {
        TaskRequestDTO request = new TaskRequestDTO("Nueva tarea", "Descripción", 1L);

        when(taskService.createTask(any(), eq("testuser"))).thenReturn(sampleResponse);

        mockMvc.perform(post("/tasks")
                        .principal(mockAuth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Tarea")));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        TaskRequestDTO updateRequest = new TaskRequestDTO("Editada", "Editada desc", 2L);
        TaskResponseDTO updated = new TaskResponseDTO(1L, "Editada", "Editada desc", "IN_PROGRESS", "testuser");

        when(taskService.updateTask(eq(1L), any(), eq("testuser"))).thenReturn(updated);

        mockMvc.perform(put("/tasks/1")
                        .principal(mockAuth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Editada")));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(1L, "testuser");

        mockMvc.perform(delete("/tasks/1").principal(mockAuth))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404IfTaskNotFound() throws Exception {
        when(taskService.getTaskById(999L)).thenThrow(new NotFoundException("Tarea no encontrada"));

        mockMvc.perform(get("/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Tarea no encontrada")));
    }

    @Test
    void shouldReturn403IfUserNotAuthorizedToUpdate() throws Exception {
        TaskRequestDTO updateRequest = new TaskRequestDTO("Editada", "Editada desc", 1L);
        doThrow(new UnauthorizedException("No autorizado para modificar esta tarea"))
                .when(taskService).updateTask(eq(1L), any(), eq("testuser"));

        mockMvc.perform(put("/tasks/1")
                        .principal(mockAuth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message", is("No autorizado para modificar esta tarea")));
    }

    @Test
    void shouldReturn400IfInvalidInput() throws Exception {
        TaskRequestDTO invalidRequest = new TaskRequestDTO();

        mockMvc.perform(post("/tasks")
                        .principal(mockAuth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[*].defaultMessage",
                        hasItem("El título es obligatorio")))
                .andExpect(jsonPath("$.errors[*].defaultMessage",
                        hasItem("El ID del estado es obligatorio")));
    }
}
