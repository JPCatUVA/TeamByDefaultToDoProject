package teambydefault.todo.cucumber_tests.poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model for the Login page (http://localhost:4200/login).
 */
public class LoginPage {

    private static final String LOGIN_URL = "http://localhost:4200/login";

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /** Navigate directly to the login page. */
    public void open() {
        driver.get(LOGIN_URL);
    }

    /** Enter a value into the email field. */
    public void enterEmail(String email) {
        WebElement emailInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    /** Enter a value into the password field. */
    public void enterPassword(String password) {
        WebElement passwordInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    /** Click the Sign In button. */
    public void clickLoginButton() {
        WebElement submitBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitBtn.click();
    }

    /** Click the "Create one" registration link. */
    public void clickRegistrationLink() {
        WebElement registerLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button.link-btn")));
        registerLink.click();
    }

    /** Return the current browser URL. */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /** Return whether the error message paragraph is visible. */
    public boolean isErrorDisplayed() {
        return !driver.findElements(By.cssSelector("p.login-error")).isEmpty();
    }
}
