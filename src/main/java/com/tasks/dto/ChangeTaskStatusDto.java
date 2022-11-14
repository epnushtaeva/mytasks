package com.tasks.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeTaskStatusDto {
    private long taskId;
    private long statusId;
}
