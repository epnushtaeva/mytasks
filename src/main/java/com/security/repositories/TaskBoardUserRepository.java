package com.security.repositories;

import com.security.entities.TaskBoardUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskBoardUserRepository  extends JpaRepository<TaskBoardUser, Long> {
    TaskBoardUser findOneByUserIdAndBoardId(long userId, long boardId);

    List<TaskBoardUser> findAllByBoardId(long boardId);

    @Modifying
    @Query(nativeQuery = true, value="DELETE FROM board_users WHERE id=:id")
    void removeById(@Param("id") long id);
}
