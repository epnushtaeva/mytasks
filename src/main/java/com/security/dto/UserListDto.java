package com.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserListDto {
    private long id;
    private String login;
    private String fullName;
}
