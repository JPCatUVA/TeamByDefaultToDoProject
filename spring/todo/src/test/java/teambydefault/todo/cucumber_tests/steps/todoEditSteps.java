package teambydefault.todo.cucumber_tests.steps;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import teambydefault.todo.repo.UserRepo;

public class todoEditSteps {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepo userRepo;

    private Response response;
    private String jwtToken;
    private String userId;
    private String taskId;

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    // ─── Background ───────────────────────────────────────────────────────────────

    @Given("an authenticated user with an existing todo")
    public void anAuthenticatedUserWithAnExistingTodo() {
        // Register a test user
        String userJson = """
                {
                    "email": "edit-test@cucumber.com",
                    "password": "P@ssw0rd"
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/register")
        .then()
            .statusCode(201);

        // Log in to obtain a JWT token
        Response loginResponse = given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/login");

        loginResponse.then().statusCode(200);
        jwtToken = loginResponse.getBody().asString();

        // Look up the user ID from the database
        userId = userRepo.findByEmail("edit-test@cucumber.com")
                .orElseThrow()
                .getUserId()
                .toString();

        // Create a todo to edit in subsequent steps
        String todoBody = """
                {
                    "title": "Original Title",
                    "description": "Original description",
                    "dueDate": "2026-09-01T12:00:00",
                    "user": { "userId": "%s" }
                }
                """.formatted(userId);

        Response createResponse = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + jwtToken)
            .body(todoBody)
        .when()
            .post("/task");

        createResponse.then().statusCode(201);
        taskId = createResponse.jsonPath().getString("taskId");
    }

    // ─── When Steps ───────────────────────────────────────────────────────────────

    @When("the user sends a PATCH request to the todo with title {string}")
    public void patchTodoTitle(String title) {
        String body = """
                {
                    "title": "%s"
                }
                """.formatted(title);

        response = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + jwtToken)
            .body(body)
        .when()
            .patch("/task/" + taskId);
    }

    @When("the user sends a PATCH request to the todo with description {string}")
    public void patchTodoDescription(String description) {
        String body = """
                {
                    "description": "%s"
                }
                """.formatted(description);

        response = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + jwtToken)
            .body(body)
        .when()
            .patch("/task/" + taskId);
    }

    @When("the user sends a PATCH request to the todo with due date {string}")
    public void patchTodoDueDate(String dueDate) {
        String body = """
                {
                    "dueDate": "%s"
                }
                """.formatted(dueDate);

        response = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + jwtToken)
            .body(body)
        .when()
            .patch("/task/" + taskId);
    }

    @When("the user sends a PATCH request to the todo with isCompleted true")
    public void patchTodoCompleted() {
        String body = """
                {
                    "isCompleted": true
                }
                """;

        response = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + jwtToken)
            .body(body)
        .when()
            .patch("/task/" + taskId);
    }

    @When("the user sends a PATCH request to the todo with title {string} and description {string}")
    public void patchTodoTitleAndDescription(String title, String description) {
        String body = """
                {
                    "title": "%s",
                    "description": "%s"
                }
                """.formatted(title, description);

        response = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + jwtToken)
            .body(body)
        .when()
            .patch("/task/" + taskId);
    }

    @When("the user sends a PATCH request to a non-existent todo with title {string}")
    public void patchNonExistentTodo(String title) {
        String fakeTaskId = UUID.randomUUID().toString();
        String body = """
                {
                    "title": "%s"
                }
                """.formatted(title);

        response = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + jwtToken)
            .body(body)
        .when()
            .patch("/task/" + fakeTaskId);
    }

    // ─── Then Steps ───────────────────────────────────────────────────────────────

    @Then("the edit response status code should be {int}")
    public void verifyEditStatusCode(int expectedStatus) {
        response.then().statusCode(expectedStatus);
    }

    @And("the edit response body should contain the title {string}")
    public void verifyEditResponseTitle(String expectedTitle) {
        response.then().body("title", equalTo(expectedTitle));
    }

    @And("the edit response body should contain the description {string}")
    public void verifyEditResponseDescription(String expectedDescription) {
        response.then().body("description", equalTo(expectedDescription));
    }

    @And("the edit response body should contain the due date {string}")
    public void verifyEditResponseDueDate(String expectedDueDate) {
        response.then().body("dueDate", equalTo(expectedDueDate));
    }

    @And("the edit response body should have isCompleted set to true")
    public void verifyEditResponseIsCompleted() {
        response.then().body("isCompleted", equalTo(true));
    }
}
