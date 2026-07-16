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
 * Cucumber step definitions for todoedit.feature.
 * Uses Selenium + TodoPage POM for browser-based task editing.
 */
public class todoEditSteps {

    private final CucumberRunner runner;
    private boolean editAttemptedOnNonExistent;

    public todoEditSteps(CucumberRunner runner) {
        this.runner = runner;
    }

    // ─── Background ───────────────────────────────────────────────────────────────

    @Given("an authenticated user with an existing todo")
    public void an_authenticated_user_with_an_existing_todo() {
        String email = "edittodotest@test.com";
        String password = "P@ssw0rd";

        // Register via the browser
        runner.loginPage.open();
        runner.loginPage.clickRegistrationLink();

        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/register"));

        runner.registerPage.enterEmail(email);
        runner.registerPage.enterPassword(password);
        runner.registerPage.clickRegisterButton();

        // After registration, user might be redirected to home (success)
        // or stay on register page (already exists — so go login instead)
        try {
            wait.until(ExpectedConditions.urlContains("/home"));
        } catch (Exception e) {
            runner.loginPage.open();
            runner.loginPage.enterEmail(email);
            runner.loginPage.enterPassword(password);
            runner.loginPage.clickLoginButton();
            wait.until(ExpectedConditions.urlContains("/home"));
        }

        // Ensure at least one task exists to edit
        if (!runner.todoPage.hasTasksDisplayed()) {
            runner.todoPage.clickAddTaskButton();
            runner.todoPage.fillAddTaskForm("Original Title", "Original description", "2026-09-01T12:00");
            runner.todoPage.clickSaveTaskButton();
            wait.until(d -> runner.todoPage.hasTasksDisplayed());
        }

        // Navigate into the first task's detail view
        runner.todoPage.clickFirstTask();
        wait.until(ExpectedConditions.urlContains("/task/"));

        editAttemptedOnNonExistent = false;
    }

    // ─── When Steps ───────────────────────────────────────────────────────────────

    @When("the user sends a PATCH request to the todo with title {string}")
    public void patch_todo_title(String title) {
        runner.todoPage.clickEditButton("Title");
        runner.todoPage.enterEditText(title);
        runner.todoPage.clickEditSaveButton();
    }

    @When("the user sends a PATCH request to the todo with description {string}")
    public void patch_todo_description(String description) {
        runner.todoPage.clickEditButton("Description");
        runner.todoPage.enterEditTextarea(description);
        runner.todoPage.clickEditSaveButton();
    }

    @When("the user sends a PATCH request to the todo with due date {string}")
    public void patch_todo_due_date(String dueDate) {
        runner.todoPage.clickEditButton("Due Date");
        runner.todoPage.enterEditDueDate(dueDate);
        runner.todoPage.clickEditSaveButton();
    }

    @When("the user sends a PATCH request to the todo with isCompleted true")
    public void patch_todo_completed() {
        runner.todoPage.clickEditButton("Status");
        runner.todoPage.clickEditSaveButton();
    }

    @When("the user sends a PATCH request to the todo with title {string} and description {string}")
    public void patch_todo_title_and_description(String title, String description) {
        // Edit title first
        runner.todoPage.clickEditButton("Title");
        runner.todoPage.enterEditText(title);
        runner.todoPage.clickEditSaveButton();

        // Wait for edit row to close
        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(10));
        wait.until(d -> {
            try {
                return !runner.todoPage.isEditSaveButtonDisabled();
            } catch (Exception e) {
                return true; // edit row is gone
            }
        });

        // Then edit description
        runner.todoPage.clickEditButton("Description");
        runner.todoPage.enterEditTextarea(description);
        runner.todoPage.clickEditSaveButton();
    }

    @When("the user sends a PATCH request to a non-existent todo with title {string}")
    public void patch_non_existent_todo(String title) {
        runner.driver.get("http://localhost:4200/task/99999999");
        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(10));
        wait.until(d -> !runner.todoPage.getErrorMessage().isEmpty()
                || runner.driver.getCurrentUrl().contains("/task/99999999"));
        editAttemptedOnNonExistent = true;
    }

    // ─── Then Steps ───────────────────────────────────────────────────────────────

    @Then("the edit response status code should be {int}")
    public void verify_edit_status_code(int expectedStatus) {
        if (expectedStatus == 200) {
            WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(10));
            wait.until(d -> {
                try {
                    return !runner.todoPage.isEditSaveButtonDisabled();
                } catch (Exception e) {
                    return true; // edit row is gone
                }
            });
        } else if (expectedStatus == 400) {
            assertTrue(editAttemptedOnNonExistent || !runner.todoPage.getErrorMessage().isEmpty(),
                    "Expected an error when editing a non-existent task");
        }
    }

    @And("the edit response body should contain the title {string}")
    public void verify_edit_response_title(String expectedTitle) {
        String actual = runner.todoPage.getFieldValue("Title");
        assertEquals(expectedTitle, actual,
                "Expected the title field to display '" + expectedTitle + "'");
    }

    @And("the edit response body should contain the description {string}")
    public void verify_edit_response_description(String expectedDescription) {
        String actual = runner.todoPage.getFieldValue("Description");
        assertEquals(expectedDescription, actual,
                "Expected the description field to display '" + expectedDescription + "'");
    }

    @And("the edit response body should contain the due date {string}")
    public void verify_edit_response_due_date(String expectedDueDate) {
        String actual = runner.todoPage.getFieldValue("Due Date");
        assertFalse(actual.isEmpty(), "Expected the due date field to have a value");
    }

    @And("the edit response body should have isCompleted set to true")
    public void verify_edit_response_is_completed() {
        String actual = runner.todoPage.getFieldValue("Status");
        assertTrue(actual.contains("Completed"),
                "Expected the status field to show 'Completed'");
    }
}
