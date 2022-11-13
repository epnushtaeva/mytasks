package com.tasks.services.impl;

import com.tasks.dto.ViewTaskDto;
import com.tasks.mappers.TaskMapper;
import com.tasks.repositories.TaskRepository;
import com.tasks.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    @Transactional
    public List<ViewTaskDto> getTasksForStatusAndBoard(long statusId, long boardId){
        return this.taskMapper.tasksToViewDtos(this.taskRepository.findAllByStatusIdAndBoardId(statusId, boardId));
    }
}
