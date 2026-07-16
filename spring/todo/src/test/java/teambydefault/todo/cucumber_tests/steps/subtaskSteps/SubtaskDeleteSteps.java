package teambydefault.todo.cucumber_tests.steps.subtaskSteps;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import teambydefault.todo.cucumber_tests.CucumberRunner;

/**
 * Cucumber step definitions for stDelete.feature (Subtask Delete).
 * Uses the TodoPage POM via the shared CucumberRunner.
 */
public class SubtaskDeleteSteps {

    private final CucumberRunner runner;
    private int subtaskCountBefore;

    public SubtaskDeleteSteps(CucumberRunner runner) {
        this.runner = runner;
    }

    // ── Background steps ─────────────────────────────────────────────────────
    // Shared login/home steps are defined in SubtaskCreateSteps.

    @And("There is a task submitted")
    public void there_is_a_task_submitted() {
        assertTrue(runner.todoPage.hasTasksDisplayed(),
                "Expected at least one task to be present on the home page");
    }

    @And("The user clicks on a valid task")
    public void the_user_clicks_on_a_valid_task() {
        runner.todoPage.clickFirstTask();
        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/task/"));
    }

    // ── Scenario: Delete a subtask ───────────────────────────────────────────

    @When("There are one or more Subtasks submitted")
    public void there_are_one_or_more_subtasks_submitted() {
        assertTrue(runner.todoPage.hasSubtasksDisplayed(),
                "Expected at least one subtask to be present");
        subtaskCountBefore = runner.todoPage.getSubtaskItems().size();
    }

    @And("The user clicks the Subtask's delete button")
    public void the_user_clicks_the_subtasks_delete_button() {
        runner.todoPage.clickDeleteFirstSubtask();
    }

    @Then("The task is removed from the list")
    public void the_task_is_removed_from_the_list() {
        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(10));
        wait.until(d -> runner.todoPage.getSubtaskItems().size() < subtaskCountBefore);
        int subtaskCountAfter = runner.todoPage.getSubtaskItems().size();
        assertTrue(subtaskCountAfter < subtaskCountBefore,
                "Expected subtask count to decrease after deletion");
    }

    // ── Scenario: Cannot delete when no subtasks present ─────────────────────

    @When("There are no Subtasks present")
    public void there_are_no_subtasks_present() {
        assertFalse(runner.todoPage.hasSubtasksDisplayed(),
                "Expected no subtasks to be present on the page");
    }

    @And("The user tries to click the delete button")
    public void the_user_tries_to_click_the_delete_button() {
        // Intentionally a no-op — we're verifying the button doesn't exist
    }

    @Then("The button is not found")
    public void the_button_is_not_found() {
        assertFalse(runner.todoPage.isSubtaskDeleteButtonPresent(),
                "Expected no subtask delete button to be present on the page");
    }
}
