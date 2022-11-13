package com.tasks.services.impl;

import com.security.UserService;
import com.security.dto.AjaxResultSuccessDto;
import com.security.entities.TaskBoardUser;
import com.security.entities.User;
import com.tasks.dto.*;
import com.tasks.entities.TaskBoard;
import com.tasks.mappers.TaskBoardMapper;
import com.tasks.repositories.TaskBoardRepository;
import com.tasks.services.TaskBoardService;
import com.tasks.services.TaskService;
import com.tasks.services.TaskStatusService;
import com.utils.SpecificationUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskBoardServiceImpl implements TaskBoardService {
    private final TaskBoardRepository taskBoardRepository;
    private final TaskStatusService taskStatusService;

    private final TaskService taskService;
    private final TaskBoardMapper taskBoardMapper;

    private final UserService userService;

    @Override
    @Transactional
    public AjaxResultSuccessDto checkUserHasRights(long boardId, Principal principal){;
        AjaxResultSuccessDto result = new AjaxResultSuccessDto();
        TaskBoard taskBoard = this.taskBoardRepository.getOne(boardId);

        if(!this.isUserNoHasRights(taskBoard, principal)){
            result.setRes("no_rights");
        }

        return result;
    }

    @Override
    @Transactional
    public EditTaskBoardDto createBoard(CreateTaskBoardDto boardDto, Principal principal){
        TaskBoard taskBoard = new TaskBoard();
        taskBoard.setName(boardDto.getName());
        this.taskBoardRepository.save(taskBoard);

        EditTaskBoardDto result = this.taskBoardMapper.taskBoardToEditDto(taskBoard);
        result.setStatuses(this.taskStatusService.createStatusesForBoard(taskBoard, boardDto.getStatuses()));

        User currentUser = this.userService.getUserByUserName(principal.getName());

        if(CollectionUtils.isEmpty(boardDto.getUserIds()) || !boardDto.getUserIds().contains(currentUser.getId())){
            if(CollectionUtils.isEmpty(boardDto.getUserIds())){
              boardDto.setUserIds(new ArrayList<>());
            }

            boardDto.getUserIds().add(currentUser.getId());
        }

        result.setUsers(this.userService.addUsersForBoard(taskBoard, boardDto.getUserIds()));

        return result;
    }

    @Override
    @Transactional
    public EditTaskBoardDto getBoardForEdit(long boardId, Principal principal){
        TaskBoard taskBoard = this.taskBoardRepository.getOne(boardId);

        if(this.isUserNoHasRights(taskBoard, principal)){
            return null;
        }

        EditTaskBoardDto result = this.taskBoardMapper.taskBoardToEditDto(taskBoard);
        result.setStatuses(this.taskStatusService.getStatusesForBoard(boardId, principal));
        result.setUsers(this.userService.getUsers(taskBoard.getBoardUsers()));
        return result;
    }

    @Override
    @Transactional
    public EditTaskBoardDto editBoard(EditTaskBoardDto editTaskBoardDto, Principal principal){
        TaskBoard taskBoard = this.taskBoardRepository.getOne(editTaskBoardDto.getId());

        if(this.isUserNoHasRights(taskBoard, principal)){
            return null;
        }

        taskBoard.setName(editTaskBoardDto.getName());
        this.taskBoardRepository.save(taskBoard);
        return editTaskBoardDto;
    }

    @Override
    @Transactional
    public List<ListTaskBoardDto> getBoards(TaskBoardsFilterDto taskBoardsFilterDto, Principal principal){
        if(taskBoardsFilterDto.getFilters() == null){
            taskBoardsFilterDto.setFilters(new HashMap<>());
        }

        User user = this.userService.getUserByUserName(principal.getName());

        taskBoardsFilterDto.getFilters().put("currentUserId", String.valueOf(user.getId()));

        Specification<TaskBoard> filterSpecification = SpecificationUtils.getTaskBoardsSpecification(taskBoardsFilterDto.getFilters());
        return this.taskBoardMapper.taskBoardsToListDtos(this.taskBoardRepository.findAll(filterSpecification));
    }

    @Override
    @Transactional
    public ViewTaskBoardDto getBoard(long boardId, Principal principal){
        TaskBoard taskBoard = this.taskBoardRepository.getOne(boardId);

        if(this.isUserNoHasRights(taskBoard, principal)){
            return null;
        }

        ViewTaskBoardDto viewTaskBoardDto = this.taskBoardMapper.taskBoardToViewDto(taskBoard);
        viewTaskBoardDto.setUsers(this.userService.getUsers(taskBoard.getBoardUsers()));

        for(ViewBoardStatusDto boardStatusDto: viewTaskBoardDto.getStatuses()){
            boardStatusDto.setTasks(this.taskService.getTasksForStatusAndBoard(boardStatusDto.getId(),boardId));
        }

        return viewTaskBoardDto;
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
