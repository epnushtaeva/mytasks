package com.tasks.services;

import com.tasks.dto.CreateTaskStatusDto;
import com.tasks.dto.EditTaskStatusDto;
import com.tasks.dto.ListStatusDto;
import com.tasks.entities.TaskBoard;
import com.tasks.entities.TaskStatus;

import java.security.Principal;
import java.util.List;

public interface TaskStatusService {
    TaskStatus getStatusById(long statusId);

    List<ListStatusDto> createStatusesForBoard(TaskBoard taskBoard, List<CreateTaskStatusDto> boardStatusesDtos);

    List<ListStatusDto> getStatusesForBoard(long boardId, Principal principal);

    EditTaskStatusDto editStatus(EditTaskStatusDto taskStatusDto, Principal principal);

    EditTaskStatusDto createStatus(CreateTaskStatusDto taskStatusDto, Principal principal);
}
