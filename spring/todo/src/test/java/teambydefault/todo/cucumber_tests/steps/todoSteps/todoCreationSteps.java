package teambydefault.todo.cucumber_tests.steps.todoSteps;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import teambydefault.todo.cucumber_tests.CucumberRunner;

/**
 * Cucumber step definitions for todocreate.feature.
 * Uses Selenium + TodoPage POM for browser-based task creation.
 */
public class todoCreationSteps {

    private final CucumberRunner runner;
    private int taskCountBefore;

    public todoCreationSteps(CucumberRunner runner) {
        this.runner = runner;
    }

    // ─── Background Steps ─────────────────────────────────────────────────────

    @And("The user clicks the Add Task button")
    public void the_user_clicks_the_add_task_button() {
        taskCountBefore = runner.todoPage.getTaskItems().size();
        runner.todoPage.clickAddTaskButton();
    }

    // ─── Create Task Steps ────────────────────────────────────────────────────

    @When("The user enters Task Title {string}, Description {string}, and picks a Due Date {string}")
    public void the_user_enters_task_title_description_and_due_date(String title, String description, String date) {
        runner.todoPage.fillAddTaskForm(title, description, date);
    }

    @And("The user clicks the Save Task button")
    public void the_user_clicks_the_save_task_button() {
        runner.todoPage.clickSaveTaskButton();
    }

    @Then("A Task is created and is viewable in the Tasks list with title {string}")
    public void a_task_is_created_and_is_viewable_in_the_tasks_list(String expectedTitle) {
        runner.todoPage.waitForTaskCountGreaterThan(taskCountBefore);

        boolean found = runner.todoPage.getTaskItems().stream()
                .anyMatch(item -> item.getText().contains(expectedTitle));
        assertTrue(found,
                "Expected task with title '" + expectedTitle + "' to appear in the task list");
    }

    // ─── Invalid Create (No Title) Steps ──────────────────────────────────────

    @Then("The Save Task button is invalid")
    public void the_save_task_button_is_invalid() {
        assertTrue(runner.todoPage.isSaveTaskButtonDisabled(),
                "Expected Save Task button to be disabled when title is empty");
    }
}
