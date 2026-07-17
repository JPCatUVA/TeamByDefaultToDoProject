package teambydefault.todo.cucumber_tests.poms;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        if (text.isEmpty()) {
            input.sendKeys("x");
            input.sendKeys(Keys.BACK_SPACE);
        } else {
            input.sendKeys(text);
        }
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
        input.click();
        sendDateTimeKeys(input, dateTimeLocal);
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

    // ── Wait helpers ─────────────────────────────────────────────────────────

    /** Wait until the URL matches the subtask detail view pattern. */
    public void waitForSubtaskDetailUrl() {
        wait.until(ExpectedConditions.urlContains("subtask"));
    }

    /** Wait until the edit row disappears (save completed). */
    public void waitForEditRowHidden() {
        wait.until(d -> !isEditRowVisible());
    }

    /** Wait until the error message is displayed. */
    public void waitForErrorDisplayed() {
        wait.until(d -> isErrorDisplayed());
    }

    /** Wait until the Save button in the edit row becomes disabled. */
    public void waitForSaveButtonDisabled() {
        wait.until(d -> {
            WebElement btn = d.findElement(
                    By.xpath("//div[contains(@class,'edit-row')]//button[text()='Save']"));
            return btn.getAttribute("disabled") != null;
        });
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    /**
     * Send date/time keys to a datetime-local input in Chrome's native format.
     * Accepts ISO format (e.g. "2026-12-25T09:00") and types it as:
     * mmddyyyy TAB hhmm AM/PM
     */
    private void sendDateTimeKeys(WebElement input, String isoDateTime) {
        LocalDateTime dt = LocalDateTime.parse(isoDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String month = String.format("%02d", dt.getMonthValue());
        String day = String.format("%02d", dt.getDayOfMonth());
        String year = String.format("%04d", dt.getYear());

        int hour = dt.getHour();
        String ampm = hour >= 12 ? "PM" : "AM";
        if (hour == 0) hour = 12;
        else if (hour > 12) hour -= 12;
        String hourStr = String.format("%02d", hour);
        String minute = String.format("%02d", dt.getMinute());

        input.sendKeys(month + day + year);
        input.sendKeys(Keys.TAB);
        input.sendKeys(hourStr + minute + ampm);
    }
}
