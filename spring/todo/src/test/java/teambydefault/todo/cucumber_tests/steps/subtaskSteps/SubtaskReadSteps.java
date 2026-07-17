package teambydefault.todo.cucumber_tests.steps.subtaskSteps;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import teambydefault.todo.cucumber_tests.CucumberRunner;

/**
 * Cucumber step definitions for stRead.feature.
 * Uses Selenium + TodoPage/SubtaskPage POMs for browser-based subtask reading.
 */
public class SubtaskReadSteps {

    private final CucumberRunner runner;

    public SubtaskReadSteps(CucumberRunner runner) {
        this.runner = runner;
    }

    // ─── View Subtask List ────────────────────────────────────────────────────

    @When("There are one or more Subtasks submitted")
    public void there_are_one_or_more_subtasks_submitted() {
        // Ensure at least one subtask exists — create one if the list is empty
        if (!runner.todoPage.hasSubtasksDisplayed()) {
            runner.todoPage.clickAddSubtaskButton();
            runner.todoPage.fillAddSubtaskForm("Read Test Subtask", "Created for read test", "2027-12-31T23:59");
            runner.todoPage.clickSaveSubtaskButton();

            WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(5));
            wait.until(d -> runner.todoPage.hasSubtasksDisplayed());
        }
    }

    @Then("The subtasks should be viewable on the page")
    public void the_subtasks_should_be_viewable_on_the_page() {
        assertTrue(runner.todoPage.hasSubtasksDisplayed(),
                "Expected at least one subtask to be visible on the task page");
        assertFalse(runner.todoPage.getSubtaskItems().isEmpty(),
                "Expected subtask items list to not be empty");
    }

    // ─── View Specific Subtask ────────────────────────────────────────────────

    @When("The user clicks on a specific subtask")
    public void the_user_clicks_on_a_specific_subtask() {
        // Ensure there's a subtask to click
        if (!runner.todoPage.hasSubtasksDisplayed()) {
            runner.todoPage.clickAddSubtaskButton();
            runner.todoPage.fillAddSubtaskForm("Clickable Subtask", "For navigation test", "2027-12-31T23:59");
            runner.todoPage.clickSaveSubtaskButton();

            WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(5));
            wait.until(d -> runner.todoPage.hasSubtasksDisplayed());
        }

        runner.todoPage.clickFirstSubtask();
    }

    @Then("The user is taken to the subtask detail view")
    public void the_user_is_taken_to_the_subtask_detail_view() {
        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlMatches(".*/task/\\d+/subtask/\\d+.*"));
        assertTrue(runner.driver.getCurrentUrl().matches(".*/task/\\d+/subtask/\\d+.*"),
                "Expected URL to match subtask detail view pattern");
    }

    // ─── Unauthorized Subtask Access ──────────────────────────────────────────

    @When("The user tries to manually enter a subtask path to a subtask that is not theirs")
    public void the_user_tries_to_manually_enter_a_subtask_path_that_is_not_theirs() {
        // Navigate to a subtask ID that doesn't belong to this user
        runner.subtaskPage.open(999999, 999999);
    }

    @Then("The page will display the error message {string}")
    public void the_page_will_display_the_error_message(String expectedMessage) {
        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(5));
        wait.until(d -> runner.subtaskPage.isErrorDisplayed());

        String actualMessage = runner.subtaskPage.getErrorMessage();
        assertTrue(actualMessage.contains(expectedMessage),
                "Expected error message to contain '" + expectedMessage + "' but got '" + actualMessage + "'");
    }
}
