package com.tasks.services.impl;

import com.security.UserService;
import com.security.dto.AjaxResultSuccessDto;
import com.security.entities.TaskBoardUser;
import com.security.entities.User;
import com.tasks.dto.*;
import com.tasks.entities.Task;
import com.tasks.entities.TaskBoard;
import com.tasks.mappers.TaskMapper;
import com.tasks.repositories.TaskBoardRepository;
import com.tasks.repositories.TaskRepository;
import com.tasks.services.TaskService;
import com.tasks.services.TaskStatusService;
import liquibase.repackaged.org.apache.commons.lang3.ObjectUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    private final UserService userService;

    private final TaskBoardRepository taskBoardRepository;

    private final TaskStatusService taskStatusService;

    @Override
    @Transactional
    public List<ViewTaskDto> getTasksForStatusAndBoard(long statusId, long boardId){
        return this.taskMapper.tasksToViewDtos(this.taskRepository.findAllByStatusIdAndBoardId(statusId, boardId));
    }

    @Override
    @Transactional
    public ViewTaskDto createTask(CreateTaskDto createOrEditTaskDto, Principal principal){
        Task task = new Task();
        TaskBoard taskBoard = this.taskBoardRepository.getOne(createOrEditTaskDto.getBoardId());

        if(this.isUserNoHasRights(taskBoard, principal)){
          return null;
        }

        task.setDescription(createOrEditTaskDto.getDescription());

        if(!ObjectUtils.isEmpty(createOrEditTaskDto.getUserId())){
            task.setUser(this.userService.getUserById(createOrEditTaskDto.getUserId()));
        }

        task.setBoard(taskBoard);
        task.setStatus(this.taskStatusService.getStatusById(createOrEditTaskDto.getStatusId()));
        this.taskRepository.save(task);
        return this.taskMapper.taskToViewDto(task);
    }

    @Override
    @Transactional
    public ViewTaskDto editTask(EditTaskDto createOrEditTaskDto, Principal principal){
        Task task = this.taskRepository.getOne(createOrEditTaskDto.getId());
        TaskBoard taskBoard = task.getBoard();

        if(this.isUserNoHasRights(taskBoard, principal)){
            return null;
        }

        task.setDescription(createOrEditTaskDto.getDescription());

        if(!ObjectUtils.isEmpty(createOrEditTaskDto.getUserId())){
            task.setUser(this.userService.getUserById(createOrEditTaskDto.getUserId()));
        } else {
            task.setUser(null);
        }

        task.setStatus(this.taskStatusService.getStatusById(createOrEditTaskDto.getStatusId()));
        this.taskRepository.save(task);
        return this.taskMapper.taskToViewDto(task);
    }

    @Override
    @Transactional
    public AjaxResultSuccessDto removeTask(RemoveDto removeDto, Principal principal){
        this.taskRepository.removeById(removeDto.getId());
        return new AjaxResultSuccessDto();
    }

    @Override
    @Transactional
    public ViewTaskDto changeUser(ChangeTaskUserDto changeTaskUserDto, Principal principal) {
        Task task = this.taskRepository.getOne(changeTaskUserDto.getTaskId());

        if(this.isUserNoHasRights(task.getBoard(), principal)){
            return null;
        }

        if(!ObjectUtils.isEmpty(changeTaskUserDto.getUserId())){
            task.setUser(this.userService.getUserById(changeTaskUserDto.getUserId()));
        } else {
            task.setUser(null);
        }
        this.taskRepository.save(task);
        return this.taskMapper.taskToViewDto(task);
    }

    @Override
    @Transactional
    public ViewTaskDto changeStatus(ChangeTaskStatusDto changeTaskStatusDto, Principal principal) {
        Task task = this.taskRepository.getOne(changeTaskStatusDto.getTaskId());

        if(this.isUserNoHasRights(task.getBoard(), principal)){
            return null;
        }

        task.setStatus(this.taskStatusService.getStatusById(changeTaskStatusDto.getStatusId()));
        this.taskRepository.save(task);
        return this.taskMapper.taskToViewDto(task);
    }

    @Override
    @Transactional
    public void removeAllByStatusId(long statusId){
        List<Task> statusTasks = this.taskRepository.findAllByStatusId(statusId);

        for(Task task: statusTasks){
            this.taskRepository.removeById(task.getId());
        }
    }

    private boolean isUserNoHasRights(TaskBoard taskBoard, Principal principal){
        User user = this.userService.getUserByUserName(principal.getName());

        if(!taskBoard
                .getBoardUsers()
                .stream()
                .map(TaskBoardUser::getUser)
                .map(User::getId)
                .collect(Collectors.toList())
                .contains(user.getId())){
            return true;
        }

        return false;
    }
}
