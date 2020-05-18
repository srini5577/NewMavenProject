package platforms.WebStepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class ClickUser {
    LoginSteps log= new LoginSteps();

    @Given("^User Click on that$")
    public void user_Click_on_that() throws Throwable {
        log.user_opens_browser_enters_url();
        System.out.println("abcd eedfdfd worked");

    }

    @When("^usere d$")
    public void usere_d() throws Throwable {
        System.out.println("loginWorgdijmodgjidfgvdf");

    }

}
