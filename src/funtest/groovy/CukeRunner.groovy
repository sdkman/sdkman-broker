import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

@RunWith(Cucumber)
@CucumberOptions(
        format=["pretty", "html:build/reports/cucumber"],
        strict=true,
        features=["src/funtest/features"],
        glue=["src/funtest/groovy"],
        tags=["~@manual", "~@review"]
)
class CukeRunner {}