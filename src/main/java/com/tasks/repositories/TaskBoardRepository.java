package com.tasks.repositories;

import com.tasks.entities.Task;
import com.tasks.entities.TaskBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskBoardRepository extends JpaRepository<TaskBoard, Long>, JpaSpecificationExecutor<TaskBoard> {
    @Modifying
    @Query(nativeQuery = true, value="DELETE FROM tasks_boards WHERE id=:id")
    void removeById(@Param("id") long id);
}
