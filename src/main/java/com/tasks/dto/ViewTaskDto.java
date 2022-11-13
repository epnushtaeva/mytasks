package com.tasks.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewTaskDto {
    private long id;
    private String description;
    private long userId;
    private String userFullName;
    private String userLogin;
}
