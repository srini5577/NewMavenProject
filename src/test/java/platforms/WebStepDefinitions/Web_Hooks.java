package platforms.WebStepDefinitions;

import com.cucumber.listener.Reporter;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.PageFactory;
import utils.CommonFunctions;
import utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Web_Hooks extends Utils {

    private static Logger log = Logger.getLogger(Web_Hooks.class);


    @Before()
    public void BeforeScenario() {

            Reporter.assignAuthor("Automation - OnlineBanking");
            System.out.println("intitalizing webdriver...");
            driver  = setupWebDriver("chrome");
            CommonFunctions wc = PageFactory.initElements(Utils.driver, CommonFunctions.class);
    }


    @After(order = 1)
    public void afterScenario(Scenario scenario) throws IOException, InterruptedException {
        if (scenario.isFailed()) {
            String screenshotName = scenario.getName().replaceAll(" ", "_");
            String timestamp = new SimpleDateFormat("yyyy:MM:dd_HH:mm:ss").format(Calendar.getInstance().getTime()).replaceAll(":", "-");
            String encodedBase64 = null;
            FileInputStream fileInputStream = null;
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File source = screenshot.getScreenshotAs(OutputType.FILE);
            String destination =System.getProperty("user.dir") +"/target/cucumber-reports/WebReports/Screenshots/" + screenshotName + timestamp + ".png";
            File finalDestination = new File(destination);
            FileUtils.copyFile(source, finalDestination);

            fileInputStream =new FileInputStream(finalDestination);
            byte[] bytes =new byte[(int)finalDestination.length()];fileInputStream.read(bytes);
            encodedBase64 = new String(Base64.encodeBase64(bytes));
            String img = "data:image/png;base64,"+encodedBase64;

            Reporter.addScreenCaptureFromPath(img.toString());
        }

    }


    @After(order = 0)
    public void AfterSteps() throws InterruptedException {
        driver.close();
        Thread.sleep(2000);
    }


}
