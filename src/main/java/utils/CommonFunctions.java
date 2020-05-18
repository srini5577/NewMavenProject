package utils;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;


public class CommonFunctions {
    public static Logger logger = Logger.getLogger(CommonFunctions.class);
    private static final String CLICK = "click";
    private static final String CLASSNAME = "classname";
    private static final String COUNT = "count";
    private static final String EXISTS = "exists";
    private static final String FALSE = "false";
    private static final String GETATTRIBUTE = "getattribute";
    private static final String GETLEN = "getlen";
    private static final String GETTEXT = "gettext";
    private static final String ISENABLED = "isenabled";
    private static final String LINKTEXT = "linktext";
    private static final String SENDKEYS = "sendkeys";
    private static final String SETTEXT = "settext";
    private static final String VALUE = "value";
    private static final String XPATH = "xpath";
    public static WebDriver wdriver;
    public CommonFunctions wc;


    public CommonFunctions(WebDriver wDriver) {
        this.wdriver = wDriver;
    }

    public CommonFunctions() {

    }


    public void selectDropDownValueCSS(String strDDOpenerCss, String strDDULCss, String strValue) throws InterruptedException {
        this.wdriver.findElement(By.cssSelector(strDDOpenerCss)).click();
        Thread.sleep(700L);
        String strResult = this.actionElementInstanceProperty("css", strDDULCss, "text", strValue, "contains", "click", "");
        if (!strResult.equals("true")) {
            throw new ElementNotSelectableException("List item not selectable");
        }
    }

    public void selectDropDownValueCSSInstance(String strDDOpenerCss, String strDDULCss, int intInstance, String strValue) throws InterruptedException {
        this.actionElementInstance("css", strDDOpenerCss, intInstance, "click", "");
        Thread.sleep(1000L);
        this.actionElementInstanceProperty("css", strDDULCss, "text", strValue, "contains", "click", "");
        Thread.sleep(1500L);
        String strResult = this.actionElementInstance("css", strDDOpenerCss, intInstance, "gettext", "");
        if (!strResult.contains(strValue)) {
            Thread.sleep(1000L);
            strResult = this.actionElementInstance("css", strDDOpenerCss, intInstance, "gettext", "");
            if (!strResult.contains(strValue)) {
                throw new ElementNotSelectableException("No of draws " + strValue + " NOT successfully selected");
            }
        }

    }

    public void selectDropDownValue(String strDDOpenerXpath, String strDDULXPath, String strValue) throws InterruptedException {
        Thread.sleep(1000L);
        this.wdriver.findElement(By.xpath(strDDOpenerXpath)).click();
        Thread.sleep(1000L);

        for (int i = 1; i <= 100; ++i) {
            try {
                String strResult = this.wdriver.findElement(By.xpath(strDDULXPath + "/li[" + i + "]")).getText();
                if (strResult.contains(strValue)) {
                    this.wdriver.findElement(By.xpath(strDDULXPath + "/li[" + i + "]")).click();
                    this.logger.info("Dropdown selection successful for value " + strValue);
                    break;
                }
            } catch (Exception var7) {
                this.logger.info("Dropdown selection failed for value " + strValue);
                throw new ElementNotSelectableException("Dropdown selection failed for value " + strValue);
            }
        }

    }

    public String actionElementInstance(String strElementRecogType, String strElementReference, int intElementInstance, String strElementAction, String strData) {
        try {
            List<WebElement> elements = this.getElements(strElementRecogType, strElementReference, false);
            int intSize = elements.size();
            if (intElementInstance > intSize) {
                return "false";
            } else {
                String strResult = this.doElementAction(elements, strElementAction, intElementInstance, strData);
                return strResult;
            }
        } catch (Exception var9) {
            return "false";
        }
    }

