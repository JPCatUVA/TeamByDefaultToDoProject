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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@Suite
@IncludeEngines("cucumber")
@SelectPackages({
    "features", 
    "teambydefault.todo.cucumber_tests.steps"
})
@ConfigurationParameter(
    key = Constants.GLUE_PROPERTY_NAME, 
    value = "teambydefault.todo.cucumber_tests.steps")
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
    private WebDriver driver;

    @Before
    public void setup(){
        ChromeOptions ops = new ChromeOptions();
        ops.addArguments("--headless");

        driver = new ChromeDriver(ops);

        //Not being used currently, but here if it is needed
        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
    }

    //Teardown as these are external resourses that need to be memory-managed
    //manually
    @After
    public void destroy(){
        if(driver != null){
            driver.quit();
            driver = null;
        }
 
    }

    //Driver Getter
    public WebDriver getDriver(){
        return driver;
    }
}
