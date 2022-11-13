package com.tasks.entities;

import com.tasks.entities.TaskBoard;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tasks_statuses")
@Getter
@Setter
public class TaskStatus {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "board_id", insertable = false, updatable = false)
    private long boardId;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private TaskBoard board;
}
