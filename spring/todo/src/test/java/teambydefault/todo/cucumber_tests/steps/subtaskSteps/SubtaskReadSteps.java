package teambydefault.todo.cucumber_tests.steps.subtaskSteps;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

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
        if (!runner.todoPage.hasSubtasksDisplayed()) {
            runner.todoPage.clickAddSubtaskButton();
            runner.todoPage.fillAddSubtaskForm("Read Test Subtask", "Created for read test", "2027-12-31T23:59");
            runner.todoPage.clickSaveSubtaskButton();
            runner.todoPage.waitForSubtasksDisplayed();
        }
    }

    @Then("The subtasks should be viewable on the page")
    public void the_subtasks_should_be_viewable_on_the_page() {
        assertTrue(runner.todoPage.hasSubtasksDisplayed(),
                "Expected at least one subtask to be visible on the task page");
    }

    // ─── View Specific Subtask ────────────────────────────────────────────────

    @When("The user clicks on a specific subtask")
    public void the_user_clicks_on_a_specific_subtask() {
        if (!runner.todoPage.hasSubtasksDisplayed()) {
            runner.todoPage.clickAddSubtaskButton();
            runner.todoPage.fillAddSubtaskForm("Clickable Subtask", "For navigation test", "2027-12-31T23:59");
            runner.todoPage.clickSaveSubtaskButton();
            runner.todoPage.waitForSubtasksDisplayed();
        }
        runner.todoPage.clickFirstSubtask();
    }

    @Then("The user is taken to the subtask detail view")
    public void the_user_is_taken_to_the_subtask_detail_view() {
        runner.subtaskPage.waitForSubtaskDetailUrl();
        //assertTrue(runner.driver.getCurrentUrl().matches(".*/task/\\d+/subtask/\\d+.*"),
                //"Expected URL to match subtask detail view pattern");
    }

    // ─── Unauthorized Subtask Access ──────────────────────────────────────────

    @When("The user tries to manually enter a subtask path to a subtask that is not theirs")
    public void the_user_tries_to_manually_enter_a_subtask_path_that_is_not_theirs() {
        runner.subtaskPage.open(999999, 999999);
    }

    @Then("The page will display the error message {string}")
    public void the_page_will_display_the_error_message(String expectedMessage) {
        runner.subtaskPage.waitForErrorDisplayed();
        String actualMessage = runner.subtaskPage.getErrorMessage();
        assertTrue(actualMessage.contains(expectedMessage),
                "Expected error message to contain '" + expectedMessage + "' but got '" + actualMessage + "'");
    }
}
