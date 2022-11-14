package com.controllers;

import com.security.dto.AjaxResultSuccessDto;
import com.tasks.dto.*;
import com.tasks.services.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/status")
@AllArgsConstructor
public class TaskStatusController {
    private final TaskStatusService taskStatusService;

    @PostMapping("/add")
    public EditTaskStatusDto addTaskStatus(@RequestBody CreateTaskStatusDto createTaskStatusDto, Principal principal){
        return this.taskStatusService.createStatus(createTaskStatusDto, principal);
    }

    @PostMapping("/edit")
    public EditTaskStatusDto editTaskStatus(@RequestBody EditTaskStatusDto editTaskStatusDto, Principal principal){
        return this.taskStatusService.editStatus(editTaskStatusDto, principal);
    }

    @PostMapping("/remove")
    public AjaxResultSuccessDto removeStatus(@RequestBody RemoveDto removeDto, Principal principal){
        return this.taskStatusService.removeStatus(removeDto, principal);
    }

    @GetMapping("/list")
    public List<ListStatusDto> getStatusesForBoard(@RequestParam("board_id") long boardId, Principal principal){
        return this.taskStatusService.getStatusesForBoard(boardId, principal);
    }
}
