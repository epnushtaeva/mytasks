package com.security.mappers;


import com.security.dto.AuthenticationResponseDto;
import com.security.dto.ProfileInfoDto;
import com.security.dto.TaskBoardUserDto;
import com.security.dto.UserListDto;
import com.security.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    TaskBoardUserDto userToTaskBoardDto(User user);

    List<TaskBoardUserDto> usersToTaskBoardDtos(List<User> users);

    ProfileInfoDto userToProfileInfoDto(User user);

    UserListDto userToListDto(User user);

    List<UserListDto> usersToListDtos(List<User> users);

    @Mapping(source="id", target="userId")
    @Mapping(source="fullName", target="userFullName")
    AuthenticationResponseDto userToAuthenticationResponseDto(User user);
}
