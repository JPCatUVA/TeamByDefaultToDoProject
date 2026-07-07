package teambydefault.todo.cucumber_tests;

import io.cucumber.junit.platform.engine.Constants;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

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
public class CucumberRunner {
}
