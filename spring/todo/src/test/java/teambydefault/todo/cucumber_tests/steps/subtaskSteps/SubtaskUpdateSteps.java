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
 * Background steps are shared from SubtaskCreateSteps (login) and
 * SubtaskDeleteSteps (task setup).
 */
public class SubtaskUpdateSteps {

    private final CucumberRunner runner;

    public SubtaskUpdateSteps(CucumberRunner runner) {
        this.runner = runner;
    }

    // ── Shared Background steps for Update ───────────────────────────────────

    @And("There is a subtask submitted")
    public void there_is_a_subtask_submitted() {
        assertTrue(runner.todoPage.hasSubtasksDisplayed(),
                "Expected at least one subtask to be present under the current task");
    }

    @And("The user clicks on a valid subtask")
    public void the_user_clicks_on_a_valid_subtask() {
        runner.todoPage.clickFirstSubtask();
        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(2));
        wait.until(d -> runner.driver.getCurrentUrl().contains("/subtask/"));
    }

    @Then("The user is on the corresponding Subtask page")
    public void the_user_is_on_the_corresponding_subtask_page() {
        assertTrue(runner.driver.getCurrentUrl().contains("/subtask/"),
                "Expected URL to contain '/subtask/' indicating the subtask detail page");
    }

    // ── Scenario: Edit a subtask field ───────────────────────────────────────

    @When("The user clicks on the edit button of a field")
    public void the_user_clicks_on_the_edit_button_of_a_field() {
        runner.subtaskPage.clickEditButton("Title");
    }

    @And("The user enters {string} into the editing box")
    public void the_user_enters_text_into_the_editing_box(String text) {
        if (text.isEmpty()) {
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
        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(2));
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
