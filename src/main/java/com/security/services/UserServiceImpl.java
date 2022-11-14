package com.security.services;

import com.security.UserService;
import com.security.dto.*;
import com.security.entities.TaskBoardUser;
import com.security.entities.User;
import com.security.mappers.UserMapper;
import com.security.repositories.TaskBoardUserRepository;
import com.security.repositories.UserRepository;
import com.tasks.entities.TaskBoard;
import com.tasks.repositories.TaskBoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final TaskBoardUserRepository taskBoardUserRepository;

    private final UserMapper userMapper;

    private final TaskBoardRepository taskBoardRepository;

    @Override
    public User getUserByUserName(String userName){
        return this.userRepository.findOneByLogin(userName);
    }

    @Override
    public User getUserById(long id){
        return this.userRepository.getOne(id);
    }
    @Override
    @Transactional
    public List<UserListDto> findUsers(UserFilterDto filterDto){
        List<User> users = this.userRepository.findAll();

        if(!CollectionUtils.isEmpty(filterDto.getFilters())){
            if(filterDto.getFilters().containsKey("login") && !ObjectUtils.isEmpty(filterDto.getFilters().get("login"))){
                users = users.stream().filter(user -> user.getLogin().toLowerCase().contains(filterDto.getFilters().get("login").toLowerCase())).collect(Collectors.toList());
            }

            if(filterDto.getFilters().containsKey("fullName") && !ObjectUtils.isEmpty(filterDto.getFilters().get("fullName"))){
                users = users.stream().filter(user -> user.getFullName().toLowerCase().contains(filterDto.getFilters().get("fullName").toLowerCase())).collect(Collectors.toList());
            }
        }

        return this.userMapper.usersToListDtos(users);
    }

    @Override
    @Transactional
    public UserListDto addUserToTaskBoard(AddUserToTaskBoardDto addUserToTaskBoardDto, Principal principal){
        User user = this.userRepository.getOne(addUserToTaskBoardDto.getUserId());
        TaskBoard taskBoard = this.taskBoardRepository.getOne(addUserToTaskBoardDto.getTaskBoardId());

        if(this.isUserNoHasRights(taskBoard, principal)){
            return null;
        }

        TaskBoardUser existingUser = this.taskBoardUserRepository.findOneByUserIdAndBoardId(user.getId(), taskBoard.getId());

        if(!ObjectUtils.isEmpty(existingUser)){
            return this.userMapper.userToListDto(user);
        }

        TaskBoardUser taskBoardUser = new TaskBoardUser();
        taskBoardUser.setBoard(taskBoard);
        taskBoardUser.setUser(user);
        this.taskBoardUserRepository.save(taskBoardUser);
        return this.userMapper.userToListDto(user);
    }

    @Override
    @Transactional
    public AjaxResultSuccessDto removeUserFromBoard(AddUserToTaskBoardDto addUserToTaskBoardDto, Principal principal){
        TaskBoardUser existingUser = this.taskBoardUserRepository.findOneByUserIdAndBoardId(addUserToTaskBoardDto.getUserId(), addUserToTaskBoardDto.getTaskBoardId());

        if(ObjectUtils.isEmpty(existingUser)){
            return new AjaxResultSuccessDto();
        }

        this.taskBoardUserRepository.removeById(existingUser.getId());

        return new AjaxResultSuccessDto();
    }

    @Override
    @Transactional
    public AjaxResultSuccessDto removeUserFromBoard(long userId, long boardId){
        TaskBoardUser existingUser = this.taskBoardUserRepository.findOneByUserIdAndBoardId(userId, boardId);

        this.taskBoardUserRepository.removeById(existingUser.getId());

        return new AjaxResultSuccessDto();
    }

    @Override
    @Transactional
    public List<UserListDto> getUsersForBoard(long boardId, Principal principal){
       List<TaskBoardUser> taskBoardUsers = this.taskBoardUserRepository.findAllByBoardId(boardId);
       return this.userMapper.usersToListDtos(taskBoardUsers.stream().map(TaskBoardUser::getUser).collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public List<TaskBoardUserDto> addUsersForBoard(TaskBoard taskBoard, List<Long> userIds){
        LinkedHashSet<Long> uniqueUserIds = new LinkedHashSet<>();

        for(Long userId: userIds){
            uniqueUserIds.add(userId);
        }

        List<TaskBoardUserDto> resultUsers = new ArrayList<>();

        for(Long userId: uniqueUserIds){
            User user = this.userRepository.getOne(userId);
            TaskBoardUser taskBoardUser = new TaskBoardUser();
            taskBoardUser.setUser(user);
            taskBoardUser.setBoard(taskBoard);
            this.taskBoardUserRepository.save(taskBoardUser);
            resultUsers.add(this.userMapper.userToTaskBoardDto(user));
        }

        return resultUsers;
    }

    @Override
    @Transactional
    public List<TaskBoardUserDto> getUsers(List<TaskBoardUser> users){
        return this.userMapper.usersToTaskBoardDtos(users.stream().map(TaskBoardUser::getUser).collect(Collectors.toList()));
    }


    @Override
    @Transactional
    public AjaxResultSuccessDto registerNewUser(RegisterUserDto registerUserDto){
        User user = new User();

        if(!ObjectUtils.isEmpty(this.userRepository.findOneByLogin(registerUserDto.getLogin()))){
            AjaxResultSuccessDto resultSuccessDto = new AjaxResultSuccessDto();
            resultSuccessDto.setRes("Пользователь с таким логином уже существует");
            return  resultSuccessDto;
        }

        user.setLogin(registerUserDto.getLogin());

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

        user.setPassword(bCryptPasswordEncoder.encode(registerUserDto.getPassword()));

        user.setFullName(registerUserDto.getFullName());
        this.userRepository.save(user);
        return new AjaxResultSuccessDto();
    }

    private boolean isUserNoHasRights(TaskBoard taskBoard, Principal principal){
        User user = this.userRepository.findOneByLogin(principal.getName());

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
