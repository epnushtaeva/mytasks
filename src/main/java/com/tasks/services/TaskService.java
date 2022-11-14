package com.tasks.services;

import com.security.dto.AjaxResultSuccessDto;
import com.tasks.dto.*;

import java.security.Principal;
import java.util.List;

public interface TaskService {
    List<ViewTaskDto> getTasksForStatusAndBoard(long statusId, long boardId);

    ViewTaskDto createTask(CreateTaskDto createOrEditTaskDto, Principal principal);

    ViewTaskDto editTask(EditTaskDto createOrEditTaskDto, Principal principal);

    AjaxResultSuccessDto removeTask(RemoveDto removeDto, Principal principal);

    ViewTaskDto changeUser(ChangeTaskUserDto changeTaskUserDto, Principal principal);

    ViewTaskDto changeStatus(ChangeTaskStatusDto changeTaskStatusDto, Principal principal);
}