    private List<WebElement> getElements(String strElementRecogType, String strElementReference, boolean blnDoWait) {
        Object elements = new ArrayList();

        try {
            WebDriverWait wait = new WebDriverWait(this.wdriver, 15L);
            String var6 = strElementRecogType.toLowerCase();
            byte var7 = -1;
            switch (var6.hashCode()) {
                case -8935421:
                    if (var6.equals("classname")) {
                        var7 = 5;
                    }
                    break;
                case 3355:
                    if (var6.equals("id")) {
                        var7 = 2;
                    }
                    break;
                case 98819:
                    if (var6.equals("css")) {
                        var7 = 0;
                    }
                    break;
                case 3373707:
                    if (var6.equals("name")) {
                        var7 = 3;
                    }
                    break;
                case 114256029:
                    if (var6.equals("xpath")) {
                        var7 = 1;
                    }
                    break;
                case 1195141159:
                    if (var6.equals("linktext")) {
                        var7 = 4;
                    }
            }

            switch (var7) {
                case 0:
                    if (blnDoWait) {
                        wait.until(ExpectedConditions.visibilityOf(this.wdriver.findElement(By.cssSelector(strElementReference))));
                    }

                    elements = this.wdriver.findElements(By.cssSelector(strElementReference));
                    break;
                case 1:
                    if (blnDoWait) {
                        wait.until(ExpectedConditions.visibilityOf(this.wdriver.findElement(By.xpath(strElementReference))));
                    }

                    elements = this.wdriver.findElements(By.xpath(strElementReference));
                    break;
                case 2:
                    if (blnDoWait) {
                        wait.until(ExpectedConditions.visibilityOf(this.wdriver.findElement(By.id(strElementReference))));
                    }

                    elements = this.wdriver.findElements(By.id(strElementReference));
                    break;
                case 3:
                    if (blnDoWait) {
                        wait.until(ExpectedConditions.visibilityOf(this.wdriver.findElement(By.name(strElementReference))));
                    }

                    elements = this.wdriver.findElements(By.name(strElementReference));
                    break;
                case 4:
                    if (blnDoWait) {
                        wait.until(ExpectedConditions.visibilityOf(this.wdriver.findElement(By.linkText(strElementReference))));
                    }

                    elements = this.wdriver.findElements(By.linkText(strElementReference));
                    break;
                case 5:
                    if (blnDoWait) {
                        wait.until(ExpectedConditions.visibilityOf(this.wdriver.findElement(By.className(strElementReference))));
                    }

                    elements = this.wdriver.findElements(By.className(strElementReference));
            }

            return (List) elements;
        } catch (Exception var8) {
            return (List) elements;
        }
    }

    private String doElementAction(List<WebElement> elements, String strElementAction, int intElementInstance, String strData) throws InterruptedException {
        String strResult = null;
        String var6 = strElementAction.toLowerCase();
        byte var7 = -1;
        switch (var6.hashCode()) {
            case -2000487418:
                if (var6.equals("getattribute")) {
                    var7 = 7;
                }
                break;
            case -1289358244:
                if (var6.equals("exists")) {
                    var7 = 6;
                }
                break;
            case -1249326337:
                if (var6.equals("getlen")) {
                    var7 = 8;
                }
                break;
            case -74172029:
                if (var6.equals("gettext")) {
                    var7 = 5;
                }
                break;
            case 94750088:
                if (var6.equals("click")) {
                    var7 = 0;
                }
                break;
            case 94851343:
                if (var6.equals("count")) {
                    var7 = 1;
                }
                break;
            case 440941271:
                if (var6.equals("isenabled")) {
                    var7 = 2;
                }
                break;
            case 1248131452:
                if (var6.equals("sendkeys")) {
                    var7 = 4;
                }
                break;
            case 1985937551:
                if (var6.equals("settext")) {
                    var7 = 3;
                }
        }

        boolean blnResult;
        switch (var7) {
            case 0:
                ((WebElement) elements.get(intElementInstance - 1)).click();
                break;
            case 1:
                strResult = Integer.toString(elements.size());
                break;
            case 2:
                blnResult = ((WebElement) elements.get(intElementInstance - 1)).isEnabled();
                if (blnResult) {
                    strResult = "true";
                }
                break;
            case 3:
                ((WebElement) elements.get(intElementInstance - 1)).clear();
                ((WebElement) elements.get(intElementInstance - 1)).sendKeys(new CharSequence[]{strData});
                break;
            case 4:
                ((WebElement) elements.get(intElementInstance - 1)).sendKeys(new CharSequence[]{strData});
                break;
            case 5:
                if (strData.equals("value")) {
                    strResult = ((WebElement) elements.get(intElementInstance - 1)).getAttribute("value");
                } else {
                    strResult = ((WebElement) elements.get(intElementInstance - 1)).getText();
                }
                break;
            case 6:
                Thread.sleep(1000L);
                blnResult = ((WebElement) elements.get(intElementInstance - 1)).isDisplayed();
                if (blnResult) {
                    strResult = "true";
                }
                break;
            case 7:
                Thread.sleep(1000L);
                strResult = ((WebElement) elements.get(intElementInstance - 1)).getAttribute(strData);
                break;
            case 8:
                if (strData.equals("value")) {
                    strResult = ((WebElement) elements.get(intElementInstance - 1)).getAttribute("value");
                } else {
                    strResult = ((WebElement) elements.get(intElementInstance - 1)).getText();
                }

                strResult = String.valueOf(strResult.length());
        }

        return strResult;
    }

