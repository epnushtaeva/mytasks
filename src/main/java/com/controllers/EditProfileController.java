package com.controllers;

import com.security.ProfileService;
import com.security.dto.ProfileEditDto;
import com.security.dto.ProfileInfoDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/profile_inf")
@AllArgsConstructor
public class EditProfileController {
    private final ProfileService profileService;

    @GetMapping("/get")
    public ProfileInfoDto getProfileInfo(Principal principal){
        return this.profileService.getProfileInfo(principal);
    }

    @PostMapping("/edit")
    public ProfileInfoDto editProfile(@RequestBody ProfileEditDto profileEditDto, Principal principal){
        return this.profileService.editProfile(principal, profileEditDto);
    }
}
