package com.controllers;

import com.security.dto.AjaxResultSuccessDto;
import com.tasks.dto.*;
import com.tasks.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/task")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/add")
    public ViewTaskDto createTask(@RequestBody CreateTaskDto createTaskDto, Principal principal){
        return this.taskService.createTask(createTaskDto, principal);
    }

    @PostMapping("/edit")
    public ViewTaskDto editTask(@RequestBody EditTaskDto editTaskDto, Principal principal){
        return this.taskService.editTask(editTaskDto, principal);
    }

    @PostMapping("/remove")
    public AjaxResultSuccessDto removeTask(@RequestBody RemoveDto removeDto, Principal principal){
        return this.taskService.removeTask(removeDto, principal);
    }

    @PostMapping("/change_user")
    public ViewTaskDto changeTaskUser(@RequestBody ChangeTaskUserDto changeTaskUserDto, Principal principal){
        return this.taskService.changeUser(changeTaskUserDto, principal);
    }

    @PostMapping("/change_status")
    public ViewTaskDto changeTaskStatus(@RequestBody ChangeTaskStatusDto changeTaskStatusDto, Principal principal){
        return this.taskService.changeStatus(changeTaskStatusDto, principal);
    }
}
