package com.taskflow.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskRequestDTO {
    @NotBlank(message = "Title is required")
    @Size(min = 3,max = 100)
    private String title;
    private String description;    
}
