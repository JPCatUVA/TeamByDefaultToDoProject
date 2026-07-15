package teambydefault.todo.cucumber_tests.steps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import teambydefault.todo.cucumber_tests.CucumberRunner;

/**
 * Cucumber step definitions for login.feature.
 *
 * <p>WebDriver setup and teardown live in {@link CucumberRunner}. This class
 * receives the runner via constructor injection and delegates all browser
 * interaction to the shared POMs it owns.
 *
 * <p>Scenarios that assert an HTTP status code (e.g. 401) call the backend
 * /login endpoint directly via REST Assured rather than scraping from the browser.
 */
public class LoginSteps {

    private final CucumberRunner runner;

    /** Stores the last HTTP response from a direct API call (for status code assertions). */
    private Response lastApiResponse;

    public LoginSteps(CucumberRunner runner) {
        this.runner = runner;
    }

    // ── Background steps ─────────────────────────────────────────────────────

    /**
     * Seeds an account via the /register endpoint so it exists in the H2 test DB.
     * Idempotent — if the account already exists (409) we ignore it.
     */
    @Given("The account {string} {string} exists")
    public void theAccountExists(String email, String password) {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = runner.serverPort;

        int status = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}")
                .when()
                .post("/register")
                .then()
                .extract().statusCode();

        // 201 = created, 400 or 409 = already exists — all fine for this step.
        // This backend returns 400 (not 409) when the email is already registered,
        // so we accept 400 here to keep the Background idempotent across test runs.
        assertThat(status).isIn(201, 400, 409);
    }

    /** Navigate to the login page (simulates an unauthorized user hitting the site). */
    @When("The unauthorized user accesses the website")
    public void theUnauthorizedUserAccessesTheWebsite() {
        runner.loginPage.open();
    }

    // ── Scenario: valid credentials ──────────────────────────────────────────

    @And("The user enters the username {string} and password {string}")
    public void theUserEntersUsernameAndPassword(String email, String password) {
        runner.loginPage.enterEmail(email);
        runner.loginPage.enterPassword(password);
    }

    @And("The user clicks the login button")
    public void theUserClicksTheLoginButton() {
        runner.loginPage.clickLoginButton();
    }

    @Then("The user is redirected to their todo manager")
    public void theUserIsRedirectedToTheirTodoManager() {
        // After a successful login the Angular router navigates to /home
        assertThat(runner.loginPage.getCurrentUrl()).contains("/home");
    }

    // ── Scenario Outline: invalid credentials ────────────────────────────────

    @And("The user enters invalid credentials {string} and {string}")
    public void theUserEntersInvalidCredentials(String email, String password) {
        // Call the API directly so we can capture the HTTP status code.
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = runner.serverPort;

        lastApiResponse = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}")
                .when()
                .post("/login");
    }

    // "The user clicks the login button" is re-used by the Outline but the API
    // response was already captured above — the click step is a no-op here.

    @Then("The response should have a status code {int}")
    public void theResponseShouldHaveStatusCode(int expectedStatus) {
        assertThat(lastApiResponse).isNotNull();
        assertThat(lastApiResponse.statusCode()).isEqualTo(expectedStatus);
    }
}
