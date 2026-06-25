package teambydefault.todo.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "ToDos")
public class Todo {

    @Column(name = "task_id")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID taskId;

    //foreign key from user
    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false, unique = true) //SHOULD TASK TITLES BE UNIQUE?
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "dueDate")//, nullable = true //I dont think a due date/time should be strictly necessary
    private LocalDateTime dueDate;

    @Column(name = "isCompleted")
    private boolean isCompleted;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subtask> subtasks;
}
