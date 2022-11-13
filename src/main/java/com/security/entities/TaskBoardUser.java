package com.security.entities;

import com.tasks.entities.TaskBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "board_users")
@Getter
@Setter
public class TaskBoardUser {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id", insertable = false, updatable = false)
    private long userId;

    @Column(name = "board_id", insertable = false, updatable = false)
    private long boardId;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private TaskBoard board;
}
