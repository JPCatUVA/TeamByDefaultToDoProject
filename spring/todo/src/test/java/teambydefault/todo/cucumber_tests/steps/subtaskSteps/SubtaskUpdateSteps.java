package teambydefault.todo.cucumber_tests.steps.subtaskSteps;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import teambydefault.todo.cucumber_tests.CucumberRunner;

/**
 * Cucumber step definitions for stUpdate.feature (Subtask Update).
 * Uses the SubtaskPage POM via the shared CucumberRunner.
 */
public class SubtaskUpdateSteps {

    private final CucumberRunner runner;

    public SubtaskUpdateSteps(CucumberRunner runner) {
        this.runner = runner;
    }

    // ── Background steps ─────────────────────────────────────────────────────
    // Shared steps (login, home, task submitted, clicks on valid task) are
    // defined in SubtaskCreateSteps and SubtaskDeleteSteps.

    @And("There is a subtask submitted")
    public void there_is_a_subtask_submitted() {
        assertTrue(runner.todoPage.hasSubtasksDisplayed(),
                "Expected at least one subtask to be present under the current task");
    }

    @And("The user clicks on a valid subtask")
    public void the_user_clicks_on_a_valid_subtask() {
        runner.todoPage.clickFirstSubtask();
        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(10));
        wait.until(d -> runner.driver.getCurrentUrl().contains("/subtask/"));
    }

    // ── Scenario: Edit a subtask field ───────────────────────────────────────

    @When("The user clicks on the edit button of a field")
    public void the_user_clicks_on_the_edit_button_of_a_field() {
        // Default to editing the Title field
        runner.subtaskPage.clickEditButton("Title");
    }

    @And("The user enters {string} into the editing box")
    public void the_user_enters_text_into_the_editing_box(String text) {
        if (text.isEmpty()) {
            // Clear the field to simulate an empty/blank title
            runner.subtaskPage.enterEditText("");
        } else {
            runner.subtaskPage.enterEditText(text);
        }
    }

    @And("The user clicks the save button")
    public void the_user_clicks_the_save_button() {
        runner.subtaskPage.clickSaveButton();
    }

    @Then("The field should now show {string} as updated")
    public void the_field_should_now_show_text_as_updated(String expectedText) {
        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(10));
        wait.until(d -> !runner.subtaskPage.isEditRowVisible());
        String actual = runner.subtaskPage.getFieldValue("Title");
        assertEquals(expectedText, actual,
                "Expected the title field to display the updated text");
    }

    // ── Scenario: Cannot save blank required title ───────────────────────────

    @Then("The save button is invalid")
    public void the_save_button_is_invalid() {
        assertTrue(runner.subtaskPage.isSaveButtonDisabled(),
                "Expected the Save button to be disabled when a required field is blank");
    }
}
