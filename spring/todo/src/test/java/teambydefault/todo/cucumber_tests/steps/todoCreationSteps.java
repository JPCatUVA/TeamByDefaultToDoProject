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

public class todoCreationSteps {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepo userRepo;

    private Response response;
    private String jwtToken;
    private String userId;

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    // ─── Background ───────────────────────────────────────────────────────────────

    @Given("a user exists in the system")
    public void aUserExistsInTheSystem() {
        // Register a test user
        String userJson = """
                {
                    "email": "cucumber@test.com",
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
        userId = userRepo.findByEmail("cucumber@test.com")
                .orElseThrow()
                .getUserId()
                .toString();
    }

    // ─── When Steps ───────────────────────────────────────────────────────────────

    @When("the user sends a POST request to {string} with title {string} and description {string} and due date {string}")
    public void createTaskWithAllFields(String endpoint, String title, String description, String dueDate) {
        String body = """
                {
                    "title": "%s",
                    "description": "%s",
                    "dueDate": "%s",
                    "user": { "userId": "%s" }
                }
                """.formatted(title, description, dueDate, userId);

        response = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + jwtToken)
            .body(body)
        .when()
            .post(endpoint);
    }

    @When("the user sends a POST request to {string} with title {string}")
    public void createTaskWithTitleOnly(String endpoint, String title) {
        String body = """
                {
                    "title": "%s",
                    "user": { "userId": "%s" }
                }
                """.formatted(title, userId);

        response = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + jwtToken)
            .body(body)
        .when()
            .post(endpoint);
    }

    @When("the user sends a POST request to {string} with an empty title")
    public void createTaskWithEmptyTitle(String endpoint) {
        String body = """
                {
                    "title": "",
                    "user": { "userId": "%s" }
                }
                """.formatted(userId);

        response = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + jwtToken)
            .body(body)
        .when()
            .post(endpoint);
    }

    @When("a POST request to {string} is sent without a user ID")
    public void createTaskWithoutUser(String endpoint) {
        String body = """
                {
                    "title": "Some task"
                }
                """;

        response = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + jwtToken)
            .body(body)
        .when()
            .post(endpoint);
    }

    @When("the user sends a POST request to {string} with a non-existent user ID and title {string}")
    public void createTaskWithNonExistentUser(String endpoint, String title) {
        String fakeUserId = UUID.randomUUID().toString();
        String body = """
                {
                    "title": "%s",
                    "user": { "userId": "%s" }
                }
                """.formatted(title, fakeUserId);

        response = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + jwtToken)
            .body(body)
        .when()
            .post(endpoint);
    }

    // ─── Then Steps ───────────────────────────────────────────────────────────────

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int expectedStatus) {
        response.then().statusCode(expectedStatus);
    }

    @And("the response body should contain the title {string}")
    public void verifyResponseTitle(String expectedTitle) {
        response.then().body("title", equalTo(expectedTitle));
    }

    @And("the response body should contain a generated task ID")
    public void verifyResponseHasTaskId() {
        response.then().body("taskId", notNullValue());
    }
}
