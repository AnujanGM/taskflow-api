package com.taskflow.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.taskflow.api.dto.request.TaskRequestDTO;
import com.taskflow.api.dto.response.TaskResponseDTO;
import com.taskflow.api.model.Task;
import com.taskflow.api.repository.TaskRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor

public class TaskController {
    private final TaskRepository taskRepository;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@Valid @RequestBody TaskRequestDTO taskRequestDTO) {
        Task task = new Task();
        task.setTitle(taskRequestDTO.getTitle());
        task.setDescription(taskRequestDTO.getDescription());
        Task savedTask = taskRepository.save(task);
    
        TaskResponseDTO taskResponseDTO = TaskResponseDTO.builder()  // ← NO "new" HERE!
            .id(savedTask.getId())
            .title(savedTask.getTitle())
            .description(savedTask.getDescription())
            .completed(savedTask.isCompleted())
            .build();
    
        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAll() {
        List<TaskResponseDTO> tasks = taskRepository.findAll()
                                         .stream()
                                         .map(this::toResponseDto)
                                         .toList();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getById(@PathVariable Long id) {
        return taskRepository.findById(id)
                .map(this::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
     public ResponseEntity<TaskResponseDTO> update(@PathVariable Long id, @Valid @RequestBody TaskRequestDTO taskDetails) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(taskDetails.getTitle());
                    task.setDescription(taskDetails.getDescription());
                    Task updatedTask = taskRepository.save(task);
                    return ResponseEntity.ok(toResponseDto(updatedTask));
                })
                .orElse(ResponseEntity.notFound().build());
    }   

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> delete(@PathVariable Long id) {
        return taskRepository.findById(id)
            .map(task -> {
                taskRepository.delete(task);
                return ResponseEntity.ok(toResponseDto(task)); // Return deleted task
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    private TaskResponseDTO toResponseDto(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .build();
    }

}
