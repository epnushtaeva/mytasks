package com.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserToTaskBoardDto {
    private long userId;
    private long taskBoardId;
}
