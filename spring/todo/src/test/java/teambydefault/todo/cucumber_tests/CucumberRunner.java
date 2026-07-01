package teambydefault.todo.cucumber_tests;

import io.cucumber.junit.platform.engine.Constants;
import io.cucumber.spring.CucumberContextConfiguration;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.openqa.selenium.WebDriver;
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
}
