package teambydefault.todo.cucumber_tests.steps.todoSteps;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import teambydefault.todo.cucumber_tests.CucumberRunner;

/**
 * Cucumber step definitions for todoedit.feature.
 * Uses Selenium + TodoPage POM for browser-based task editing.
 */
public class todoEditSteps {

    private final CucumberRunner runner;
    private String editedFieldLabel;

    public todoEditSteps(CucumberRunner runner) {
        this.runner = runner;
    }

    // ─── Edit Scenario Outline Steps ──────────────────────────────────────────

    @When("The user clicks on the edit button of a task field called {string}")
    public void the_user_clicks_on_the_edit_button_of_a_task_field_called(String field) {
        editedFieldLabel = field;
        runner.todoPage.clickEditButton(editedFieldLabel);
    }

    @And("The user enters {string} into the task editing box")
    public void the_user_enters_text_into_the_task_editing_box(String text) {
        if ("Description".equals(editedFieldLabel)) {
            runner.todoPage.enterEditTextarea(text);
        } else {
            runner.todoPage.enterEditText(text);
        }
    }

    @And("The user enters {string} into the task date editing box")
    public void the_user_enters_date_into_the_task_date_editing_box(String date) {
        runner.todoPage.enterEditDueDate(date);
    }

    @And("The user clicks the task save button")
    public void the_user_clicks_the_task_save_button() {
        // For Status field, toggle the checkbox before saving
        if ("Status".equals(editedFieldLabel)) {
            runner.todoPage.toggleCompletedCheckbox();
        }
        runner.todoPage.clickEditSaveButton();
    }

    @Then("The task field should now show {string} as updated")
    public void the_task_field_should_now_show_text_as_updated(String expectedValue) {
        // Wait for the edit row to close (save completed)
        runner.todoPage.waitForEditRowHidden();

        String actualValue = runner.todoPage.getFieldValue(editedFieldLabel);
        assertEquals(expectedValue, actualValue,
                "Expected field '" + editedFieldLabel + "' to show '" + expectedValue + "'");
    }

    // ─── Due Date Update ──────────────────────────────────────────────────────

    @Then("The task due date field should be updated")
    public void the_task_due_date_field_should_be_updated() {
        runner.todoPage.waitForEditRowHidden();

        String actual = runner.todoPage.getFieldValue("Due Date");
        assertFalse(actual.isEmpty(), "Expected the due date field to have a value after update");
    }

    // ─── Mark as Completed ────────────────────────────────────────────────────

    @Then("The task status field should show completed")
    public void the_task_status_field_should_show_completed() {
        runner.todoPage.waitForEditRowHidden();

        String actual = runner.todoPage.getFieldValue("Status");
        assertTrue(actual.contains("Completed"),
                "Expected the status field to show 'Completed'");
    }

    // ─── Invalid Edit (Blank Title) ───────────────────────────────────────────

    @Then("The task save button is invalid")
    public void the_task_save_button_is_invalid() {
        assertTrue(runner.todoPage.isEditSaveButtonDisabled(),
                "Expected Save button to be disabled when required field is blank");
    }

    // ─── Non-existent Task ────────────────────────────────────────────────────

    @When("The user tries to manually enter a task path to a task that does not exist")
    public void the_user_tries_to_manually_enter_a_task_path_that_does_not_exist() {
        runner.todoPage.openTask(99999999);
    }

    @Then("The page will display a task error message")
    public void the_page_will_display_a_task_error_message() {
        runner.todoPage.waitForErrorDisplayed();

        String errorMessage = runner.todoPage.getErrorMessage();
        assertFalse(errorMessage.isEmpty(),
                "Expected an error message to be displayed for non-existent task");
    }
}
