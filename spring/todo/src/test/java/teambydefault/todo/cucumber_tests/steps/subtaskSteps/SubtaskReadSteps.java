package teambydefault.todo.cucumber_tests.steps.subtaskSteps;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import teambydefault.todo.cucumber_tests.CucumberRunner;

/**
 * Cucumber step definitions for stRead.feature (Subtask Read).
 * Background steps are shared from SubtaskCreateSteps (login) and
 * SubtaskDeleteSteps (task setup).
 */
public class SubtaskReadSteps {

    private final CucumberRunner runner;

    public SubtaskReadSteps(CucumberRunner runner) {
        this.runner = runner;
    }

    // ── Scenario: View list of subtasks ──────────────────────────────────────

    // "There are one or more Subtasks submitted" is defined in SubtaskDeleteSteps.

    @Then("The subtasks should be viewable on the page")
    public void the_subtasks_should_be_viewable_on_the_page() {
        assertTrue(runner.todoPage.hasSubtasksDisplayed(),
                "Expected subtask list to be visible on the task detail page");
        assertFalse(runner.todoPage.getSubtaskItems().isEmpty(),
                "Expected at least one subtask item to be rendered");
    }

    // ── Scenario: View a specific subtask ────────────────────────────────────

    @When("The user clicks on a specific subtask")
    public void the_user_clicks_on_a_specific_subtask() {
        runner.todoPage.clickFirstSubtask();
    }

    @Then("The user is taken to the subtask detail view")
    public void the_user_is_taken_to_the_subtask_detail_view() {
        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.urlContains("/subtask/"));
        assertTrue(runner.driver.getCurrentUrl().contains("/subtask/"),
                "Expected URL to contain '/subtask/' indicating the subtask detail view");
    }

    // ── Scenario: Cannot view subtasks of others ─────────────────────────────

    @When("The user tries to manually enter a subtask path to a subtask that is not theirs")
    public void the_user_tries_to_manually_enter_a_subtask_path_to_a_subtask_that_is_not_theirs() {
        runner.subtaskPage.open(99999, 99999);
    }

    @Then("The page will display the error message {string}")
    public void the_page_will_display_the_error_message(String expectedMessage) {
        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(2));
        wait.until(d -> !runner.subtaskPage.getErrorMessage().isEmpty());
        String actual = runner.subtaskPage.getErrorMessage();
        assertEquals(expectedMessage, actual,
                "Expected error message to match");
    }
}
