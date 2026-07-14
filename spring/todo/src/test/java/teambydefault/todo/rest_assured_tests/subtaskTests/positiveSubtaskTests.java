package teambydefault.todo.rest_assured_tests.subtaskTests;

//Recommended imports from RESTAssured
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

//other imports
import org.junit.jupiter.api.Test;
import java.util.Map;

public class positiveSubtaskTests extends baseSubtaskTest{
    

/////////////////////////////////////// SETUP STUFF ////////////////////////////
//orignally here, moved to baseSubtaskTest, for cleanliness
//////////////////////////////////////// TESTS /////////////////////////////////
/// 
/// Map.of was used throughout this class instead of Java Object serialization 
/// beacuse it was working incorrectly with the way we have our entities set up
/// (I think, somehow creating too many circlular references) so instead of
/// changing the Entity classes I have simply Mapped explicit objects here.
/// Perhaps it could be changed later


    //Test 1, adding a subTask
    @Test
    public void addSubtaskTest(){

    given()
        .spec(authorizedRequestSpecSubtask(token))
        .body(Map.of(
                "title", st1.getTitle(),
                "description", st1.getDescription(),
                "dueDate", st1.getDueDate().toString(),
                "isCompleted", false,
                "todo", Map.of("taskId", testTask.getTaskId().toString())
                )
            )
    .when()
        .post()
    .then()
        .spec(responseSpecSubtask(200, true))
        .body("id", notNullValue())
        .body("title", equalTo("practiceSubTask"))
        .body("description", equalTo("this is for testing"))
        .body("dueDate", equalTo(st1.getDueDate().toString()))
        .body("isCompleted", equalTo(false));
    }

    //Test 2, delete subtask
    @Test
    public void deleteSubtaskTest(){

    //first, create a subtask so we have something to delete
    String subtaskId =
    given()
        .spec(authorizedRequestSpecSubtask(token))
        .body(Map.of(
                "title", st2.getTitle(),
                "description", st2.getDescription(),
                "dueDate", st2.getDueDate().toString(),
                "isCompleted", false,
                "todo", Map.of("taskId", testTask.getTaskId().toString())
                )
            )
    .when()
        .post()
    .then()
        .statusCode(200)
        .extract().path("id");

    //now delete it
    given()
        .spec(authorizedRequestSpecSubtask(token))
    .when()
        .delete("/{subtaskId}", subtaskId)
    .then()
        .spec(responseSpecSubtask(204, false));

    //verify it's actually gone
    given()
        .spec(authorizedRequestSpecSubtask(token))
    .when()
        .get("/{subtaskId}", subtaskId)
    .then()
        .spec(responseSpecSubtask(404, false));
    }


    //Test 3, get subtask by id, this time using st1 again

    @Test
    public void getSubtaskByIdTest(){

    //first, create a subtask to retrieve
    String subtaskId =
    given()
        .spec(authorizedRequestSpecSubtask(token))
        .body(Map.of(
                "title", st1.getTitle(),
                "description", st1.getDescription(),
                "dueDate", st1.getDueDate().toString(),
                "isCompleted", false,
                "todo", Map.of("taskId", testTask.getTaskId().toString())
                )
            )
    .when()
        .post()
    .then()
        .statusCode(200)
        .extract().path("id");

    //now get it by id
    given()
        .spec(authorizedRequestSpecSubtask(token))
    .when()
        .get("/{subtaskId}", subtaskId)
    .then()
        .spec(responseSpecSubtask(200, true))
        .body("id", equalTo(subtaskId))
        .body("title", equalTo("practiceSubTask"))
        .body("description", equalTo("this is for testing"))
        .body("dueDate", equalTo(st1.getDueDate().toString()))
        .body("isCompleted", equalTo(false));
    }


    //Test 4, patch subtask by id using st2 just because
    @Test
    public void patchSubtaskTest(){

    //first, create a subtask to patch
    String subtaskId =
    given()
        .spec(authorizedRequestSpecSubtask(token))
        .body(Map.of(
                "title", st2.getTitle(),
                "description", st2.getDescription(),
                "dueDate", st2.getDueDate().toString(),
                "isCompleted", false,
                "todo", Map.of("taskId", testTask.getTaskId().toString())
                )
            )
    .when()
        .post()
    .then()
        .statusCode(200)
        .extract().path("id");

    //now patch it — update title and mark as completed
    given()
        .spec(authorizedRequestSpecSubtask(token))
        .body(Map.of(
                "title", "updatedSubTask",
                "isCompleted", true
                )
            )
    .when()
        .patch("/{subtaskId}", subtaskId)
    .then()
        .spec(responseSpecSubtask(200, true))
        .body("id", equalTo(subtaskId))
        .body("title", equalTo("updatedSubTask"))
        .body("description", equalTo("this is also for testing"))
        .body("isCompleted", equalTo(true));
    }


    //Test 5, get all subtasks for a task
    @Test
    public void getAllSubtasksByTaskIdTest(){

    //create subtask 1
    String subtaskId1 =
    given()
        .spec(authorizedRequestSpecSubtask(token))
        .body(Map.of(
                "title", st1.getTitle(),
                "description", st1.getDescription(),
                "dueDate", st1.getDueDate().toString(),
                "isCompleted", false,
                "todo", Map.of("taskId", testTask.getTaskId().toString())
                )
            )
    .when()
        .post()
    .then()
        .statusCode(200)
        .extract().path("id");

    //create subtask 2
    String subtaskId2 =
    given()
        .spec(authorizedRequestSpecSubtask(token))
        .body(Map.of(
                "title", st2.getTitle(),
                "description", st2.getDescription(),
                "dueDate", st2.getDueDate().toString(),
                "isCompleted", false,
                "todo", Map.of("taskId", testTask.getTaskId().toString())
                )
            )
    .when()
        .post()
    .then()
        .statusCode(200)
        .extract().path("id");

    //get all subtasks for the task
    given()
        .spec(authorizedRequestSpecSubtask(token))
        .queryParam("taskId", testTask.getTaskId().toString())
    .when()
        .get()
    .then()
        .spec(responseSpecSubtask(200, true))
        .body("size()", greaterThanOrEqualTo(2))
        .body("title", hasItems("practiceSubTask", "practiceSubTask2"))
        .body("id", hasItems(subtaskId1, subtaskId2));
    }
}

//Further potential edge case testing here if necessary