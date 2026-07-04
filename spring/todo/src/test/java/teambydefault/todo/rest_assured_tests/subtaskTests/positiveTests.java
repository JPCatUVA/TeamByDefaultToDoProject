package teambydefault.todo.rest_assured_tests.subtaskTests;

//Recommended imports from RESTAssured
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

//other imports
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import teambydefault.todo.entity.Subtask;
import teambydefault.todo.entity.Todo;
import teambydefault.todo.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;


//Unsure why this needs to be imported twice but otherwise 
//there is a problem with RestAssured.baseURI
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class positiveTests {
    

/////////////////////////////////////// SETUP STUFF ////////////////////////////
    @LocalServerPort
    private int port;

    // String token = getAuthToken("email@test.com", "Password0!");
    //add one user, one task, and two subtasks
    private ArrayList<Subtask> subTaskList = new ArrayList<Subtask>();
    private Subtask st1 = new Subtask();
    private Subtask st2 = new Subtask();

    @BeforeAll
    void initializeDB(){
        //put initial values in subtasks

        st1.setTitle("practiceSubTask");
        st1.setDescription("this is for testing");
        st1.setDueDate(LocalDateTime.MAX);
        st1.setIsCompleted(false);

        
        st2.setTitle("practiceSubTask2");
        st2.setDescription("this is also for testing");
        st2.setDueDate(LocalDateTime.MIN);
        st2.setIsCompleted(false);

        subTaskList.add(st1);
        subTaskList.add(st2);        
        
    }


    @BeforeEach
    void setup(){
        RestAssured.baseURI = "http.localhost/subtask";
        RestAssured.port = port;
    }

    /**
    * Runs once AFTER ALL tests in a subclass finish. Resets REST Assured's static config so it
    * doesn't leak into other test classes.
    */
    @AfterAll
    static void tearDown() {
    RestAssured.reset();
    }

    //Request and Response specification set ups
    private RequestSpecification authorizedRequestSpec(String authToken){
        RequestSpecification rSpec = new RequestSpecBuilder()
                                            .setBaseUri("http://localhost")
                                            .setPort(port)
                                            .setContentType(ContentType.JSON)
                                            .addHeader("Authorization", "Bearer " + authToken)
                                            .build();

        return rSpec;
    }

    private ResponseSpecification authorizedResponseSpec(String authToken){
        ResponseSpecification rSpec = new ResponseSpecBuilder()
                                            .expectStatusCode(200)
                                            .expectContentType(ContentType.JSON)
                                            .build();

        return rSpec;
    }

    
    //Method to get the JWT token string, borrowed from project sample.
    //registers and logs in a user, then gets its token
    private String getAuthToken(String email, String password){
        given()
            .contentType(ContentType.JSON)
            .body(Map.of("email", email, "password", password))
        .when()
            .post("/register")
        .then()
            .statusCode(201);


        return given()
            .contentType(ContentType.JSON)
            .body(Map.of("email", email, "password", password))
            .when()
            .post("/login")
            .then()
            .statusCode(200)
            //our login endpoint returns the JWT in its body
            .extract().body().asString();
    }
//////////////////////////////////////// TESTS /////////////////////////////////

    // //Test 0 
    // @Test
    // public void getAllUserSubtasks(){
    //     String token = getAuthToken("email@test.com", "Password0!");
    //     Todo testTodo = new Todo();
    //     testTodo.setTaskId(UUID.randomUUID());
        

    //     given()
    //         .header("Authorization", "Bearer" + token)
    //         .queryParam("taskID", testTodo.getTaskId())
    //     .when()
    //         .get("/?taskId={taskId}")
    //     .then()
    //         .statusCode(200);
    // }

    //Test 1, adding a subTask
    @Test
    public void addSubtaskTest(){
        String token = getAuthToken("email@test.com", "Password0!");

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(subTaskList.get(0))
        .when()
            .post()
        .then()
            .statusCode(200)
            .body("title", equalTo("practiceSubTask"))
            .body("description", equalTo("this is for testing"))
            .body("dueDate", equalTo(LocalDateTime.MAX))
            .body("isCompleted", equalTo(false));
    }
}
