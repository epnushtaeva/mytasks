package com.tasks.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateTaskBoardDto {
    private String name;
    private List<CreateTaskStatusDto> statuses;
    private List<Long> userIds;
}
