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
 * Cucumber step definitions for stDelete.feature (Subtask Deletion).
 * Also contains the shared Background steps for task setup used across
 * all subtask features (Create, Read, Update, Delete).
 */
public class SubtaskDeleteSteps {

    private final CucumberRunner runner;
    private int subtaskCountBefore;

    public SubtaskDeleteSteps(CucumberRunner runner) {
        this.runner = runner;
    }

    // ── Shared Background steps (used by all subtask features) ───────────────

    @And("There is a task submitted")
    public void there_is_a_task_submitted() {
        runner.todoPage.clickAddTaskButton();
        runner.todoPage.fillAddTaskForm("Test Task", "A task for subtask testing", "2027-12-31T23:59");
        runner.todoPage.clickSaveTaskButton();
        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(2));
        wait.until(d -> runner.todoPage.hasTasksDisplayed());
    }

    @And("The user clicks on a valid task")
    public void the_user_clicks_on_a_valid_task() {
        runner.todoPage.clickFirstTask();
        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.urlContains("/task/"));
    }

    @Then("The user is on the corresponding task page")
    public void the_user_is_on_the_corresponding_task_page() {
        assertTrue(runner.driver.getCurrentUrl().contains("/task/"),
                "Expected URL to contain '/task/' indicating the task detail page");
    }

    // ── Scenario: Delete a subtask ───────────────────────────────────────────

    @When("There are one or more Subtasks submitted")
    public void there_are_one_or_more_subtasks_submitted() {
        // Create a subtask if none exist yet
        if (!runner.todoPage.hasSubtasksDisplayed()) {
            runner.todoPage.clickAddSubtaskButton();
            runner.todoPage.fillAddSubtaskForm("Setup Subtask", "Auto-created for test", "2027-12-31T23:59");
            runner.todoPage.clickSaveSubtaskButton();
            WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(2));
            wait.until(d -> runner.todoPage.hasSubtasksDisplayed());
        }
        subtaskCountBefore = runner.todoPage.getSubtaskItems().size();
    }

    @And("The user clicks the Subtask's delete button")
    public void the_user_clicks_the_subtasks_delete_button() {
        runner.todoPage.clickDeleteFirstSubtask();
    }

    @Then("The Subtask is removed from the list")
    public void the_subtask_is_removed_from_the_list() {
        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(2));
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
