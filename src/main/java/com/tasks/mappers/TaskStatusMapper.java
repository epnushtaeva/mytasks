package com.tasks.mappers;

import com.tasks.dto.EditTaskStatusDto;
import com.tasks.dto.ListStatusDto;
import com.tasks.dto.ViewBoardStatusDto;
import com.tasks.entities.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskStatusMapper {
    EditTaskStatusDto taskStatusToEditDto(TaskStatus taskStatus);

    ListStatusDto taskStatusToListDto(TaskStatus taskStatus);

    List<ListStatusDto> taskStatusesToListDtos(List<TaskStatus> taskStatuses);

    ViewBoardStatusDto taskStatusToViewDto(TaskStatus taskStatus);

    List<ViewBoardStatusDto> taskStatusesToViewDtos(List<TaskStatus> taskStatuses);
}