    public String actionElementInstanceNoWait(String strElementRecogType, String strElementReference, int intElementInstance, String strElementAction, String strData) {
        try {
            List<WebElement> elements = this.getElements(strElementRecogType, strElementReference, false);
            int intSize = elements.size();
            if (intElementInstance > intSize) {
                return "false";
            } else {
                String strResult = this.doElementAction(elements, strElementAction, intElementInstance, strData);
                return strResult;
            }
        } catch (Exception var9) {
            return "false";
        }
    }

    public String actionElementInstanceProperty(String strElementRecogType, String strElementReference, String strPropertyType, String strPropertyValue, String strComparitor, String strElementAction, String strData) {
        try {
            List<WebElement> elements = this.getElements(strElementRecogType, strElementReference, false);
            int intSize = elements.size();
            boolean blnFound = false;

            int intElementInstance;
            for (intElementInstance = 1; !blnFound; ++intElementInstance) {
                blnFound = !"text".equals(strPropertyType) || this.evaluateText(elements, strComparitor, intElementInstance, strPropertyValue);
                if (intElementInstance > intSize) {
                    break;
                }
            }

            if (blnFound) {
                String strResult = this.doElementAction(elements, strElementAction, intElementInstance, strData);
                return strResult;
            } else {
                return "false";
            }
        } catch (Exception var13) {
            return "false";
        }
    }

    private boolean evaluateText(List<WebElement> elements, String strComparitor, int intElementInstance, String strPropertyValue) {
        String strResult = ((WebElement) elements.get(intElementInstance - 1)).getText();
        return strComparitor.equals("equals") ? strResult.equals(strPropertyValue) : strResult.contains(strPropertyValue);
    }

    public String actionExpectedText(String strAction, String strHeader, String strExpectedText, String strXPathParentString) {
        boolean blnFound = false;
        String strResult = "";
        int intRow = 1;

        while (!blnFound) {
            try {
                String strHead = this.wdriver.findElement(By.xpath(strXPathParentString + "/tr[" + intRow + "]/td[1]")).getText();
                if (strHead.equalsIgnoreCase(strHeader)) {
                    strResult = this.wdriver.findElement(By.xpath(strXPathParentString + "/tr[" + intRow + "]/td[2]")).getText();
                    if (strAction.equals("Verify")) {
                        if (strResult.equalsIgnoreCase(strExpectedText)) {
                            strResult = "True";
                            blnFound = true;
                        }
                    } else {
                        blnFound = true;
                    }
                }

                ++intRow;
            } catch (Exception var10) {
                break;
            }
        }

        return strResult;
    }

    public void scrollDown() {
        JavascriptExecutor jse = (JavascriptExecutor) this.wdriver;
        jse.executeScript("window.scrollBy(0,300)", new Object[]{"100"});
    }

