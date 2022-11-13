package com.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponseDto {
    private String status = "200";
    private String token;
    private String refreshToken;
    private String userId;
    private String userFullName;
    private String userAvatar;
}
