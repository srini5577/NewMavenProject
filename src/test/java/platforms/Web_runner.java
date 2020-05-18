
package platforms;


import com.cucumber.listener.Reporter;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.testng.annotations.Test;
import utils.Utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//import cucumber.api.testng.AbstractTestNGCucumberTests;


@RunWith(Cucumber.class)
@CucumberOptions(

        features = {"src/test/feature/WEB"},
      // tags ={"@Transfers,@FND_Profile_Limits,@FND_Electricity,@FND_GetInTouch,@FND_ProfileSettings"},
        //  tags = {"@FND_Payments,@FND_Govt_Payments,@FND_Govt_Payments,@FND_Recurring_Payments_Weekly,@FND_Recurring_Payments_Monthly,@FNDWeb_QuickPay"},
        tags = {"@clickUser,@Feature2"},
      //   tags = {"@FND_Recurring_Payments_Weekly,@FND_Recurring_Payments_Monthly,@DeleteScheduledPayments,@ProcessedGovernmentPayments"},
      // tags = {"@FND_Govt_Payments,@FND_Payments,@FND_Recurring_Payments_Weekly,@FND_Recurring_Payments_Monthly,@FND_Payments_MFC_Account,@FND_Profile_Limits,@FNDWeb_QuickPay,@FND_ProfileSettings,@FND_GetInTouch,@Transfers"},

        //Failed Scenarios
       //tags = {"@FNDEditRecipient"},
        glue = {"platforms.WebStepDefinitions"},
        dryRun =false,
        format = {"pretty"},
        plugin = {"com.cucumber.listener.ExtentCucumberFormatter:target/cucumber-reports/WebReports/Web.html"}
        , monochrome = true)


@Test
public class Web_runner {
    private static Logger log = Logger.getLogger(Web_runner.class);
   public static Utils utils = new Utils();

    @org.junit.AfterClass
    public static void writeExtentReport() throws IOException {

        Reporter.loadXMLConfig(new File(System.getProperty("user.dir")+ "/extent-web-config.xml"));
        copyLatestExtentReport();
        Reporter.loadXMLConfig("extent-web-config.xml");
    }

    /***EXTENT REPORT****************************************************************/

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;

        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;

            while((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    public static void copyLatestExtentReport() throws IOException {
        String timestamp = new SimpleDateFormat("yyyy:MM:dd_HH:mm:ss").format(Calendar.getInstance().getTime()).replaceAll(":", "-");
        File source = new File(System.getProperty("user.dir") + "/target/cucumber-reports/WebReports/Web.html");
        File dest = new File(System.getProperty("user.dir") + "/target/cucumber-reports/WebReports/Web_"+ timestamp + ".html");
        copyFileUsingStream(source, dest);
    }

}