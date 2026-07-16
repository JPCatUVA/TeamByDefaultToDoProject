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
 * Page Object Model for the Task detail view (http://localhost:4200/task/:id).
 * Also provides helpers for the home page task list since the task-view is
 * reached by clicking a task on the home page.
 */
public class TodoPage {

    private static final String HOME_URL = "http://localhost:4200/home";

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ── Home page elements ───────────────────────────────────────────────────

    @FindBy(css = "button.add-btn")
    private WebElement addTaskButton;

    @FindBy(id = "title")
    private WebElement addTitleInput;

    @FindBy(id = "description")
    private WebElement addDescriptionInput;

    @FindBy(id = "dueDate")
    private WebElement addDueDateInput;

    @FindBy(css = "form.add-form button[type='submit']")
    private WebElement saveTaskButton;

    @FindBy(css = "ul.task-list")
    private WebElement taskList;

    @FindBy(css = "p.empty")
    private WebElement emptyMessage;

    @FindBy(css = "p.error")
    private WebElement errorMessage;

    // ── Task detail view elements ────────────────────────────────────────────

    @FindBy(css = "button.back-btn")
    private WebElement backButton;

    public TodoPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Navigation ───────────────────────────────────────────────────────────

    /** Navigate to the home page (task list). */
    public void open() {
        driver.get(HOME_URL);
    }

    /** Navigate to a specific task's detail view. */
    public void openTask(long taskId) {
        driver.get(HOME_URL.replace("/home", "/task/" + taskId));
    }

    /** Click the back button to return to the task list. */
    public void clickBackButton() {
        wait.until(ExpectedConditions.elementToBeClickable(backButton)).click();
    }

    /** Return the current browser URL. */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // ── Task list interactions (Home page) ───────────────────────────────────

    /** Click the "+ Add Task" / "Cancel" toggle button. */
    public void clickAddTaskButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addTaskButton)).click();
    }

    /** Fill in the add-task form fields. */
    public void fillAddTaskForm(String title, String description, String dueDate) {
        wait.until(ExpectedConditions.visibilityOf(addTitleInput));
        addTitleInput.clear();
        addTitleInput.sendKeys(title);

        addDescriptionInput.clear();
        addDescriptionInput.sendKeys(description);

        addDueDateInput.clear();
        addDueDateInput.sendKeys(dueDate);
    }

    /** Click the "Save Task" submit button. */
    public void clickSaveTaskButton() {
        wait.until(ExpectedConditions.elementToBeClickable(saveTaskButton)).click();
    }

    /** Return whether the Save Task button is disabled. */
    public boolean isSaveTaskButtonDisabled() {
        return !saveTaskButton.isEnabled();
    }

    /** Return all task item elements in the list. */
    public List<WebElement> getTaskItems() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.task-list")));
        return driver.findElements(By.cssSelector("li.task-item"));
    }

    /** Return true if at least one task is displayed in the list. */
    public boolean hasTasksDisplayed() {
        return !driver.findElements(By.cssSelector("li.task-item")).isEmpty();
    }

    /** Click the first task in the list. */
    public void clickFirstTask() {
        WebElement firstTask = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("li.task-item")));
        firstTask.click();
    }

    /** Click a task by its visible title text. */
    public void clickTaskByTitle(String title) {
        WebElement task = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(@class,'task-item')]//div[@class='task-title' and text()='" + title + "']/ancestor::li")));
        task.click();
    }

    /** Click the delete button on the first task. */
    public void clickDeleteFirstTask() {
        WebElement deleteBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("li.task-item button.delete-btn")));
        deleteBtn.click();
    }

    /** Return true if the empty message ("No tasks found") is displayed. */
    public boolean isEmptyMessageDisplayed() {
        return !driver.findElements(By.cssSelector("p.empty")).isEmpty();
    }

    // ── Task detail view interactions ────────────────────────────────────────

    /** Click the Edit button for a given field (title, description, dueDate, isCompleted). */
    public void clickEditButton(String fieldLabel) {
        String xpath = "//div[contains(@class,'task-field')]/span[@class='field-label' and text()='"
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

    /** Enter text into the currently visible edit textarea. */
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

    /** Click Save in the active edit row. */
    public void clickEditSaveButton() {
        WebElement saveBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[contains(@class,'edit-row')]//button[text()='Save']")));
        saveBtn.click();
    }

    /** Click Cancel in the active edit row. */
    public void clickEditCancelButton() {
        WebElement cancelBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[contains(@class,'edit-row')]//button[text()='Cancel']")));
        cancelBtn.click();
    }

    /** Return whether the Save button in the edit row is disabled. */
    public boolean isEditSaveButtonDisabled() {
        WebElement saveBtn = driver.findElement(
                By.xpath("//div[contains(@class,'edit-row')]//button[text()='Save']"));
        String disabled = saveBtn.getAttribute("disabled");
        return disabled != null;
    }

    /** Get the displayed value of a field by its label. */
    public String getFieldValue(String fieldLabel) {
        String xpath = "//div[contains(@class,'task-field')]/span[@class='field-label' and text()='"
                + fieldLabel + "']/following-sibling::div[@class='field-value']";
        WebElement fieldValue = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        return fieldValue.getText().replace("Edit", "").trim();
    }

    // ── Subtasks section (within task detail view) ───────────────────────────

    /** Click the "+ Add Subtask" / "Cancel" button in the subtasks section. */
    public void clickAddSubtaskButton() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("section.subtasks-section button.add-btn")));
        btn.click();
    }

    /** Fill in the add-subtask form. */
    public void fillAddSubtaskForm(String title, String description, String dueDate) {
        WebElement titleInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("sub-title")));
        titleInput.clear();
        titleInput.sendKeys(title);

        WebElement descInput = driver.findElement(By.id("sub-description"));
        descInput.clear();
        descInput.sendKeys(description);

        WebElement dateInput = driver.findElement(By.id("sub-dueDate"));
        dateInput.clear();
        dateInput.sendKeys(dueDate);
    }

    /** Click the "Save Subtask" submit button. */
    public void clickSaveSubtaskButton() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("section.subtasks-section form.add-form button[type='submit']")));
        btn.click();
    }

    /** Return whether the Save Subtask button is disabled. */
    public boolean isSaveSubtaskButtonDisabled() {
        WebElement btn = driver.findElement(
                By.cssSelector("section.subtasks-section form.add-form button[type='submit']"));
        return !btn.isEnabled();
    }

    /** Return whether the add-subtask form is currently visible. */
    public boolean isAddSubtaskFormVisible() {
        return !driver.findElements(By.cssSelector("section.subtasks-section form.add-form")).isEmpty();
    }

    /** Return all subtask item elements. */
    public List<WebElement> getSubtaskItems() {
        return driver.findElements(By.cssSelector("li.subtask-item"));
    }

    /** Return true if at least one subtask is displayed. */
    public boolean hasSubtasksDisplayed() {
        return !driver.findElements(By.cssSelector("li.subtask-item")).isEmpty();
    }

    /** Click on the first subtask in the list. */
    public void clickFirstSubtask() {
        WebElement firstSubtask = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("li.subtask-item")));
        firstSubtask.click();
    }

    /** Click the delete button on the first subtask. */
    public void clickDeleteFirstSubtask() {
        WebElement deleteBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("li.subtask-item button.delete-btn")));
        deleteBtn.click();
    }

    /** Return true if any subtask delete button is present. */
    public boolean isSubtaskDeleteButtonPresent() {
        return !driver.findElements(By.cssSelector("li.subtask-item button.delete-btn")).isEmpty();
    }

    /** Get the error message text, or empty string if not displayed. */
    public String getErrorMessage() {
        List<WebElement> errors = driver.findElements(By.cssSelector("p.error"));
        return errors.isEmpty() ? "" : errors.get(0).getText();
    }

    /** Get any field-level error text from the add-subtask form. */
    public String getSubtaskFieldError() {
        List<WebElement> errors = driver.findElements(By.cssSelector("section.subtasks-section span.field-error"));
        return errors.isEmpty() ? "" : errors.get(0).getText();
    }
}
