package com.tasks.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeTaskUserDto {
    private long taskId;
    private Long userId;
}
