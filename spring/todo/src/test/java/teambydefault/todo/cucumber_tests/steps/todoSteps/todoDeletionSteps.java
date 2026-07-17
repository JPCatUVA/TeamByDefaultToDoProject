package teambydefault.todo.cucumber_tests.steps.todoSteps;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import teambydefault.todo.cucumber_tests.CucumberRunner;

/**
 * Cucumber step definitions for tododelete.feature.
 * Uses Selenium + TodoPage POM for browser-based task deletion.
 */
public class todoDeletionSteps {

    private final CucumberRunner runner;
    private int taskCountBefore;

    public todoDeletionSteps(CucumberRunner runner) {
        this.runner = runner;
    }

    // ─── Delete Task Scenario ─────────────────────────────────────────────────

    @When("The user clicks the Task's delete button")
    public void the_user_clicks_the_tasks_delete_button() {
        taskCountBefore = runner.todoPage.getTaskItems().size();
        runner.todoPage.clickDeleteFirstTask();
    }

    @Then("The Task is removed from the list")
    public void the_task_is_removed_from_the_list() {
        int taskCountAfter = runner.todoPage.getTaskItems().size();
        assertTrue(taskCountAfter < taskCountBefore,
                "Expected task count to decrease after deletion");
    }

    // ─── No Tasks Present Scenario ────────────────────────────────────────────

    @When("There are no Tasks present")
    public void there_are_no_tasks_present() {
        // Delete all existing tasks to ensure none remain
        while (runner.todoPage.hasTasksDisplayed()) {
            runner.todoPage.clickDeleteFirstTask();
        }
        assertFalse(runner.todoPage.hasTasksDisplayed(),
                "Expected no tasks to be present");
    }

    @Then("The delete button is not found")
    public void the_delete_button_is_not_found() {
        assertTrue(runner.todoPage.isEmptyMessageDisplayed(),
                "Expected empty message to be displayed when there are no tasks");
    }
}
