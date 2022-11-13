package com.tasks.services;

import com.tasks.dto.ViewTaskDto;

import java.util.List;

public interface TaskService {
    List<ViewTaskDto> getTasksForStatusAndBoard(long statusId, long boardId);
}
