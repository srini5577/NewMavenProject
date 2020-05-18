package utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DriverFactory {

    private static Logger log = Logger.getLogger(DriverFactory.class);
    public static AppiumDriver<MobileElement>  driver;
    public static String propertyFile = "./Framework.properties";
    Utils util = new Utils();
    String androidAppLocation = util.getConfigPropertyValue(propertyFile, "AndroidAppLocation") + util.getConfigPropertyValue(propertyFile, "AndroidAppName");
    String iOSAppLocation = util.getConfigPropertyValue(propertyFile, "iOSAppLocation") + util.getConfigPropertyValue(propertyFile, "iOSAppName");
    File androidApp = new File(androidAppLocation);
    File iOSApp = new File(iOSAppLocation);


    public AppiumDriver getAndroidDriver() {
        try {
            if (driver == null) {
                PropertyConfigurator.configure("log4j.properties");
                DesiredCapabilities capabilities = setAndroidCapabilities("emulator-5556",
                        "Android", androidApp.getAbsolutePath());
                log.info("Start driver setup");
                driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);

                log.info("App launched successfully");
                log.info("Driver setup successful" + driver);
                driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.info("Unable to load driver! - Exception: " + e.getMessage());
        }

        return driver;
    }

    public AppiumDriver getIOSDriver() {
        try {
            if (driver==null) {
                PropertyConfigurator.configure("log4j.properties");
                DesiredCapabilities capabilities = setIOSCapabilities("iOS", "iPhone 8", "12.4", iOSApp.getAbsolutePath(),
                        "PORTRAIT", "XCUITest");
                driver = new IOSDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
            }
        } catch (Exception e) {
            log.info("Unable to load driver! - Exception: " + e.getMessage());
        }
        return driver;
    }


    private DesiredCapabilities setAndroidCapabilities(String... args) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, args[0]);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, args[1]);
        capabilities.setCapability(MobileCapabilityType.APP, args[2]);
        capabilities.setCapability(MobileCapabilityType.NO_RESET, false);
        capabilities.setCapability(MobileCapabilityType.FULL_RESET, true);
        capabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
        capabilities.setCapability(AndroidMobileCapabilityType.UNICODE_KEYBOARD, true);
        capabilities.setCapability(AndroidMobileCapabilityType.RESET_KEYBOARD, true);
        capabilities.setCapability("autoAcceptAlerts", true);
        capabilities.setCapability("skipDeviceInitialization", false);
        capabilities.setCapability("skipServerInstallation", false);
        //capabilities.setCapability("autoDismissAlerts", true);

        return capabilities;
    }

    private DesiredCapabilities setIOSCapabilities(String... args) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, args[0]);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, args[1]);
        capabilities.setCapability("platformVersion", args[2]);
        capabilities.setCapability(MobileCapabilityType.APP, args[3]);
        capabilities.setCapability("orientation", args[4]);
        capabilities.setCapability("automationName", args[5]);
//        capabilities.setCapability(MobileCapabilityType.UDID, "2092e749a6178b4b980bef50b986c0bf68d6b15e");
        capabilities.setCapability("useNewWDA", false);
        capabilities.setCapability("waitForQuiescence", false);
        capabilities.setCapability("showXcodeLog", true);
        capabilities.setCapability(MobileCapabilityType.NO_RESET, false);
        capabilities.setCapability("autoAcceptAlerts", true);
        capabilities.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS, true);
        capabilities.setCapability(IOSMobileCapabilityType.CONNECT_HARDWARE_KEYBOARD, false);
        capabilities.setCapability("shouldUseSingletonTestManager", false);
        capabilities.setCapability("unicodeKeyboard", true);
        capabilities.setCapability("skipDeviceInitialization", true);
        capabilities.setCapability("skipServerInstallation", true);
        capabilities.setCapability(IOSMobileCapabilityType.LAUNCH_TIMEOUT, "500000");


        return capabilities;
    }
}