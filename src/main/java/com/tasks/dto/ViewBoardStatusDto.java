package com.tasks.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ViewBoardStatusDto {
    private long id;
    private String name;
    List<ViewTaskDto> tasks;
}
