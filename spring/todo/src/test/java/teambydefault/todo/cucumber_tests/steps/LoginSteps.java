package teambydefault.todo.cucumber_tests.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import teambydefault.todo.cucumber_tests.CucumberRunner;

/**
 * Cucumber step definitions for login.feature.
 *
 * <p>All interactions go through the browser via Page Object Models.
 * WebDriver setup and teardown live in {@link CucumberRunner}.
 */
public class LoginSteps {

    private final CucumberRunner runner;

    public LoginSteps(CucumberRunner runner) {
        this.runner = runner;
    }

    // ── Background steps ─────────────────────────────────────────────────────

    /**
     * Seeds an account via the registration page. If registration fails
     * (account already exists), navigates to the login page instead.
     * If registration succeeds (lands on /home), logs out to prepare
     * for the next step.
     */
    @Given("The account {string} {string} exists")
    public void theAccountExists(String email, String password) {
        runner.registerPage.open();
        runner.registerPage.enterEmail(email);
        runner.registerPage.enterPassword(password);
        runner.registerPage.clickRegisterButton();

        try {
            // If registration succeeds, the app navigates to /home
            String url = runner.registerPage.getCurrentUrl();
            if (url.contains("/home")) {
                // Logout back to the login page
                runner.loginPage.clickLogoutButton();
            }
        } catch (Exception e) {
            // Registration failed (account already exists) — navigate to login page
            runner.loginPage.open();
        }
    }

    /** Navigate to the login page (simulates an unauthorized user hitting the site). */
    @When("The unauthorized user accesses the website")
    public void theUnauthorizedUserAccessesTheWebsite() {
        runner.loginPage.open();
    }

    // ── Scenario: valid credentials ──────────────────────────────────────────

    @And("The user enters the username {string} and password {string}")
    public void theUserEntersUsernameAndPassword(String email, String password) {
        runner.loginPage.enterEmail(email);
        runner.loginPage.enterPassword(password);
    }

    @And("The user clicks the login button")
    public void theUserClicksTheLoginButton() {
        runner.loginPage.clickLoginButton();
    }

    @Then("The user is redirected to their todo manager")
    public void theUserIsRedirectedToTheirTodoManager() {
        // After a successful login the Angular router navigates to /home
        assertThat(runner.loginPage.getCurrentUrl()).contains("/home");
    }

    // ── Scenario Outline: invalid credentials ────────────────────────────────

    @Then("The user should see login error message {string}")
    public void theUserShouldSeeLoginErrorMessage(String expectedMessage) {
        String actual = runner.loginPage.getErrorMessage();
        assertThat(actual).contains(expectedMessage);
    }
}
