package com.controllers;

import com.security.UserService;
import com.security.dto.AjaxResultSuccessDto;
import com.security.dto.RegisterUserDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/register")
@AllArgsConstructor
public class RegisterUserController {
    private final UserService userService;

    @PostMapping("")
    public AjaxResultSuccessDto registerNewUser(@RequestBody RegisterUserDto registerUserDto){
        return this.userService.registerNewUser(registerUserDto);
    }
}
