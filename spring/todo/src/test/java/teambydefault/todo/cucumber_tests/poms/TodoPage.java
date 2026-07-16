package teambydefault.todo.cucumber_tests.poms;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object Model for the Todos (home) page (http://localhost:4200/home).
 */
public class TodoPage {
    
    private static final String TODO_URL = "http://localhost:4200/home";


    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "")
    private WebElement elm1;

    public TodoPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }


}
