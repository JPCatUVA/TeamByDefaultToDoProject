package teambydefault.todo.rest_assured_tests.subtaskTests;

//Recommended imports from RESTAssured
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

//other imports
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import teambydefault.todo.entity.Subtask;
import teambydefault.todo.entity.Todo;
import teambydefault.todo.entity.User;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

//Unsure why this needs to be imported twice but otherwise 
//there is a problem with RestAssured.baseURI
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public abstract class baseSubtaskTest {
    
    @LocalServerPort
    protected int port;

    //add one user, one task, and two subtasks,
    //These java Objects were added when I was trying to to the RESTAssured tests
    //in the subclasses by letting Jackson automatially serialize them into JSONs.
    //It didn't work but I have left them in because I find it to still be a bit
    //more re-usable than writing all those Map.of string values
    protected Todo testTask = new Todo();
    protected User testUser = new User();
    protected Subtask st1 = new Subtask();
    protected Subtask st2 = new Subtask();
    protected String token;
    
    @BeforeEach
    //put initial values in task/subtasks/token
    void initializeDB() throws Exception{

        // Must configure REST Assured BEFORE making any HTTP calls
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        //create a token, this will also login a user
        //unique email per test so registration never conflicts
        String email = UUID.randomUUID().toString().substring(0, 8) + "@test.com";
        token = getAuthToken(email, "Password0!");

        //get userId. Because we dont return a user object in our API, and so can't 
        //get its userId, we need to decode it from the JWT token.
        //from KIRO:
        String[] parts = token.split("\\.");
        String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
        // payload is JSON like {"sub":"<uuid>","email":"...","iat":...,"exp":...}
        String userId = payload.split("\"sub\":\"")[1].split("\"")[0];


        //back to ME:
        //sets user and task internals   
        testUser.setUserId(UUID.fromString(userId));
        testTask.setUser(testUser);
        testTask.setTitle("BIG TITLE");

        //submit task to db, extract taskId
        String taskId = given()
                            .contentType(ContentType.JSON)
                            .header("Authorization", "Bearer " + token)
                            .body(testTask)
                        .when()
                            .post("/task")
                        .then()
                            .statusCode(201)
                            .extract().path("taskId");

        //add in taskId to task before linking to subtasks
        testTask.setTaskId(UUID.fromString(taskId));

        //set subtasks internals
        st1.setTitle("practiceSubTask");
        st1.setDescription("this is for testing");
        st1.setDueDate(LocalDateTime.of(1999, 12, 31, 23, 59, 59));
        st1.setIsCompleted(false);
        st1.setTodo(testTask);
        st2.setTitle("practiceSubTask2");
        st2.setDescription("this is also for testing");
        st2.setDueDate(LocalDateTime.of(1968, 5, 25, 14, 30, 45));
        st2.setIsCompleted(false);
        st2.setTodo(testTask);

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
    //all requests here have the same URI and port, should have Java objects
    //serialized into JSON content, and have an authorization header added
    protected RequestSpecification authorizedRequestSpecSubtask(String authToken){
        RequestSpecification rSpec = new RequestSpecBuilder()
                                            .setBaseUri("http://localhost/subtask")
                                            .setPort(port)
                                            .setContentType(ContentType.JSON)
                                            .addHeader("Authorization", "Bearer " + authToken)
                                            .setConfig(RestAssured.config)
                                            .build();

        return rSpec;
    }

    //reusable response spec — pass expected status code and whether JSON body is expected
    protected ResponseSpecification responseSpecSubtask(int expectedStatusCode, boolean expectJson){
        ResponseSpecBuilder rSpec = new ResponseSpecBuilder()
                                            .expectStatusCode(expectedStatusCode);

        if (expectJson) {
            rSpec.expectContentType(ContentType.JSON)
                   .expectHeader("Content-Type", containsString("application/json"));
        }

        return rSpec.build();
    }

    
    //Method to get the JWT token string, borrowed from project sample.
    //registers and logs in a user, then gets its token
    protected String getAuthToken(String email, String password){
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
}
