package com.security;

import com.security.dto.ProfileEditDto;
import com.security.dto.ProfileInfoDto;

import java.security.Principal;

public interface ProfileService {
    ProfileInfoDto getProfileInfo(Principal principal);

    ProfileInfoDto editProfile(Principal principal, ProfileEditDto profileEditDto);
}
