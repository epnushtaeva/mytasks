package com.tasks.mappers;

import com.tasks.dto.ViewTaskDto;
import com.tasks.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "userFullName")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "status.id", target = "statusId")
    @Mapping(source = "status.name", target = "statusName")
    ViewTaskDto taskToViewDto(Task task);

    List<ViewTaskDto> tasksToViewDtos(List<Task> tasks);
}
