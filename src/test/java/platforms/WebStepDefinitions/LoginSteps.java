package platforms.WebStepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import utils.Utils;

public class LoginSteps extends Utils {

    @Given("^User opens browser enters url$")
    public void user_opens_browser_enters_url() throws Throwable {
        Thread.sleep(3000);
        driver.get("https://www.greatandhra.com");
    }


    }


