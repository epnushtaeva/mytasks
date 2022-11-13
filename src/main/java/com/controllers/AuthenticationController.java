package com.controllers;

import com.security.UserService;
import com.security.dto.AuthenticationRequestDto;
import com.security.dto.AuthenticationResponseDto;
import com.security.entities.User;
import com.security.jwt.JwtTokenProvider;
import com.security.mappers.UserMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping(value = "/api/token")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/refresh")
    public AuthenticationResponseDto login(HttpServletRequest req, HttpServletResponse res, @RequestParam("refresh_token") String refreshToken) throws IOException, ServletException {
        try {
                if(jwtTokenProvider.validateRefreshToken(req, res, refreshToken)) {
                    User user = this.userService.getUserByUserName(jwtTokenProvider.getUsername(refreshToken));
                    AuthenticationResponseDto authenticationResponseDto = this.userMapper.userToAuthenticationResponseDto(user);
                    authenticationResponseDto.setToken(jwtTokenProvider.createToken(user.getLogin(), Arrays.asList("admin")));
                    return authenticationResponseDto;
                } else {
                    AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();
                    authenticationResponseDto.setStatus("401");
                    return authenticationResponseDto;
                }
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid refresh token");
        }
    }

    @PostMapping("")
    public AuthenticationResponseDto login(HttpServletRequest request, @RequestBody AuthenticationRequestDto requestDto) {
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));

            User user = this.userService.getUserByUserName(username);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            if (StringUtils.isEmpty(user.getPassword())) {
                AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();
                authenticationResponseDto.setStatus("401");
                return authenticationResponseDto;
            }

            AuthenticationResponseDto authenticationResponseDto = this.userMapper.userToAuthenticationResponseDto(user);
            authenticationResponseDto.setToken(jwtTokenProvider.createToken(username, Arrays.asList("admin")));
            authenticationResponseDto.setRefreshToken(jwtTokenProvider.createRefreshToken(username, Arrays.asList("admin")));
            return authenticationResponseDto;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
