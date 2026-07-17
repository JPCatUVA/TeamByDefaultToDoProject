package teambydefault.todo.cucumber_tests.steps.subtaskSteps;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import teambydefault.todo.cucumber_tests.CucumberRunner;

/**
 * Cucumber step definitions for stDelete.feature.
 * Uses Selenium + TodoPage POM for browser-based subtask deletion.
 */
public class SubtaskDeleteSteps {

    private final CucumberRunner runner;
    private int subtaskCountBefore;

    public SubtaskDeleteSteps(CucumberRunner runner) {
        this.runner = runner;
    }

    // ─── Delete Subtask Scenario ──────────────────────────────────────────────

    @And("The user clicks the Subtask's delete button")
    public void the_user_clicks_the_subtasks_delete_button() {
        subtaskCountBefore = runner.todoPage.getSubtaskItems().size();
        runner.todoPage.clickDeleteFirstSubtask();
    }

    @Then("The Subtask is removed from the list")
    public void the_subtask_is_removed_from_the_list() {
        runner.todoPage.waitForSubtaskCountLessThan(subtaskCountBefore);
        int subtaskCountAfter = runner.todoPage.getSubtaskItems().size();
        assertTrue(subtaskCountAfter < subtaskCountBefore,
                "Expected subtask count to decrease after deletion");
    }

    // ─── No Subtasks Present Scenario ─────────────────────────────────────────

    @When("There are no Subtasks present")
    public void there_are_no_subtasks_present() {
        while (runner.todoPage.hasSubtasksDisplayed()) {
            int countBefore = runner.todoPage.getSubtaskItems().size();
            runner.todoPage.clickDeleteFirstSubtask();
            runner.todoPage.waitForSubtaskCountLessThan(countBefore);
        }
        assertFalse(runner.todoPage.hasSubtasksDisplayed(),
                "Expected no subtasks to be present");
    }

    @And("The user tries to click the delete button")
    public void the_user_tries_to_click_the_delete_button() {
        // Intentionally no-op — we just need to check the button doesn't exist
    }

    @Then("The button is not found")
    public void the_button_is_not_found() {
        assertFalse(runner.todoPage.isSubtaskDeleteButtonPresent(),
                "Expected no delete button to be present when there are no subtasks");
    }
}
