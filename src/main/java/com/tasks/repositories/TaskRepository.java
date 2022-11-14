package com.tasks.repositories;

import com.tasks.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByStatusIdAndBoardId(long statusId, long boardId);

    @Modifying
    @Query(nativeQuery = true, value="DELETE FROM tasks WHERE id=:id")
    void removeById(@Param("id") long id);

    List<Task> findAllByStatusId(long statusId);
}
