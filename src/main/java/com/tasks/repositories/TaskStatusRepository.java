package com.tasks.repositories;

import com.tasks.entities.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    List<TaskStatus> findAllByBoardId(long boardId);

    @Modifying
    @Query(nativeQuery = true, value="DELETE FROM tasks_statuses WHERE id=:id")
    void removeById(@Param("id") long id);
}
