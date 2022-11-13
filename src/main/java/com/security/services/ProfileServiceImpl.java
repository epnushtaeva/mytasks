package com.security.services;

import com.security.ProfileService;
import com.security.dto.ProfileEditDto;
import com.security.dto.ProfileInfoDto;
import com.security.entities.User;
import com.security.mappers.UserMapper;
import com.security.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public ProfileInfoDto getProfileInfo(Principal principal){
        User currentUser = this.userRepository.findOneByLogin(principal.getName());
        return this.userMapper.userToProfileInfoDto(currentUser);
    }

    @Override
    public ProfileInfoDto editProfile(Principal principal, ProfileEditDto profileEditDto){
        User currentUser = this.userRepository.findOneByLogin(principal.getName());
        currentUser.setFullName(profileEditDto.getFullNane());

        if(!StringUtils.isEmpty(profileEditDto.getPassword())) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
            currentUser.setPassword(bCryptPasswordEncoder.encode(profileEditDto.getPassword()));
        }

        this.userRepository.save(currentUser);
        return this.userMapper.userToProfileInfoDto(currentUser);
    }
}