    public void scrollToEndOfPage() {
        ((JavascriptExecutor) wdriver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }



    public void scrollUp() {
        JavascriptExecutor jse = (JavascriptExecutor) this.wdriver;
        jse.executeScript("window.scrollBy(0,-300)", new Object[]{""});
    }

    public void scrollToElement(WebElement ele) {
        ((JavascriptExecutor) wdriver).executeScript("arguments[0].scrollIntoView(true);", ele);
    }

    public String getDateData(String strDate) {
        int intDaysAmt = Integer.valueOf(strDate);
        SimpleDateFormat sdFormat = new SimpleDateFormat("EEEE, dd MMMM yyy");
        SimpleDateFormat sdDayFormat = new SimpleDateFormat("dd");
        SimpleDateFormat sdMonthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat sdYearFormat = new SimpleDateFormat("yyy");
        Calendar clDate = Calendar.getInstance();
        String strCurrYear = sdYearFormat.format(clDate.getTime());
        String strCurrMonth = sdMonthFormat.format(clDate.getTime());
        String strCurrDay = sdDayFormat.format(clDate.getTime());
        clDate.add(5, intDaysAmt);
        String strDateData = sdFormat.format(clDate.getTime());
        String strDay = sdDayFormat.format(clDate.getTime());
        String strMonth = sdMonthFormat.format(clDate.getTime());
        String strYear = sdYearFormat.format(clDate.getTime());
        int intDaysInMth = clDate.getActualMaximum(5);
        strDateData = strDateData + "," + strDay + "," + strCurrDay + "," + strMonth + "," + strCurrMonth + "," + strYear + "," + strCurrYear + "," + intDaysInMth;
        return strDateData;
    }

    public String selectDate(String strDate, String strCalIconCss, String strMthSelCss, String strCalNextAndPrevYrBtnCss, String strMonthXpath, String strDayXPath, boolean blnEndDate) throws InterruptedException {
        Thread.sleep(1000L);
        this.wdriver.findElement(By.cssSelector(strCalIconCss)).click();
        String strDateData = this.getDateData(strDate);
        String[] strData = strDateData.split(",");
        double dblMonth = Double.parseDouble(strData[4]);
        int intDayVal = Integer.parseInt(strData[2]);
        Thread.sleep(1000L);
        if (blnEndDate) {
            this.actionElementInstanceNoWait("css", strMthSelCss, 2, "click", "");
        } else {
            this.actionElementInstance("css", strMthSelCss, 1, "click", "");
        }

        int intYear = Integer.parseInt(strData[6]);
        int intCurrYear = Integer.parseInt(strData[7]);
        double dblCurrMonth = Double.parseDouble(strData[5]);
        int intCurrDay = Integer.parseInt(strData[3]);
        int intDaysInMth = Integer.parseInt(strData[8]);
        String[] strNextPrev = strCalNextAndPrevYrBtnCss.split(",");
        String strNextBtncss = strNextPrev[0];
        String strPrevBtncss = strNextPrev[1];
        if (blnEndDate) {
            this.actionElementInstanceNoWait("css", strPrevBtncss, 3, "click", "");
        }

        if (intYear > intCurrYear) {
            this.actionElementInstance("css", strNextBtncss, 2, "click", "");
        }

        Thread.sleep(1000L);
        double dblMonthRow = Math.ceil(dblMonth / 4.0D);
        double dblButtonNo = dblMonth % 4.0D == 0.0D ? 4.0D : dblMonth % 4.0D;
        String strMonthRow = Integer.toString((int) dblMonthRow);
        String strButtonNo = Integer.toString((int) dblButtonNo);
        this.wdriver.findElement(By.xpath(strMonthXpath + strMonthRow + "]/button[" + strButtonNo + "]")).click();
        if (dblMonth == dblCurrMonth && intDayVal > intCurrDay) {
            --intDayVal;
        }

        if (blnEndDate) {
            intDayVal += intDaysInMth;
        }

        this.actionElementInstanceNoWait("xpath", strDayXPath, intDayVal, "click", "");
        return strData[0] + "," + strData[1];
    }

    public int arrayPos(String[] strFindPosWithin, String strFindText) {
        int intPos;
        for (intPos = 0; intPos < strFindPosWithin.length && !strFindPosWithin[intPos].equals(strFindText); ++intPos) {
        }

        return intPos + 1;
    }

    public static void driverquit(WebDriver driver) {
        driver.quit();
    }

    public boolean verifyTextPresent(String textToVerify) {
        this.logger.info("Verifying text present on page - " + textToVerify);

        try {
            return this.wdriver.getPageSource().contains(textToVerify);
        } catch (Exception var3) {
            this.logger.info("Text not present on page - " + textToVerify + "--" + var3.getMessage());
            return false;
        }
    }

    public void secondsDelay(int sec) {
        int timeinMilliSeconds;
        try {
            timeinMilliSeconds = sec * 1000;
            logger.info("##############################################");
            logger.info("Going for " + timeinMilliSeconds + " delay");
            logger.info("##############################################");
            Thread.sleep(timeinMilliSeconds);
        } catch (Exception e) {
            logger.info(e.getStackTrace());
        }
    }

    /* **********************************************************************
       Description of the function : Overloaded explicit wait.
       Author-CC312417/Sudhansu
     ***********************************************************************/
    public WebElement waitForElement(WebElement ele) {
        WebDriverWait wait = new WebDriverWait(wdriver, 60);
        wait.until(ExpectedConditions.visibilityOf(ele));
        return ele;
    }

    /* **********************************************************************
       Description of the function : Overloaded explicit wait.
       Author-CC312417/Sudhansu
     ***********************************************************************/
    public WebElement waitForElement(WebElement ele, int Time) {
        WebDriverWait wait = new WebDriverWait(wdriver, Time);
        wait.until(ExpectedConditions.visibilityOf(ele));
        return ele;
    }

    /* **********************************************************************
       Description of the function : Javascript click if normal click does not work.
       Author-CC312417/Sudhansu
     ***********************************************************************/
    public void javaScriptClick(WebElement ele) {
        JavascriptExecutor executor = (JavascriptExecutor) wdriver;
        executor.executeScript("arguments[0].click();", ele);
    }

    public void actionsClick(WebElement ele) {
        Actions actions = new Actions(wdriver);
        actions.moveToElement(ele).click().build().perform();
    }

    /* **********************************************************************
       Description of the function : Try clicking the element multiple times if there are errors like stale element etc.
       Author-CC312417/Sudhansu
     ***********************************************************************/
    public void clickAndTryMultiple(WebElement ele, int attempts) {
        boolean done = false;
        int iLoop = 0;
        while (!done && iLoop <= attempts) {
            try {
                Thread.sleep(1000);
                ele.click();
                done = true;
            } catch (Exception e) {
                iLoop++;
                if (iLoop == attempts) {
                    javaScriptClick(ele);
                }
            }
        }
    }

    /* **********************************************************************
       Description of the function : Try sendkeys multiple times if there are errors like stale element etc.
       Author-CC312417/Sudhansu
     ***********************************************************************/
    public void sendKeysAndTryMultiple(WebElement ele, String value, int attempts) {
        boolean done = false;
        int iLoop = 0;
        while (!done && iLoop <= attempts) {
            try {
                Thread.sleep(2000);
                ele.sendKeys(value);
                done = true;
            } catch (Exception e) {
                iLoop++;
            }
        }
    }

    public static String generatePassword(int length) {

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

        for (int i = 0; i < length - 4; i++) {
            password.append(allAllowed[random.nextInt(allAllowed.length)]);
        }

        //Ensure password policy is met by inserting required random chars in random positions
        password.insert(random.nextInt(password.length()), lowercase[random.nextInt(lowercase.length)]);
        password.insert(random.nextInt(password.length()), uppercase[random.nextInt(uppercase.length)]);
        password.insert(random.nextInt(password.length()), numbers[random.nextInt(numbers.length)]);
        password.insert(random.nextInt(password.length()), symbols[random.nextInt(symbols.length)]);

        return password.toString();
    }

    /* **********************************************************************
       Description of the function : Getting next month from calender with respect to current month
       Author-CC312417/Sudhansu
     ***********************************************************************/
    public String get_Next_Month() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.MONTH, 1);
        dt = c.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMMMMMMM");
        return formatter.format(dt);
    }

    /* ************************************************************************
       Description of the function : To slide a slider in UI by right arrow key
       Author-CC312417/Sudhansu
     *************************************************************************/
    public void Slider(WebElement SliderElement, int TimesSliderMoves) {
        for (int i = 1; i <= TimesSliderMoves; i++) {
            SliderElement.sendKeys(Keys.ARROW_RIGHT);
        }

    }

    /* ***********************************************************************
       Description of the function : To get a random no in the range specified
       Author-CC312417/Sudhansu
     ************************************************************************/
    public int RandomNo(int MaxRange) {
        Random rand = new Random();
        int n = rand.nextInt(MaxRange);
        n += 1;
        return n;
    }

}