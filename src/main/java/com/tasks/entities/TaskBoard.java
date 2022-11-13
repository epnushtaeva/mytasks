package com.tasks.entities;

import com.security.entities.TaskBoardUser;
import com.security.entities.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tasks_boards")
@Getter
@Setter
public class TaskBoard {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "board", cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private List<TaskStatus> taskStatuses;

    @OneToMany(mappedBy = "board", cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private List<TaskBoardUser> boardUsers;
}
