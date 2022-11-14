package com.tasks.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditTaskDto {
    private long id;
    private String description;
    private long statusId;
    private Long userId;
}
