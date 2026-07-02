package teambydefault.todo.rest_assured_tests;

import static io.restassured.RestAssured.*;
//import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import teambydefault.todo.entity.Todo;
import teambydefault.todo.entity.User;

import teambydefault.todo.repo.ToDoRepo;
import teambydefault.todo.repo.UserRepo;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")

public class RestAssuredTodoCreateEditDelete {
    @LocalServerPort
    private int port;

    @Autowired
    private ToDoRepo todoRepo;

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    void setup(){
        // we can specify where we want all of our requests to be sent
        RestAssured.baseURI = "http://localhost";
        // we can specify the port for the requests
        RestAssured.port = port;
    }

    @Test
    void createTodoTest() {
        // 1. Register a user
        User testUser = new User();
        testUser.setEmail("todocreator@test.com");
        testUser.setPassword("P@ssw0rd");

        given()
            .contentType(ContentType.JSON)
            .body(testUser)
        .when()
            .post("/register")
        .then()
            .statusCode(201);

        // 2. Login to get a JWT token
        String token =
            given()
                .contentType(ContentType.JSON)
                .body(testUser)
            .when()
                .post("/login")
            .then()
                .statusCode(200)
                .extract().body().asString();

        // 3. Get the user's ID from the database
        UUID userId = userRepo.findByEmail("todocreator@test.com").get().getUserId();

        // 4. Create a Todo via the API
        String requestBody = """
                {
                    "title": "Test Todo",
                    "description": "A test task",
                    "user": { "userId": "%s" }
                }
                """.formatted(userId);

        String taskId =
            given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
            .when()
                .post("/task")
            .then()
                .statusCode(201)
                .body("title", equalTo("Test Todo"))
                .body("description", equalTo("A test task"))
                .body("taskId", notNullValue())
                .extract().path("taskId");

        // 5. Verify the todo exists in the database
        Optional<Todo> savedTodo = todoRepo.findById(UUID.fromString(taskId));
        assertTrue(savedTodo.isPresent(), "Todo should be persisted in the database after creation");
        assertEquals("Test Todo", savedTodo.get().getTitle());
        assertEquals("A test task", savedTodo.get().getDescription());
        assertEquals(userId, savedTodo.get().getUser().getUserId());
    }

    @Test
    void editTodoTest(){
        // 1. Register a user
        User testUser = new User();
        testUser.setEmail("todoeditor@test.com");
        testUser.setPassword("P@ssw0rd");

        given()
            .contentType(ContentType.JSON)
            .body(testUser)
        .when()
            .post("/register")
        .then()
            .statusCode(201);

        // 2. Login to get a JWT token
        String token =
            given()
                .contentType(ContentType.JSON)
                .body(testUser)
            .when()
                .post("/login")
            .then()
                .statusCode(200)
                .extract().body().asString();

        // 3. Get the user's ID from the database
        UUID userId = userRepo.findByEmail("todoeditor@test.com").get().getUserId();

        // 4. Create a Todo via the API (need something to edit)
        String createBody = """
                {
                    "title": "Original Title",
                    "description": "Original description",
                    "user": { "userId": "%s" }
                }
                """.formatted(userId);

        String taskId =
            given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(createBody)
            .when()
                .post("/task")
            .then()
                .statusCode(201)
                .extract().path("taskId");

        // 5. PATCH the Todo with updated fields
        String patchBody = """
                {
                    "title": "Updated Title",
                    "description": "Updated description",
                    "isCompleted": true
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(patchBody)
        .when()
            .patch("/task/" + taskId)
        .then()
            .statusCode(200)
            .body("title", equalTo("Updated Title"))
            .body("description", equalTo("Updated description"))
            .body("isCompleted", equalTo(true));

        // 6. Verify the update persisted in the database
        Optional<Todo> updatedTodo = todoRepo.findById(UUID.fromString(taskId));
        assertTrue(updatedTodo.isPresent(), "Todo should still exist after editing");
        assertEquals("Updated Title", updatedTodo.get().getTitle());
        assertEquals("Updated description", updatedTodo.get().getDescription());
        assertTrue(updatedTodo.get().getIsCompleted(), "isCompleted should be true after patch");
        assertEquals(userId, updatedTodo.get().getUser().getUserId(), "User association should remain unchanged");
    }

    @Test
    void deleteTodoTest() {
        // 1. Register a user
        User testUser = new User();
        testUser.setEmail("tododelete@test.com");
        testUser.setPassword("P@ssw0rd");

        given()
            .contentType(ContentType.JSON)
            .body(testUser)
        .when()
            .post("/register")
        .then()
            .statusCode(201);

        // 2. Login to get a JWT token
        String token =
            given()
                .contentType(ContentType.JSON)
                .body(testUser)
            .when()
                .post("/login")
            .then()
                .statusCode(200)
                .extract().body().asString();

        // 3. Get the user's ID from the database
        UUID userId = userRepo.findByEmail("tododelete@test.com").get().getUserId();

        // 4. Create a Todo via the API (need something to delete)
        String createBody = """
                {
                    "title": "Original Title",
                    "description": "Original description",
                    "user": { "userId": "%s" }
                }
                """.formatted(userId);

        String taskId =
            given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(createBody)
            .when()
                .post("/task")
            .then()
                .statusCode(201)
                .extract().path("taskId");

        //5. Delete the created Todo
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .delete("/task/" + taskId)
        .then()
            .statusCode(204);

        //6. Confirm Todo is gone
        Optional<Todo> deletedTodo = todoRepo.findById(UUID.fromString(taskId));
        assertFalse(deletedTodo.isPresent(), "Todo should no longer exist after deletion");

    }

    @Test
    void createTodoNoTitle() {
        // 1. Register and login to get a valid token
        User testUser = new User();
        testUser.setEmail("notitle@test.com");
        testUser.setPassword("P@ssw0rd");

        given()
            .contentType(ContentType.JSON)
            .body(testUser)
        .when()
            .post("/register")
        .then()
            .statusCode(201);

        String token =
            given()
                .contentType(ContentType.JSON)
                .body(testUser)
            .when()
                .post("/login")
            .then()
                .statusCode(200)
                .extract().body().asString();

        UUID userId = userRepo.findByEmail("notitle@test.com").get().getUserId();

        // 2. Attempt to create a Todo without a title
        String requestBody = """
                {
                    "description": "No title provided",
                    "user": { "userId": "%s" }
                }
                """.formatted(userId);

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(requestBody)
        .when()
            .post("/task")
        .then()
            .statusCode(400);
    }

    @Test
    void createTodoBadToken() {
        // Attempt to create a Todo using a completely invalid token
        String requestBody = """
                {
                    "title": "Should Not Work",
                    "description": "This request uses a bad token",
                    "user": { "userId": "00000000-0000-0000-0000-000000000000" }
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer this.is.not.a.valid.jwt")
            .body(requestBody)
        .when()
            .post("/task")
        .then()
            .statusCode(401);
    }
}
