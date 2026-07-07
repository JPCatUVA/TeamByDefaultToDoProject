package teambydefault.todo.rest_assured_tests.subtaskTests;


//Recommended imports from RESTAssured
import static io.restassured.RestAssured.*;

//other imports
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.UUID;

public class negativeSubtaskTests extends baseSubtaskTest {


    //Test 1, POST /subtask — missing todo (no parent task reference)
    @Test
    public void createSubtask_missingTodo(){

    given()
        .spec(authorizedRequestSpecSubtask(token))
        .body(Map.of(
                "title", st1.getTitle(),
                "description", st1.getDescription(),
                "isCompleted", st1.getIsCompleted(),
                "dueDate", st1.getDueDate().toString()
                
                )
            )
    .when()
        .post()
    .then()
        .spec(responseSpecSubtask(400, false));
    }


    //Test 2, POST /subtask — invalid taskId (todo doesn't exist), using random 
    //UUID here
    @Test
    public void createSubtask_nonExistentTodo(){

    given()
        .spec(authorizedRequestSpecSubtask(token))
        .body(Map.of(
                "title", st2.getTitle(),    
                "description", st2.getDescription(),
                "isCompleted", false,
                "todo", Map.of("taskId", UUID.randomUUID().toString())
                )
            )
    .when()
        .post()
    .then()
        .spec(responseSpecSubtask(400, false));
    }


    //Test 3, GET /subtask/{subtaskId} — subtask doesn't exist
    @Test
    public void getSubtaskById_nonExistent(){

    given()
        .spec(authorizedRequestSpecSubtask(token))
    .when()
        .get("/{subtaskId}", UUID.randomUUID().toString())
    .then()
        .spec(responseSpecSubtask(404, false));
    }


    //Test 4, DELETE /subtask/{subtaskId} — subtask doesn't exist
    @Test
    public void deleteSubtask_nonExistent(){

    given()
        .spec(authorizedRequestSpecSubtask(token))
    .when()
        .delete("/{subtaskId}", UUID.randomUUID().toString())
    .then()
        .spec(responseSpecSubtask(400, false));
    }


    //Test 5, PATCH /subtask/{subtaskId} — subtask doesn't exist
    @Test
    public void patchSubtask_nonExistent_returns400(){

    given()
        .spec(authorizedRequestSpecSubtask(token))
        .body(Map.of("title", "updated title"))
    .when()
        .patch("/{subtaskId}", UUID.randomUUID().toString())
    .then()
        .spec(responseSpecSubtask(400, false));
    }


    //Test 6, GET /subtask?taskId={taskId} — task doesn't exist
    @Test
    public void getAllSubtasks_nonExistentTask_returns400(){

    given()
        .spec(authorizedRequestSpecSubtask(token))
        .queryParam("taskId", UUID.randomUUID().toString())
    .when()
        .get()
    .then()
        .spec(responseSpecSubtask(400, false));
    }

}
//MORE EDGE CASES LATER