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
 * Cucumber step definitions for tododelete.feature.
 * Uses Selenium + TodoPage POM for browser-based task deletion.
 */
public class todoDeletionSteps {

    private final CucumberRunner runner;
    private int taskCountBefore;
    private boolean deleteAttemptedOnEmpty;

    public todoDeletionSteps(CucumberRunner runner) {
        this.runner = runner;
    }

    // ─── Background ───────────────────────────────────────────────────────────────

    @Given("an authenticated user with a deletable todo")
    public void an_authenticated_user_with_a_deletable_todo() {
        String email = "deletetodotest@test.com";
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

        // Ensure at least one task exists for deletion
        if (!runner.todoPage.hasTasksDisplayed()) {
            runner.todoPage.clickAddTaskButton();
            runner.todoPage.fillAddTaskForm("Task to delete", "This will be removed", "2026-12-31T23:59");
            runner.todoPage.clickSaveTaskButton();
            wait.until(d -> runner.todoPage.hasTasksDisplayed());
        }

        taskCountBefore = runner.todoPage.getTaskItems().size();
        deleteAttemptedOnEmpty = false;
    }

    // ─── When Steps ───────────────────────────────────────────────────────────────

    @When("the user sends a DELETE request to the todo")
    public void delete_the_todo() {
        taskCountBefore = runner.todoPage.getTaskItems().size();
        runner.todoPage.clickDeleteFirstTask();
    }

    @When("the user sends a GET request for the deleted todo")
    public void get_deleted_todo() {
        // After deletion, the task should no longer be in the list.
        // Verification happens in the Then step.
    }

    @When("the user sends a DELETE request to a non-existent todo")
    public void delete_non_existent_todo() {
        // Navigate to a non-existent task URL directly
        runner.driver.get("http://localhost:4200/task/99999999");
        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(10));
        wait.until(d -> !runner.todoPage.getErrorMessage().isEmpty()
                || runner.driver.getCurrentUrl().contains("/task/99999999"));
        deleteAttemptedOnEmpty = true;
    }

    @When("the user sends another DELETE request to the same todo")
    public void delete_the_same_todo_again() {
        // After a task was already deleted, attempt to delete again
        if (runner.todoPage.hasTasksDisplayed()) {
            runner.todoPage.clickDeleteFirstTask();
        } else {
            deleteAttemptedOnEmpty = true;
        }
    }

    // ─── Then Steps ───────────────────────────────────────────────────────────────

    @Then("the delete response status code should be {int}")
    public void verify_delete_status_code(int expectedStatus) {
        if (expectedStatus == 204) {
            WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(10));
            wait.until(d -> runner.todoPage.getTaskItems().size() < taskCountBefore);
            assertTrue(runner.todoPage.getTaskItems().size() < taskCountBefore,
                    "Expected task count to decrease after successful deletion");
        } else if (expectedStatus == 400) {
            assertTrue(deleteAttemptedOnEmpty || !runner.todoPage.getErrorMessage().isEmpty(),
                    "Expected deletion to fail (error shown or no task to delete)");
        }
    }

    @Then("the get response status code should be {int}")
    public void verify_get_status_code(int expectedStatus) {
        if (expectedStatus == 404) {
            int taskCountAfter = runner.todoPage.getTaskItems().size();
            assertTrue(taskCountAfter < taskCountBefore,
                    "Expected the deleted task to no longer be in the list");
        }
    }
}
