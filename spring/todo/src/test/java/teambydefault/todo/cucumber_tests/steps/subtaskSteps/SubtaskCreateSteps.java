package teambydefault.todo.cucumber_tests.steps.subtaskSteps;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import teambydefault.todo.cucumber_tests.CucumberRunner;

/**
 * Cucumber step definitions for stCreate.feature (Subtask Creation).
 * Also contains the shared Background login steps used across all subtask features.
 */
public class SubtaskCreateSteps {

    private final CucumberRunner runner;
    private int subtaskCountBefore;

    public SubtaskCreateSteps(CucumberRunner runner) {
        this.runner = runner;
    }

    // ── Shared Background steps (used by all subtask features) ───────────────

    @Given("A user is registered with a valid password")
    public void a_user_is_registered_with_a_valid_password() {
        String email = "testuser@test.com";
        String password = "Password123!";

        runner.loginPage.open();

        // If already authenticated and redirected to /home, skip registration
        if (runner.driver.getCurrentUrl().contains("/home")) {
            return;
        }

        runner.loginPage.clickRegistrationLink();

        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("/register"));

        runner.registerPage.enterEmail(email);
        runner.registerPage.enterPassword(password);
        runner.registerPage.clickRegisterButton();

        // After registration attempt, user ends up on /home (success) or stays on /register (error).
        // Either way, ensure we're logged out and on the login page for the next steps.
        try {
            wait.until(ExpectedConditions.urlContains("/home"));
        } catch (Exception e) {
            // Registration failed (user likely already exists) — go to login directly
        }
    }

    @When("The user enters a valid username")
    public void the_user_enters_a_valid_username() {
        runner.loginPage.enterEmail("testuser@test.com");
    }

    @When("The user enters the corresponding password")
    public void the_user_enters_the_corresponding_password() {
        runner.loginPage.enterPassword("Password123!");
        runner.loginPage.clickLoginButton();
    }

    @When("The user is on their home page")
    public void the_user_is_on_their_home_page() {
        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.urlContains("/home"));
        assertTrue(runner.driver.getCurrentUrl().contains("/home"));
    }

    // ── Create-specific Background step ──────────────────────────────────────

    @And("The user clicks the Add Subtask button")
    public void the_user_clicks_the_add_subtask_button() {
        subtaskCountBefore = runner.todoPage.getSubtaskItems().size();
        runner.todoPage.clickAddSubtaskButton();
        assertTrue(runner.todoPage.isAddSubtaskFormVisible(),
                "Expected the add subtask form to be visible");
    }

    // ── Scenario: Cancel subtask creation ────────────────────────────────────

    @When("The user clicks the Cancel button")
    public void the_user_clicks_the_cancel_button() {
        runner.todoPage.clickAddSubtaskButton();
    }

    @Then("The Subtask editing fields should close and nothing is added")
    public void the_subtask_editing_fields_should_close_and_nothing_is_added() {
        assertFalse(runner.todoPage.isAddSubtaskFormVisible(),
                "Expected the add subtask form to be hidden after cancel");
        int subtaskCountAfter = runner.todoPage.getSubtaskItems().size();
        assertEquals(subtaskCountBefore, subtaskCountAfter,
                "Expected no new subtask to be added after cancellation");
    }

    // ── Scenario Outline: Create subtask with varied input ───────────────────

    @When("The user enters Title {string}, Description {string}, and picks a Due Date {string}")
    public void the_user_enters_title_description_and_picks_a_due_date(String title, String description, String date) {
        runner.todoPage.fillAddSubtaskForm(title, description, date);
    }

    @And("The user clicks the Save Subtask button")
    public void the_user_clicks_the_save_subtask_button() {
        runner.todoPage.clickSaveSubtaskButton();
    }

    @Then("A Subtask is created and is viewable in the Subtasks list")
    public void a_subtask_is_created_and_is_viewable_in_the_subtasks_list() {
        WebDriverWait wait = new WebDriverWait(runner.driver, Duration.ofSeconds(2));
        wait.until(d -> runner.todoPage.getSubtaskItems().size() > subtaskCountBefore);
        assertTrue(runner.todoPage.hasSubtasksDisplayed(),
                "Expected newly created subtask to be visible in the subtasks list");
    }

    // ── Scenario Outline: Cannot create subtask without title ────────────────

    @Then("The Save Subtask button is invalid")
    public void the_save_subtask_button_is_invalid() {
        assertTrue(runner.todoPage.isSaveSubtaskButtonDisabled(),
                "Expected the Save Subtask button to be disabled when title is empty");
    }
}
