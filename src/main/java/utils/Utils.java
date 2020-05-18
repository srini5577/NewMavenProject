package utils;

import com.cucumber.listener.Reporter;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static WebDriver driver;
    public JavascriptExecutor jExecutor;
    public WebDriverWait driverWait;
    private static Logger log = Logger.getLogger(Utils.class);


    /*
     * this sets up the the web driver for testNG multi-platform run takes only the
     * browser name and then sets up the web driver for the specified driver
     *
     */
    public WebDriver setupWebDriver(String browserName)  {
        String macDriverLocation = "./Drivers/Mac/";
        String linuxDriverLocation = "./Drivers/Linux/";
        String windowsDriverLocation = "./Drivers/Windows/";

        System.out.println("Browser name : " + browserName);
        if (browserName.equalsIgnoreCase("chrome")) {
            String chromeDriverPath = null;

            if (this.getOsName().equalsIgnoreCase("Windows")) {
                chromeDriverPath = windowsDriverLocation + "chromedriver.exe";
            } else if (this.getOsName().equalsIgnoreCase("Mac OS")) {
                chromeDriverPath = macDriverLocation + "chromedriver";
            } else if (this.getOsName().equalsIgnoreCase("Linux")) {
                chromeDriverPath = linuxDriverLocation + "chromedriver";
            }

            log.info("This is the chrome driver path is :::: " + chromeDriverPath);

            String absoluteChromeDriverPath = toAbsolutePath(chromeDriverPath);
            log.info("This is the chrome driver real path is :::: " + absoluteChromeDriverPath);

            System.setProperty("webdriver.chrome.driver", absoluteChromeDriverPath);
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("useAutomationExtension", false);
            options.addArguments("--no-sandbox");
	    options.addArguments("--disable-extensions"); // disabling extensions
	    options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
	    //options.addArguments("--headless");
            options.addArguments("Zoom 50%");
	    options.addArguments("start-maximized"); // open Browser in maximized mode
	    options.addArguments("disable-infobars"); // disabling infobars
            driver = new ChromeDriver(options);

        } else if (browserName.equalsIgnoreCase("firefox")) {
            String firefoxDriverPath = null;
            log.info("Firefox ?: " + browserName);
            if (this.getOsName().equalsIgnoreCase("Windows")) {
                firefoxDriverPath = windowsDriverLocation + "geckodriver.exe";
            } else if (this.getOsName().equalsIgnoreCase("Mac OS")) {
                firefoxDriverPath = macDriverLocation + "geckodriver";
            } else if (this.getOsName().equalsIgnoreCase("Linux")) {
                firefoxDriverPath = linuxDriverLocation + "geckodriver";
            }
            log.info("This is the firefox driver path is :::: " + firefoxDriverPath);

            String absoluteFirefoxDriverPath = toAbsolutePath(firefoxDriverPath);
            log.info("This is the chrome driver real path is :::: " + absoluteFirefoxDriverPath);

            System.setProperty("webdriver.gecko.driver", absoluteFirefoxDriverPath);
            FirefoxOptions options = new FirefoxOptions();
            options.setProfile(new FirefoxProfile());
            options.addPreference("dom.webnotifications.enabled", false);
            options.addArguments("--disable-notifications");

            driver =new FirefoxDriver(options);
            driver = new FirefoxDriver();
        } else if (browserName.equalsIgnoreCase("IE")) {
            String ieDriverPath = null;

            if (this.getOsName().equalsIgnoreCase("Windows")) {
                ieDriverPath = windowsDriverLocation + "IEDriverServer.exe";
            } else if (this.getOsName().equalsIgnoreCase("Mac OS")) {
                ieDriverPath = macDriverLocation + "IEDriverServer";
            } else if (this.getOsName().equalsIgnoreCase("Linux")) {
                ieDriverPath = linuxDriverLocation + "IEDriverServer";
            }
            log.info("This is the chrome driver path is :::: " + ieDriverPath);

            String absoluteIeDriverPath = toAbsolutePath(ieDriverPath);
            log.info("This is the chrome driver real path is :::: " + absoluteIeDriverPath);

            System.setProperty("webdriver.ie.driver", absoluteIeDriverPath);
            driver = new InternetExplorerDriver();
        }

        try {
            jExecutor = (JavascriptExecutor) driver;
            driver.manage().window().maximize();
            driverWait = new WebDriverWait(driver, 5);
        } catch (Exception e) {
            log.info("The stack trace here happens when I try to maximize the screen");
            log.info(e.getStackTrace()) ;
        }
        return driver;
    }

    public boolean checkNullElement(WebElement thisElement) {
        return (thisElement.equals(null)) ? false : true;
    }

    public String getConfigPropertyValue(String propertyFileName, String propertyName) {
        String Value = null;
        try {
            FileInputStream fileIS = new FileInputStream(new File(propertyFileName));
            Properties prop = new Properties();
            prop.load(fileIS);

            Value = prop.getProperty(propertyName);
        } catch (IOException e) {
            log.info(e.getStackTrace()) ;
        }

        return Value;
    }

    /**
     * Delay time in seconds to pause or hold for some page objects to load takes
     * only integer value of number of seconds to pause
     */

    public void secondsDelay(int sec) {
        int timeInMilliSeconds;
        try {
            timeInMilliSeconds = sec * 1000;
            log.info("##############################################");
            log.info("Going for " + timeInMilliSeconds + " delay");
            log.info("##############################################");
            Thread.sleep(timeInMilliSeconds);
        } catch (Exception e) {
            log.info(e.getStackTrace());
        }
    }

    /**
     * get real path on a machine using a relative path with respect to the current
     * root directory (i.e project root directory) it takes one argument just the
     * relative path
     */
    public static String toAbsolutePath(String relativePath) {
        Path relPath = Paths.get(relativePath);
        Path absolutePath = null;
        if (!relPath.isAbsolute()) {
            Path base = Paths.get("");
            absolutePath = base.resolve(relPath).toAbsolutePath();
        }
        return absolutePath.normalize().toString();
    }

    /**
     * get the os type that the code is running on takes no variable but returns the
     * os type such as: Windows, Mac OS, Linux
     */
    public String getOsName() {
        String osType;
        String osName = "";

        osType = System.getProperty("os.name");

        if (osType.contains("Windows") || osType.contains("windows")) {
            osName = "Windows";
        } else if (osType.contains("Mac") || osType.contains("mac")) {
            osName = "Mac OS";
        } else if (osType.contains("Linux") || osType.contains("linux")) {
            osName = "Linux";
        }

        log.info("os Type is ::: " + osType + "found os Name ::: " + osName);

        return osName;
    }

    /*
     * this method ticks a checkbox among a list of checkboxes on a page
     *
     */
    public void tickCheckBoxFromMultiple(List checkBoxes, int indexToCheck) {
        boolean checkIsChecked = ((WebElement) checkBoxes.get(indexToCheck)).isSelected();

        if (checkIsChecked) {
            System.out.println("The checkbox at index " + indexToCheck + " is ticked already");
        } else {
            ((WebElement) checkBoxes.get(indexToCheck)).click();
        }
    }

    /*
     * this method ticks the checkbox passed in the web element
     *
     */
    public void tickCheckBox(WebElement checkBox) {
        boolean checkIsChecked = checkBox.isSelected();

        if (checkIsChecked) {
            System.out.println("The checkbox is ticked already");
        } else {
            checkBox.click();
        }
    }

    /*
     * This creates the a new folder to store artifacts
     *
     * */
    public void createArtifactsFolder() {
        String osName = getOsName();
        File downloadFolder = null;

        File folderPath = new File("./guiReports/TestArtifacts/");
        try {
            if (!folderPath.exists()) {
                folderPath.mkdir();
                System.out.println("Folder Creation Successful location is :: " + folderPath.getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void scrollPageDown() {
        jExecutor.executeScript("window.scrollTo(0,1000);");
    }

    public void scrollPageUp() {
        jExecutor.executeScript("window.scrollTo(1000,0);");
        System.out.println("I scrolled up");
    }

    public void scrollPageTo(WebElement element) {

//        WebElement element = driver.findElement(By.xpath(element));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        secondsDelay(3);

        System.out.println("I scrolled up");
    }

    public void highLighterMethod(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
    }

    /*
        This method enters text into an input field using javascript
     */

    public void JavascriptEnterText(String text, WebElement element) {

        JavascriptExecutor js = (JavascriptExecutor) Utils.driver;
        js.executeScript("arguments[0].focus();", element);
        js.executeScript("arguments[0].value =' " + text + "';", element);
        js.executeScript("arguments[0].blur();", element);

    }

    /*
        This method performs a javascript click. ** Only to be used if selenium click does not work
     */

    public void Javascriptclick(WebElement element) {

        JavascriptExecutor click = (JavascriptExecutor) driver;
        click.executeScript("arguments[0].click()", element);
    }

    /*
        This method performs a javascript tab off a field. ** Only to be used if selenium tab does not work
     */

    public void JavascriptTab(WebDriver driver, WebElement element) {
        JavascriptExecutor tab = (JavascriptExecutor) driver;

        tab.executeScript("arguments[0].value = '\\t';", element);
    }

    public void DropdownSelect(String element) {

        driver.findElement(By.id("locator-typ-drpdwn")).click();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        WebElement selectByText = driver.findElement(By.xpath("//li[contains(text(),'" + element + "')]"));
        selectByText.click();

    }

    public static void HoverAndDelete(String element) {

        Actions action = new Actions(driver);
        WebElement elementToHover = driver.findElement(By.xpath("//div[contains(text(),'" + element + "')]"));
        WebElement deleteIcon = driver.findElement(By.xpath("//div[contains(text(),'" + element + "')]//following::i[2]"));
        Coordinates cor = ((Locatable) elementToHover).getCoordinates();
        cor.inViewPort();
        action.moveToElement(elementToHover).click(deleteIcon).build().perform();

//        action.click(editIcon).perform();
    }

    public static void scrollToElement(WebElement element) {

        Coordinates cor = ((Locatable) element).getCoordinates();
        cor.inViewPort();
    }

    public static void HoverAndClick(String element) {

        Actions action = new Actions(driver);
        WebElement elementToHover = driver.findElement(By.xpath("//div[contains(text(),'" + element + "')]"));
        WebElement checkBox = driver.findElement(By.xpath("//div[contains(text(),'" + element + "')]//preceding::input[1]"));
        Coordinates cor = ((Locatable) elementToHover).getCoordinates();
        cor.inViewPort();
        action.moveToElement(elementToHover).click(checkBox).build().perform();

//        action.click(editIcon).perform();
    }

    public static void HoverAndEdit(String element) {

        Actions action = new Actions(driver);
        WebElement elementToHover = driver.findElement(By.xpath("//div[contains(text(),'" + element + "')]"));
        WebElement editIcon = driver.findElement(By.xpath("//div[contains(text(),'" + element + "')]//following::i[1]"));
        Coordinates cor = ((Locatable) elementToHover).getCoordinates();
        cor.inViewPort();
        action.moveToElement(elementToHover).click(editIcon).build().perform();

//        action.click(editIcon).perform();
    }


    public static void Hover(WebElement element) {
        Actions action = new Actions(driver);
        action.moveToElement(element).perform();
    }

    public static void MoveToElement(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.perform();
    }

    public String getTextAt(String element) {
        if (driver instanceof JavascriptExecutor) {
            JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
            return (String) javascriptExecutor.executeScript("return document.getElementById('" + element + "').value;");
        }
        return null;
    }

    public void actionClearAndInput(WebElement element, String text) {
        Actions actions = new Actions(driver);
        actions.click(element)
                .keyDown(Keys.CONTROL)
                .sendKeys("a")
                .keyUp(Keys.CONTROL)
                .sendKeys(Keys.BACK_SPACE)
                .build()
                .perform();
        actions.sendKeys(element, text).perform();
    }

    public void actionClear(WebElement element) {
        Actions actions = new Actions(driver);
        actions.click(element)
                .keyDown(Keys.CONTROL)
                .sendKeys("a")
                .keyUp(Keys.CONTROL)
                .sendKeys(Keys.BACK_SPACE)
                .build()
                .perform();
    }

    public void actionInput(WebElement element, String text) {
        Actions actions = new Actions(driver);
        actions.sendKeys(element, text).perform();
    }

    public static void pressDownKey(int numberofpresses) {

        Actions actions = new Actions(driver);

        for (int i = 0; i <= numberofpresses; i ++) {

            actions.sendKeys(Keys.DOWN).perform();

        }
    }

    public static String randomMobileNum() throws IOException {

        String numStart = "+27";
        String numRemainder;
        String numComplete;
        String sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11;

        Random rand = new Random();

        int d3 = rand.nextInt(10);
        int d4 = rand.nextInt(10);
        int d5 = rand.nextInt(10);
        int d6 = rand.nextInt(10);
        int d7 = rand.nextInt(10);
        int d8 = rand.nextInt(10);
        int d9 = rand.nextInt(10);
        int d10 = rand.nextInt(10);
        int d11 = rand.nextInt(10);

        sd3 = Integer.toString(d3);
        sd4 = Integer.toString(d4);
        sd5 = Integer.toString(d5);
        sd6 = Integer.toString(d6);
        sd7 = Integer.toString(d7);
        sd8 = Integer.toString(d8);
        sd9 = Integer.toString(d9);
        sd10 = Integer.toString(d10);
        sd11 = Integer.toString(d11);

        numRemainder= ""+sd3+""+sd4+""+sd5+""+sd6+""+sd7+""+sd8+""
                +sd9+""+sd10+""+sd11+"";

        numComplete = ""+numStart+""+numRemainder+"";
        System.out.println("MobileNum : " + numComplete);
        return numComplete;

    }

    public static void PrintSmt (String Statement) throws FileNotFoundException {
        // Creating a File object that represents the disk file.
        PrintStream o = new PrintStream(new File("A.txt"));

        // Store current System.out before assigning a new value
        PrintStream console = System.out;

        // Assign o to output stream
        System.setOut(o);
        System.out.println(Statement);

        // Use stored value for output stream
        System.setOut(console);
        System.out.println("This will be written on the console!");
    }

    public static String generatePassword (int length) {

        //minimum length of 6
        if (length < 4) {
            length = 6;
        }

        final char[] lowercase = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        final char[] uppercase = "ABCDEFGJKLMNPRSTUVWXYZ".toCharArray();
        final char[] numbers = "0123456789".toCharArray();
        final char[] symbols = "^$?!@#%&".toCharArray();
        final char[] allAllowed = "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789^$?!@#%&".toCharArray();

        //Use cryptographically secure random number generator
        Random random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length-4; i++) {
            password.append(allAllowed[random.nextInt(allAllowed.length)]);
        }

        //Ensure password policy is met by inserting required random chars in random positions
        password.insert(random.nextInt(password.length()), lowercase[random.nextInt(lowercase.length)]);
        password.insert(random.nextInt(password.length()), uppercase[random.nextInt(uppercase.length)]);
        password.insert(random.nextInt(password.length()), numbers[random.nextInt(numbers.length)]);
        password.insert(random.nextInt(password.length()), symbols[random.nextInt(symbols.length)]);

        return password.toString();
    }

    public static void TakeScreenCapture(String screenshotName) throws IOException {
        //screenshotName = getName().replaceAll(" ", "_");
        String timestamp = new SimpleDateFormat("yyyy:MM:dd_HH:mm:ss").format(Calendar.getInstance().getTime()).replaceAll(":", "-");
        String encodedBase64 = null;
        FileInputStream fileInputStream = null;
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File source = screenshot.getScreenshotAs(OutputType.FILE);
        String destination =System.getProperty("user.dir") +"/target/cucumber-reports/WebReports/Screenshots/" + screenshotName + timestamp + ".png";
        File finalDestination = new File(destination);
        FileUtils.copyFile(source, finalDestination);

        fileInputStream =new FileInputStream(finalDestination);
        byte[] bytes =new byte[(int)finalDestination.length()];
        fileInputStream.read(bytes);
        encodedBase64 = new String(Base64.encodeBase64(bytes));
        String img = "data:image/png;base64,"+encodedBase64;

        Reporter.addScreenCaptureFromPath(img.toString());
    }



}
