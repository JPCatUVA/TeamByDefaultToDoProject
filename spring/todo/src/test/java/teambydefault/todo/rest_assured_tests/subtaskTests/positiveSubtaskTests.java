package teambydefault.todo.rest_assured_tests.subtaskTests;

//Recommended imports from RESTAssured
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

//other imports
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Map;

public class positiveSubtaskTests extends baseSubtaskTest{
    

/////////////////////////////////////// SETUP STUFF ////////////////////////////
//orignally here, moved to baseSubtaskTest, for cleanliness
//////////////////////////////////////// TESTS /////////////////////////////////


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
        .spec(authorizedResponseSpecSubtask(token))
        .body("id", notNullValue())
        .body("title", equalTo("practiceSubTask"))
        .body("description", equalTo("this is for testing"))
        .body("dueDate", equalTo(LocalDateTime.MAX.toString()))
        .body("isCompleted", equalTo(false));
    }
}
