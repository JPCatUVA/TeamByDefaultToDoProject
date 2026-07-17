package teambydefault.todo.cucumber_tests.steps.subtaskSteps;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import teambydefault.todo.cucumber_tests.CucumberRunner;

/**
 * Cucumber step definitions for stCreate.feature.
 * Uses Selenium + TodoPage/SubtaskPage POMs for browser-based subtask creation.
 */
public class SubtaskCreateSteps {

    private final CucumberRunner runner;
    private int subtaskCountBefore;

    public SubtaskCreateSteps(CucumberRunner runner) {
        this.runner = runner;
    }

    // ─── Background Steps ─────────────────────────────────────────────────────

    @When("The user logs in to their home page with {string} {string}")
    public void the_user_is_on_their_home_page(String email, String password) {
        runner.loginPage.open();
        runner.loginPage.enterEmail(email);
        runner.loginPage.enterPassword(password);
        runner.loginPage.clickLoginButton();
        runner.todoPage.waitForUrlContains("/home");
    }

    @And("There is a task submitted")
    public void there_is_a_task_submitted() {
        if (!runner.todoPage.hasTasksDisplayed()) {
            runner.todoPage.clickAddTaskButton();
            runner.todoPage.fillAddTaskForm("Subtask Test Parent", "Parent task for subtask tests", "2027-12-31T23:59");
            runner.todoPage.clickSaveTaskButton();
            runner.todoPage.waitForTasksDisplayed();
        }
    }

    @And("The user clicks on a valid task")
    public void the_user_clicks_on_a_valid_task() {
        runner.todoPage.clickFirstTask();
    }

    @Then("The user is on the corresponding task page")
    public void the_user_is_on_the_corresponding_task_page() {
        runner.todoPage.waitForUrlContains("task");
    }

    @And("The user clicks the Add Subtask button")
    public void the_user_clicks_the_add_subtask_button() {
        subtaskCountBefore = runner.todoPage.getSubtaskItems().size();
        runner.todoPage.clickAddSubtaskButton();
        // Wait for the form to actually appear
        runner.todoPage.waitForAddSubtaskFormVisible();
    }

    // ─── Cancel Scenario ──────────────────────────────────────────────────────

    @When("The user clicks the Cancel button")
    public void the_user_clicks_the_cancel_button() {
        runner.todoPage.clickAddSubtaskButton(); // Toggle cancels the form
    }

    @Then("The Subtask editing fields should close and nothing is added")
    public void the_subtask_editing_fields_should_close_and_nothing_is_added() {
        assertFalse(runner.todoPage.isAddSubtaskFormVisible(),
                "Expected add-subtask form to be closed after cancel");
        assertEquals(subtaskCountBefore, runner.todoPage.getSubtaskItems().size(),
                "Expected no new subtasks to be added");
    }

    // ─── Create Subtask Steps ─────────────────────────────────────────────────

    @When("The user enters Title {string}, Description {string}, and picks a Due Date {string}")
    public void the_user_enters_title_description_and_due_date(String title, String description, String date) {
        runner.todoPage.fillAddSubtaskForm(title, description, date);
    }

    @And("The user clicks the Save Subtask button")
    public void the_user_clicks_the_save_subtask_button() {
        runner.todoPage.clickSaveSubtaskButton();
    }

    @Then("A Subtask is created and is viewable in the Subtasks list")
    public void a_subtask_is_created_and_is_viewable_in_the_subtasks_list() {
        runner.todoPage.waitForSubtaskCountGreaterThan(subtaskCountBefore);
        assertTrue(runner.todoPage.hasSubtasksDisplayed(),
                "Expected at least one subtask to be displayed in the subtasks list");
    }

    // ─── Invalid Create (No Title) Steps ──────────────────────────────────────

    @Then("The Save Subtask button is invalid")
    public void the_save_subtask_button_is_invalid() {
        assertTrue(runner.todoPage.isSaveSubtaskButtonDisabled(),
                "Expected Save Subtask button to be disabled when title is empty");
    }
}
