package com.tasks.services.impl;

import com.security.UserService;
import com.security.dto.AjaxResultSuccessDto;
import com.security.entities.TaskBoardUser;
import com.security.entities.User;
import com.tasks.dto.CreateTaskStatusDto;
import com.tasks.dto.EditTaskStatusDto;
import com.tasks.dto.ListStatusDto;
import com.tasks.dto.RemoveDto;
import com.tasks.entities.Task;
import com.tasks.entities.TaskBoard;
import com.tasks.entities.TaskStatus;
import com.tasks.mappers.TaskStatusMapper;
import com.tasks.repositories.TaskBoardRepository;
import com.tasks.repositories.TaskRepository;
import com.tasks.repositories.TaskStatusRepository;
import com.tasks.services.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskStatusServiceImpl  implements TaskStatusService {
    private final TaskStatusRepository taskStatusRepository;
    private final TaskBoardRepository taskBoardRepository;

    private final TaskRepository taskRepository;

    private final TaskStatusMapper taskStatusMapper;
    private final UserService userService;

    @Override
    public TaskStatus getStatusById(long statusId){
        return this.taskStatusRepository.getOne(statusId);
    }

    @Override
    @Transactional
    public List<ListStatusDto> createStatusesForBoard(TaskBoard taskBoard, List<CreateTaskStatusDto> boardStatusesDtos){
        List<TaskStatus> createdStatuses = new ArrayList<>();

        if(CollectionUtils.isEmpty(boardStatusesDtos)){
            boardStatusesDtos = this.getDefaultBoardStatusesDtos();
        }

        for(CreateTaskStatusDto createTaskStatusDto: boardStatusesDtos) {
            TaskStatus taskStatus = new TaskStatus();
            taskStatus.setBoard(taskBoard);
            taskStatus.setName(createTaskStatusDto.getName());
            this.taskStatusRepository.save(taskStatus);
            createdStatuses.add(taskStatus);
        }

        return this.taskStatusMapper.taskStatusesToListDtos(createdStatuses);
    }

    @Override
    @Transactional
    public List<ListStatusDto> getStatusesForBoard(long boardId, Principal principal){
        TaskBoard taskBoard = this.taskBoardRepository.getOne(boardId);
        User user = this.userService.getUserByUserName(principal.getName());

        if(!taskBoard
                .getBoardUsers()
                .stream()
                .map(TaskBoardUser::getUser)
                .map(User::getId)
                .collect(Collectors.toList())
                .contains(user.getId())){
            return null;
        }

        return this.taskStatusMapper.taskStatusesToListDtos(this.taskStatusRepository.findAllByBoardId(boardId));
    }

    @Override
    @Transactional
    public EditTaskStatusDto editStatus(EditTaskStatusDto taskStatusDto, Principal principal){
        TaskStatus taskStatus = this.taskStatusRepository.getOne(taskStatusDto.getId());

        if(this.isUserNoHasRights(taskStatus.getBoard(), principal)){
            return null;
        }

        taskStatus.setName(taskStatusDto.getName());
        this.taskStatusRepository.save(taskStatus);
        return taskStatusDto;
    }

    @Override
    @Transactional
    public EditTaskStatusDto createStatus(CreateTaskStatusDto taskStatusDto, Principal principal){
        TaskStatus taskStatus = new TaskStatus();
        TaskBoard taskBoard = this.taskBoardRepository.getReferenceById(taskStatusDto.getBoardId());

        if(this.isUserNoHasRights(taskBoard, principal)){
            return null;
        }

        taskStatus.setBoard(taskBoard);
        taskStatus.setName(taskStatusDto.getName());
        taskStatus.setBoard(this.taskBoardRepository.getOne(taskStatusDto.getBoardId()));
        this.taskStatusRepository.save(taskStatus);
        return this.taskStatusMapper.taskStatusToEditDto(taskStatus);
    }

    @Override
    @Transactional
    public AjaxResultSuccessDto removeStatus(RemoveDto removeDto, Principal principal){
        TaskStatus taskStatus = this.taskStatusRepository.getOne(removeDto.getId());

        if(this.isUserNoHasRights(taskStatus.getBoard(), principal)){
            return null;
        }

        this.removeAllTasksByStatusId(taskStatus.getId());
        this.taskStatusRepository.removeById(taskStatus.getId());
        return new AjaxResultSuccessDto();
    }

    @Override
    @Transactional
    public void removeAllByBoardId(long boardId){
        List<TaskStatus> taskStatuses = this.taskStatusRepository.findAllByBoardId(boardId);

        for(TaskStatus taskStatus: taskStatuses){
            this.removeAllTasksByStatusId(taskStatus.getId());
            this.taskStatusRepository.removeById(taskStatus.getId());
        }
    }

    private void removeAllTasksByStatusId(long statusId){
        List<Task> statusTasks = this.taskRepository.findAllByStatusId(statusId);

        for(Task task: statusTasks){
            this.taskRepository.removeById(task.getId());
        }
    }

    private List<CreateTaskStatusDto> getDefaultBoardStatusesDtos(){
        return Arrays.asList(
                new CreateTaskStatusDto("Ожидает взятия в работу", null),
                new CreateTaskStatusDto("В работе", null),
                new CreateTaskStatusDto("Работа завершена", null)
        );
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
