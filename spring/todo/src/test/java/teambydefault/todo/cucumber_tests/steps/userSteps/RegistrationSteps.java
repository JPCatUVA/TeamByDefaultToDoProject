package teambydefault.todo.cucumber_tests.steps.userSteps;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import teambydefault.todo.cucumber_tests.CucumberRunner;

/**
 * Cucumber step definitions for registration.feature.
 *
 * <p>WebDriver setup and teardown live in {@link CucumberRunner}. This class
 * receives the runner via constructor injection and delegates all browser
 * interaction to the shared POMs it owns.
 */
public class RegistrationSteps {

    private final CucumberRunner runner;

    public RegistrationSteps(CucumberRunner runner) {
        this.runner = runner;
    }

    // ── Background steps ─────────────────────────────────────────────────────

    /** Navigate to the login page first (that's where the site starts). */
    @Given("The user is on the login page")
    public void theUserIsOnTheLoginPage() {
        runner.loginPage.open();
    }

    /** Click the "Create one" link on the login page to reach registration. */
    @When("The user clicks the registration link")
    public void theUserClicksTheRegistrationLink() {
        runner.loginPage.clickRegistrationLink();
    }

    // ── Scenario: valid registration ─────────────────────────────────────────

    /**
     * Fills in a known-valid email and password.
     * Uses a timestamp-based email to avoid conflicts across test runs.
     */
    @And("The user enters valid credentials")
    public void theUserEntersValidCredentials() {
        String uniqueEmail = "user" + System.currentTimeMillis() + "@example.com";
        runner.registerPage.enterEmail(uniqueEmail);
        runner.registerPage.enterPassword("P@ssw0rd");
    }

    @And("The user clicks the register button")
    public void theUserClicksTheRegisterButton() {
        runner.registerPage.clickRegisterButton();
    }

    /**
     * After successful registration the Angular component auto-logs in and
     * redirects to /home.
     */
    @Then("The user should be sent to the home page")
    public void theUserShouldBeSentToTheHomePage() {
        assertThat(runner.registerPage.getCurrentUrl()).contains("/home");
    }

    // ── Scenario Outline: invalid credentials ────────────────────────────────

    @And("The user enters username {string} and password {string}")
    public void theUserEntersUsernameAndPassword(String email, String password) {
        runner.registerPage.enterEmail(email);
        runner.registerPage.enterPassword(password);
    }

    /**
     * Asserts the expected failure message is visible in the UI.
     *
     * <p>The feature table uses these messages:
     * <ul>
     *   <li>"Registration failed. Please try a different password." — password too short/long</li>
     *   <li>"Registration failed. Please enter an email." — invalid email format</li>
     * </ul>
     */
    @Then("The user should see failure message {string}")
    public void theUserShouldSeeFailureMessage(String expectedMessage) {
        String actualMessage = runner.registerPage.getErrorMessage();
        assertThat(actualMessage).contains(expectedMessage);
    }
}
