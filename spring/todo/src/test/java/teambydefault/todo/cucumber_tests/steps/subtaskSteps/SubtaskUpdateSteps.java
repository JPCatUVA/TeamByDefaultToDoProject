package teambydefault.todo.cucumber_tests.steps.subtaskSteps;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import teambydefault.todo.cucumber_tests.CucumberRunner;

/**
 * Cucumber step definitions for stUpdate.feature.
 * Uses Selenium + TodoPage/SubtaskPage POMs for browser-based subtask updating.
 */
public class SubtaskUpdateSteps {

    private final CucumberRunner runner;
    private String editedFieldLabel;

    public SubtaskUpdateSteps(CucumberRunner runner) {
        this.runner = runner;
    }

    // ─── Background Steps (unique to Update) ──────────────────────────────────

    @And("There is a subtask submitted")
    public void there_is_a_subtask_submitted() {
        if (!runner.todoPage.hasSubtasksDisplayed()) {
            runner.todoPage.clickAddSubtaskButton();
            runner.todoPage.fillAddSubtaskForm("Update Test Subtask", "Created for update test", "2027-12-31T23:59");
            runner.todoPage.clickSaveSubtaskButton();
            runner.todoPage.waitForSubtasksDisplayed();
        }
    }

    @And("The user clicks on a valid subtask")
    public void the_user_clicks_on_a_valid_subtask() {
        runner.todoPage.clickFirstSubtask();
    }

    @Then("The user is on the corresponding Subtask page")
    public void the_user_is_on_the_corresponding_subtask_page() {
        runner.subtaskPage.waitForSubtaskDetailUrl();
        //assertTrue(runner.driver.getCurrentUrl().matches(".*/task/\\d+/subtask/\\d+.*"),
                //"Expected URL to match subtask detail view pattern");
    }

    // ─── Edit Scenario Outline Steps ──────────────────────────────────────────

    @When("The user clicks on the edit button of a field called {string}")
    public void the_user_clicks_on_the_edit_button_of_a_field_called(String field) {
        editedFieldLabel = field;
        runner.subtaskPage.clickEditButton(editedFieldLabel);
    }

    @And("The user enters {string} into the editing box")
    public void the_user_enters_text_into_the_editing_box(String text) {
        if ("Description".equals(editedFieldLabel)) {
            runner.subtaskPage.enterEditTextarea(text);
        } else {
            runner.subtaskPage.enterEditText(text);
        }
    }

    @And("The user clicks the save button")
    public void the_user_clicks_the_save_button() {
        runner.subtaskPage.clickSaveButton();
    }

    @Then("The field should now show {string} as updated")
    public void the_field_should_now_show_text_as_updated(String expectedValue) {
        runner.subtaskPage.waitForEditRowHidden();
        String actualValue = runner.subtaskPage.getFieldValue(editedFieldLabel);
        assertEquals(expectedValue, actualValue,
                "Expected field '" + editedFieldLabel + "' to show '" + expectedValue + "'");
    }

    // ─── Invalid Edit (Blank Title) Steps ─────────────────────────────────────

    @When("The user clicks on the edit button of a the title")
    public void the_user_clicks_on_the_edit_button_of_the_title() {
        editedFieldLabel = "Title";
        runner.subtaskPage.clickEditButton(editedFieldLabel);
    }

    @Then("The save button is invalid")
    public void the_save_button_is_invalid() {
        assertTrue(runner.subtaskPage.isSaveButtonDisabled(),
                "Expected Save button to be disabled when required field is blank");
    }
}
