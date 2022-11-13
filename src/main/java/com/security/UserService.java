package com.security;
import com.security.dto.*;
import com.security.entities.TaskBoardUser;
import com.security.entities.User;
import com.tasks.entities.TaskBoard;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface UserService {
    User getUserByUserName(String userName);

    List<UserListDto> findUsers(UserFilterDto filterDto);

    UserListDto addUserToTaskBoard(AddUserToTaskBoardDto addUserToTaskBoardDto, Principal principal);

    AjaxResultSuccessDto removeUserFromBoard(AddUserToTaskBoardDto addUserToTaskBoardDto, Principal principal);

    List<UserListDto> getUsersForBoard(long boardId, Principal principal);

    List<TaskBoardUserDto> addUsersForBoard(TaskBoard taskBoard, List<Long> userIds);

    List<TaskBoardUserDto> getUsers(List<TaskBoardUser> users);

    AjaxResultSuccessDto registerNewUser(RegisterUserDto registerUserDto);
}
