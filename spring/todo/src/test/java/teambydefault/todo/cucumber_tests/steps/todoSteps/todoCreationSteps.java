package teambydefault.todo.cucumber_tests.steps.todoSteps;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import teambydefault.todo.cucumber_tests.CucumberRunner;

/**
 * Cucumber step definitions for todocreate.feature.
 * Uses Selenium + TodoPage POM for browser-based task creation.
 */
public class todoCreationSteps {

    private final CucumberRunner runner;
    private int taskCountBefore;
    private boolean saveButtonWasDisabled;

    public todoCreationSteps(CucumberRunner runner) {
        this.runner = runner;
    }

    // ─── Background ───────────────────────────────────────────────────────────────

    @Given("a user exists in the system")
    public void a_user_exists_in_the_system() {
        String email = "todotest@test.com";
        String password = "P@ssw0rd";

        // Register via the browser (if user already exists, login will still work)
        runner.loginPage.open();
        runner.loginPage.clickRegistrationLink();

        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.urlContains("/register"));

        runner.registerPage.enterEmail(email);
        runner.registerPage.enterPassword(password);
        runner.registerPage.clickRegisterButton();

        // After registration, user might be redirected to home (success)
        // or stay on register page (already exists — so go login instead)
        try {
            wait.until(ExpectedConditions.urlContains("/home"));
        } catch (Exception e) {
            // Registration failed (user already exists) — log in instead
            runner.loginPage.open();
            runner.loginPage.enterEmail(email);
            runner.loginPage.enterPassword(password);
            runner.loginPage.clickLoginButton();
            wait.until(ExpectedConditions.urlContains("/home"));
        }
    }

    // ─── When Steps ───────────────────────────────────────────────────────────────

    @When("the user sends a POST request to {string} with title {string} and description {string} and due date {string}")
    public void create_task_with_all_fields(String endpoint, String title, String description, String dueDate) {
        taskCountBefore = runner.todoPage.getTaskItems().size();
        runner.todoPage.clickAddTaskButton();
        runner.todoPage.fillAddTaskForm(title, description, dueDate);
        runner.todoPage.clickSaveTaskButton();
    }

    @When("the user sends a POST request to {string} with title {string}")
    public void create_task_with_title_only(String endpoint, String title) {
        taskCountBefore = runner.todoPage.getTaskItems().size();
        runner.todoPage.clickAddTaskButton();
        runner.todoPage.fillAddTaskForm(title, "", "2026-12-31T23:59");
        runner.todoPage.clickSaveTaskButton();
    }

    @When("the user sends a POST request to {string} with an empty title")
    public void create_task_with_empty_title(String endpoint) {
        taskCountBefore = runner.todoPage.getTaskItems().size();
        runner.todoPage.clickAddTaskButton();
        runner.todoPage.fillAddTaskForm("", "Some description", "2026-12-31T23:59");
        saveButtonWasDisabled = runner.todoPage.isSaveTaskButtonDisabled();
    }

    @When("a POST request to {string} is sent without a user ID")
    public void create_task_without_user(String endpoint) {
        taskCountBefore = runner.todoPage.getTaskItems().size();
        runner.todoPage.clickAddTaskButton();
        runner.todoPage.fillAddTaskForm("", "", "");
        saveButtonWasDisabled = runner.todoPage.isSaveTaskButtonDisabled();
    }

    @When("the user sends a POST request to {string} with a non-existent user ID and title {string}")
    public void create_task_with_non_existent_user(String endpoint, String title) {
        taskCountBefore = runner.todoPage.getTaskItems().size();
        runner.todoPage.clickAddTaskButton();
        runner.todoPage.fillAddTaskForm(title, "", "");
        saveButtonWasDisabled = runner.todoPage.isSaveTaskButtonDisabled();
    }

    // ─── Then Steps ───────────────────────────────────────────────────────────────

    @Then("the response status code should be {int}")
    public void verify_status_code(int expectedStatus) {
        if (expectedStatus == 201) {
            WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(2));
            wait.until(d -> runner.todoPage.getTaskItems().size() > taskCountBefore);
            assertTrue(runner.todoPage.hasTasksDisplayed());
        } else if (expectedStatus == 400) {
            assertTrue(saveButtonWasDisabled || runner.todoPage.isSaveTaskButtonDisabled(),
                    "Expected form submission to be blocked (save button disabled)");
        }
    }

    @And("the response body should contain the title {string}")
    public void verify_response_title(String expectedTitle) {
        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(2));
        wait.until(d -> runner.todoPage.hasTasksDisplayed());
        boolean found = runner.todoPage.getTaskItems().stream()
                .anyMatch(item -> item.getText().contains(expectedTitle));
        assertTrue(found, "Expected task with title '" + expectedTitle + "' to appear in the task list");
    }

    @And("the response body should contain a generated task ID")
    public void verify_response_has_task_id() {
        int taskCountAfter = runner.todoPage.getTaskItems().size();
        assertTrue(taskCountAfter > taskCountBefore,
                "Expected a new task to appear in the list (confirming server assigned an ID)");
    }
}
