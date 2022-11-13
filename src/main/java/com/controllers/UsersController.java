package com.controllers;

import com.security.UserService;
import com.security.dto.AddUserToTaskBoardDto;
import com.security.dto.AjaxResultSuccessDto;
import com.security.dto.UserFilterDto;
import com.security.dto.UserListDto;
import com.tasks.services.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/users")
@AllArgsConstructor
public class UsersController {
    private final UserService userService;

    @PostMapping("/find")
    public List<UserListDto> getUsers(@RequestBody UserFilterDto userFilterDto){
        return this.userService.findUsers(userFilterDto);
    }

    @PostMapping("/add_to_board")
    public UserListDto addUserToBoard(@RequestBody AddUserToTaskBoardDto addUserToTaskBoardDto, Principal principal){
        return this.userService.addUserToTaskBoard(addUserToTaskBoardDto, principal);
    }

    @PostMapping("/remove_from_board")
    public AjaxResultSuccessDto removeUserFromBoard(@RequestBody AddUserToTaskBoardDto addUserToTaskBoardDto, Principal principal){
        return this.userService.removeUserFromBoard(addUserToTaskBoardDto, principal);
    }

    @GetMapping("/get_for_board")
    public List<UserListDto> getUsersForBoard(@RequestParam("board_id") long boardId, Principal principal){
        return this.userService.getUsersForBoard(boardId, principal);
    }
}
