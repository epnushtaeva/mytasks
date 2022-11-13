package com.tasks.dto;

import com.security.dto.TaskBoardUserDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EditTaskBoardDto {
    private long id;
    private String name;
    private List<ListStatusDto> statuses;
    private List<TaskBoardUserDto> users;
}
