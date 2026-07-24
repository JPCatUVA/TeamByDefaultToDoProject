package teambydefault.todo.cucumber_tests;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.junit.platform.engine.Constants;
import io.cucumber.spring.CucumberContextConfiguration;


import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import teambydefault.todo.cucumber_tests.poms.LoginPage;
import teambydefault.todo.cucumber_tests.poms.RegisterPage;
import teambydefault.todo.cucumber_tests.poms.SubtaskPage;
import teambydefault.todo.cucumber_tests.poms.TodoPage;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("teambydefault.todo.cucumber_tests")
@ConfigurationParameter(
    key = Constants.FEATURES_PROPERTY_NAME,
    value = "src/test/resources/features")
@ConfigurationParameter(
    key = Constants.GLUE_PROPERTY_NAME,
    value = "teambydefault.todo.cucumber_tests")
@ConfigurationParameter(
    key = Constants.PLUGIN_PROPERTY_NAME,
    value = "html:reports/cucumber-report.html"
)
@CucumberContextConfiguration
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@TestPropertySource(
    locations = "classpath:test.properties"
)
public class CucumberRunner {

    @Value("${server.port:8080}")
    public int serverPort;

    public WebDriver driver;
    public LoginPage loginPage;
    public RegisterPage registerPage;
    public SubtaskPage subtaskPage;
    public TodoPage todoPage;

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--headless=new",
                "--disable-gpu",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--window-size=1280,800");
        driver = new ChromeDriver(options);
        loginPage = new LoginPage(driver);
        registerPage = new RegisterPage(driver);
        subtaskPage = new SubtaskPage(driver);
        todoPage = new TodoPage(driver);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
