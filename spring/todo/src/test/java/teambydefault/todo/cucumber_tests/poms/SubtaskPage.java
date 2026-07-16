package teambydefault.todo.cucumber_tests.poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object Model for the Subtask detail view (http://localhost:4200/task/:taskId/subtask/:subtaskId).
 * Maps to the subtask-view component in Angular.
 */
public class SubtaskPage {

    private static final String BASE_URL = "http://localhost:4200";

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ── Page elements ────────────────────────────────────────────────────────

    @FindBy(css = "button.back-btn")
    private WebElement backButton;

    @FindBy(css = "p.error")
    private WebElement errorMessage;

    public SubtaskPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Navigation ───────────────────────────────────────────────────────────

    /** Navigate directly to a subtask detail view. */
    public void open(long taskId, long subtaskId) {
        driver.get(BASE_URL + "/task/" + taskId + "/subtask/" + subtaskId);
    }

    /** Click the "← Back to Task" button. */
    public void clickBackButton() {
        wait.until(ExpectedConditions.elementToBeClickable(backButton)).click();
    }

    /** Return the current browser URL. */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // ── Field read operations ────────────────────────────────────────────────

    /** Get the displayed value of a field by its label (Title, Description, Due Date, Status). */
    public String getFieldValue(String fieldLabel) {
        String xpath = "//div[contains(@class,'subtask-field')]/span[@class='field-label' and text()='"
                + fieldLabel + "']/following-sibling::div[@class='field-value']";
        WebElement fieldValue = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        return fieldValue.getText().replace("Edit", "").trim();
    }

    /** Get the error message text, or empty string if not displayed. */
    public String getErrorMessage() {
        List<WebElement> errors = driver.findElements(By.cssSelector("p.error"));
        return errors.isEmpty() ? "" : errors.get(0).getText();
    }

    /** Return true if the error message element is displayed. */
    public boolean isErrorDisplayed() {
        return !driver.findElements(By.cssSelector("p.error")).isEmpty();
    }

    // ── Edit operations ──────────────────────────────────────────────────────

    /** Click the Edit button for a given field (Title, Description, Due Date, Status). */
    public void clickEditButton(String fieldLabel) {
        String xpath = "//div[contains(@class,'subtask-field')]/span[@class='field-label' and text()='"
                + fieldLabel + "']/following-sibling::div//button[@class='edit-btn']";
        WebElement editBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        editBtn.click();
    }

    /** Enter text into the currently visible edit input (text field). */
    public void enterEditText(String text) {
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.edit-row input[type='text']")));
        input.clear();
        input.sendKeys(text);
    }

    /** Enter text into the currently visible edit textarea (description). */
    public void enterEditTextarea(String text) {
        WebElement textarea = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.edit-row textarea")));
        textarea.clear();
        textarea.sendKeys(text);
    }

    /** Enter a value into the currently visible datetime-local edit input. */
    public void enterEditDueDate(String dateTimeLocal) {
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.edit-row input[type='datetime-local']")));
        input.clear();
        input.sendKeys(dateTimeLocal);
    }

    /** Toggle the completed checkbox in the edit row. */
    public void toggleCompletedCheckbox() {
        WebElement checkbox = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("div.edit-row input[type='checkbox']")));
        checkbox.click();
    }

    /** Click Save in the active edit row. */
    public void clickSaveButton() {
        WebElement saveBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[contains(@class,'edit-row')]//button[text()='Save']")));
        saveBtn.click();
    }

    /** Click Cancel in the active edit row. */
    public void clickCancelButton() {
        WebElement cancelBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[contains(@class,'edit-row')]//button[text()='Cancel']")));
        cancelBtn.click();
    }

    /** Return whether the Save button in the edit row is disabled. */
    public boolean isSaveButtonDisabled() {
        WebElement saveBtn = driver.findElement(
                By.xpath("//div[contains(@class,'edit-row')]//button[text()='Save']"));
        String disabled = saveBtn.getAttribute("disabled");
        return disabled != null;
    }

    /** Return true if an edit row is currently visible (i.e. a field is being edited). */
    public boolean isEditRowVisible() {
        return !driver.findElements(By.cssSelector("div.edit-row")).isEmpty();
    }
}
