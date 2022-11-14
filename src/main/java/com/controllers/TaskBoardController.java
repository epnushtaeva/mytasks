package com.controllers;

import com.security.dto.AjaxResultSuccessDto;
import com.tasks.dto.*;
import com.tasks.services.TaskBoardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/board")
@AllArgsConstructor
public class TaskBoardController {
    private final TaskBoardService taskBoardService;

    @PostMapping("/list")
    public List<ListTaskBoardDto> getBoard(@RequestBody TaskBoardsFilterDto filters, Principal principal){
        return this.taskBoardService.getBoards(filters, principal);
    }

    @PostMapping("/add")
    public EditTaskBoardDto addTaskBoard(@RequestBody CreateTaskBoardDto createTaskBoardDto, Principal principal){
        return this.taskBoardService.createBoard(createTaskBoardDto, principal);
    }

    @GetMapping("/get_for_edit")
    public EditTaskBoardDto getBoardForEdit(@RequestParam("board_id") long boardId, Principal principal){
        return this.taskBoardService.getBoardForEdit(boardId, principal);
    }

    @PostMapping("/edit")
    public EditTaskBoardDto editBoard(@RequestBody EditTaskBoardDto editTaskBoardDto, Principal principal){
        return this.taskBoardService.editBoard(editTaskBoardDto, principal);
    }

    @PostMapping("/remove")
    public AjaxResultSuccessDto removeBoard(@RequestBody RemoveDto removeDto, Principal principal){
        return this.taskBoardService.removeBoard(removeDto, principal);
    }

    @GetMapping("/get")
    public ViewTaskBoardDto getBoard(@RequestParam("board_id") long boardId, Principal principal){
        return this.taskBoardService.getBoard(boardId, principal);
    }

    @PostMapping("/check_rights")
    public AjaxResultSuccessDto checkUserRights(@RequestParam("board_id") long boardId, Principal principal){
        return this.taskBoardService.checkUserHasRights(boardId, principal);
    }
}
