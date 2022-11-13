package com.tasks.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class TaskBoardsFilterDto {
    private Map<String, String> filters;
}
