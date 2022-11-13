package com.tasks.repositories;

import com.tasks.entities.Task;
import com.tasks.entities.TaskBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskBoardRepository extends JpaRepository<TaskBoard, Long>, JpaSpecificationExecutor<TaskBoard> {
}
