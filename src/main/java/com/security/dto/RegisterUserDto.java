package com.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    private String login;
    private String password;
    private String fullName;
}
