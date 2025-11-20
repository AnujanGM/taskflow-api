package com.taskflow.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
}
