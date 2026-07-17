package teambydefault.todo.cucumber_tests.poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model for the Registration page (http://localhost:4200/register).
 */
public class RegisterPage {

    private static final String REGISTER_URL = "http://localhost:4200/register";

    private final WebDriver driver;
    private final WebDriverWait wait;

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /** Navigate directly to the registration page. */
    public void open() {
        driver.get(REGISTER_URL);
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

    /** Click the "Create Account" submit button. */
    public void clickRegisterButton() {
        WebElement submitBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitBtn.click();
    }

    /**
     * Wait for the URL to change away from the registration page, then return it.
     * Useful after a successful registration + auto-login flow.
     */
    public String getCurrentUrl() {
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/register")));
        return driver.getCurrentUrl();
    }

    /**
     * Return the text of the visible error message element, or an empty string
     * if no error is currently displayed.
     */
    public String getErrorMessage() {
        var errors = driver.findElements(By.cssSelector("p.register-error"));
        if (!errors.isEmpty()) {
            return errors.get(0).getText();
        }
        // Also surface inline field-level errors
        var fieldErrors = driver.findElements(By.cssSelector("span.field-error"));
        if (!fieldErrors.isEmpty()) {
            return fieldErrors.get(0).getText();
        }
        return "";
    }
}
