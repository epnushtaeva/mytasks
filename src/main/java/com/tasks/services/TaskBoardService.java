package com.tasks.services;

import com.security.dto.AjaxResultSuccessDto;
import com.tasks.dto.*;

import java.security.Principal;
import java.util.List;

public interface TaskBoardService {
    AjaxResultSuccessDto checkUserHasRights(long boardId, Principal principal);

    EditTaskBoardDto createBoard(CreateTaskBoardDto boardDto, Principal principal);

    EditTaskBoardDto getBoardForEdit(long boardId, Principal principal);

    EditTaskBoardDto editBoard(EditTaskBoardDto editTaskBoardDto, Principal principal);

    List<ListTaskBoardDto> getBoards(TaskBoardsFilterDto taskBoardsFilterDto, Principal principal);

    ViewTaskBoardDto getBoard(long boardId, Principal principal);
}
