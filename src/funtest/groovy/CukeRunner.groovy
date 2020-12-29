import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber.class)
@CucumberOptions(
        features = ["src/funtest/features"],
        glue = ["steps"],
        tags = "not @manual and not @review and not @pending"
)
class CukeRunner {}
