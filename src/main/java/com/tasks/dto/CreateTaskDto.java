package com.tasks.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaskDto {
    private String description;
    private long boardId;
    private long statusId;
    private Long userId;
}
