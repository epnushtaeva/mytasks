package com.tasks.mappers;

import com.tasks.dto.EditTaskBoardDto;
import com.tasks.dto.ListTaskBoardDto;
import com.tasks.dto.ViewTaskBoardDto;
import com.tasks.entities.TaskBoard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {TaskStatusMapper.class})
public interface TaskBoardMapper {
    EditTaskBoardDto taskBoardToEditDto(TaskBoard taskBoard);

    @Mapping(source = "taskStatuses", target = "statuses")
    ViewTaskBoardDto taskBoardToViewDto(TaskBoard taskBoard);

    ListTaskBoardDto taskBoardToListDto(TaskBoard taskBoard);

    List<ListTaskBoardDto> taskBoardsToListDtos(List<TaskBoard> taskBoard);
}
