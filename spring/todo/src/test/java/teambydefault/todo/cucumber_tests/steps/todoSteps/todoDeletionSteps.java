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

public class todoDeletionSteps {

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

    @Given("an authenticated user with a deletable todo")
    public void anAuthenticatedUserWithADeletableTodo() {
        String email = "cucumber-delete-" + UUID.randomUUID() + "@test.com";

        String userJson = """
                {
                    "email": "%s",
                    "password": "P@ssw0rd"
                }
                """.formatted(email);

        // Register
        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/register")
        .then()
            .statusCode(201);

        // Login
        Response loginResponse = given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/login");

        loginResponse.then().statusCode(200);
        jwtToken = loginResponse.getBody().asString();

        // Look up user ID
        userId = userRepo.findByEmail(email)
                .orElseThrow()
                .getUserId()
                .toString();

        // Create a todo to delete
        String todoBody = """
                {
                    "title": "Task to delete",
                    "description": "This will be removed",
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

    @When("the user sends a DELETE request to the todo")
    public void deleteTheTodo() {
        response = given()
            .header("Authorization", "Bearer " + jwtToken)
        .when()
            .delete("/task/" + taskId);
    }

    @When("the user sends a GET request for the deleted todo")
    public void getDeletedTodo() {
        response = given()
            .header("Authorization", "Bearer " + jwtToken)
        .when()
            .get("/task/" + taskId);
    }

    @When("the user sends a DELETE request to a non-existent todo")
    public void deleteNonExistentTodo() {
        String fakeTaskId = UUID.randomUUID().toString();
        response = given()
            .header("Authorization", "Bearer " + jwtToken)
        .when()
            .delete("/task/" + fakeTaskId);
    }

    @When("the user sends another DELETE request to the same todo")
    public void deleteTheSameTodoAgain() {
        response = given()
            .header("Authorization", "Bearer " + jwtToken)
        .when()
            .delete("/task/" + taskId);
    }

    // ─── Then Steps ───────────────────────────────────────────────────────────────

    @Then("the delete response status code should be {int}")
    public void verifyDeleteStatusCode(int expectedStatus) {
        response.then().statusCode(expectedStatus);
    }

    @Then("the get response status code should be {int}")
    public void verifyGetStatusCode(int expectedStatus) {
        response.then().statusCode(expectedStatus);
    }
}
