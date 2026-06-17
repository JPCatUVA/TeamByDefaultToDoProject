package teambydefault.todo.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "ToDos")
public class ToDo {

    @Column(name = "taskId")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID taskId;

    //foreign key from user
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "title", nullable = false, unique = true) //SHOULD TASK TITLES BE UNIQUE?
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "dueDate")//, nullable = true //I dont think a due date/time should be strictly necessary
    private LocalDateTime dueDate;

    @Column(name = "isCompleted")
    private boolean isCompleted;
}
