package org.example;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import javax.swing.plaf.nimbus.State;
import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;

public class App {
    public static void main(String[] args) throws InterruptedException {

        ChromeDriver driver;
        driver = new ChromeDriver();

        /*=******  Test Login Page  *********=*/
        //TestLoginPage(driver);

        /*=******  Test Radio Buttons Page  *********=*/
        //TestRadioButtons(driver);

        /*=******  Test Check Box Page  *********=*/
        //TestCheckBoxPage(driver);

        /*=******  Test Drop Down Page  *********=*/
        //TestDropDownPage(driver);

        /*=******  Test Dynamically loading elements  *********=*/
        //TestDynamicLoadingPage(driver);

        /*=******  Test Alerts and pop UPs elements *********=*/
        TestAlerts(driver, "Alert with Textbox ");

    }

    static void TestLoginPage(ChromeDriver driver) throws InterruptedException {


        driver.get("https://practice.expandtesting.com/login");

        driver.manage().window().maximize();

        By userNameLocator = By.id("username");
        By passwordLocator = By.id("password");
        WebElement loginButtonElement = driver.findElement(By.xpath("//button[@type='submit']"));
        WebElement userNameElement = driver.findElement(By.xpath("//li/b[text()='practice']"));
        WebElement passwordElement = driver.findElement(By.xpath("//li/b[text()='SuperSecretPassword!']"));

        String userName = userNameElement.getText();
        String password = passwordElement.getText();


        driver.findElement(userNameLocator).sendKeys(userName);
        driver.findElement(passwordLocator).sendKeys(password);
//      loginButtonElement.click();   /*This one does not work due to invisibility of element*/
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginButtonElement);   /*this one works as it Triggers the click event programmatically rather than simulating a physical mouse click*/

        Thread.sleep(2500);
        driver.close();

    }

    static void TestRadioButtons(ChromeDriver driver) throws InterruptedException {
        driver.get("https://practice.expandtesting.com/radio-buttons");

        String color = "blue";
        String sport = "football";

        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); //an explicit wait (dynamic wait) with a 10-second timeout

        WebElement colorElement = wait.until(ExpectedConditions.elementToBeClickable(By.id(color))); // Wait for the color radio button to be BOTH present in DOM AND clickable
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", colorElement);// Use JavaScript to click the element

        WebElement sportElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(sport))); // Wait for the sport radio button to be present in DOM
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sportElement);

    }

    static void TestCheckBoxPage(ChromeDriver driver) throws InterruptedException {
        driver.get("https://practice.expandtesting.com/checkboxes");

        // Wait for checkboxes to be present
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> checkBoxElements = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//input[@type='checkbox']"))
        );

        // Print initial state
        System.out.println("---------------------------  Before  --------------------");
        for (WebElement element : checkBoxElements) {
            System.out.println("Checkbox ID: " + element.getDomAttribute("id"));
            System.out.println("Selected: " + element.isSelected());
            System.out.println("------------------------------------------------------");
        }

        // Toggle all checkboxes
        for (WebElement element : checkBoxElements) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }

        // Print final state
        System.out.println("---------------------------  After  --------------------");
        for (WebElement element : checkBoxElements) {
            System.out.println("Checkbox ID: " + element.getDomAttribute("id"));
            System.out.println("Selected: " + element.isSelected());
            System.out.println("------------------------------------------------------");
        }
    }

    static void TestDropDownPage(ChromeDriver driver) {
        driver.get("https://practice.expandtesting.com/dropdown");

        By dropDown1Locator = By.xpath("//select[@id='dropdown']");
        By dropDown2Locator = By.id("elementsPerPageSelect");
        By dropDown3Locator = By.xpath("//select[@name='country' and @id='country']");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            WebElement dropDown1Element = wait.until(ExpectedConditions.presenceOfElementLocated(dropDown1Locator));
            Select dropdown1 = new Select(dropDown1Element);
            dropdown1.selectByIndex(2);

            WebElement dropDown2Element = wait.until(ExpectedConditions.presenceOfElementLocated(dropDown2Locator));
            Select elementsPerPage = new Select(dropDown2Element);
            elementsPerPage.selectByValue("100");

            WebElement dropDown3Element = wait.until(ExpectedConditions.presenceOfElementLocated(dropDown3Locator));
            new Select(dropDown3Element).selectByVisibleText("Egypt"); // do the selection without creating variable to point to.
        } catch (Exception e) {
            System.out.println("Selection Failed: " + e.getMessage());
        }
    }

    /**
     * @deprecated This implementation has several critical issues:
     * 1. Doesn't properly handle dynamic ID changes after page refresh
     * 2. cause TimeoutException
     */
    @Deprecated
    static void TestDynamicIdPage(ChromeDriver driver) {
        driver.get("https://practice.expandtesting.com/dynamic-id#google_vignette");

        By dynamicButtonLocator = By.xpath("//button[text() = 'Button with Dynamic ID' and @type='button']");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));


        WebElement button1 = wait.until(ExpectedConditions.presenceOfElementLocated(dynamicButtonLocator));
        String id1 = button1.getDomAttribute("id");
        System.out.println("Second ID: " + id1);


        // Refresh page to get new dynamic ID
        driver.navigate().refresh();

        // Get second ID
        WebElement button2 = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[contains(text(), 'Button with Dynamic ID')]")));
        String id2 = button2.getDomAttribute("id");
        System.out.println("Second ID: " + id2);
    }

    static void TestDynamicLoadingPage(ChromeDriver driver) {
        driver.get("https://practice.expandtesting.com/dynamic-loading");

        By hiddenElementLocator = By.xpath("//a[@href='/dynamic-loading/1']");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        WebElement hiddenElement = wait.until(ExpectedConditions.elementToBeClickable(hiddenElementLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", hiddenElement);

        By startButtonLocator = By.xpath("//button[text()='Start']");

        WebElement startButton = wait.until(ExpectedConditions.presenceOfElementLocated(startButtonLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", startButton);

        // Wait for loading to complete and verify the result
        By resultLocator = By.xpath("//div[@id='finish']/h4");
        WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(resultLocator));

        String resultText = result.getText();

        if (!resultText.equals("Hello World!")) {
            throw new AssertionError("Expected 'Hello World!' but got '" + resultText + "'");
        }
        System.out.println("Dynamic loading test passed successfully!");


    }

    static void TestAlerts(ChromeDriver driver, String tabId) throws InterruptedException {
        driver.get("https://demo.automationtesting.in/Alerts.html");

        // Map alert types to their corresponding tab IDs
        Map<String, String> alertTypeToId = new HashMap<>();
        alertTypeToId.put("Alert with OK ", "OKTab");
        alertTypeToId.put("Alert with OK & Cancel ", "CancelTab");
        alertTypeToId.put("Alert with Textbox ", "Textbox");


        By alertTypeLocator = By.xpath("//a[text()='" + tabId + "' and @class='analystic']");
        By alertLocator = By.xpath("//div[@id = '" + alertTypeToId.get(tabId) + "']/button"); // Actual button text

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement typeButton = wait.until(ExpectedConditions.elementToBeClickable(alertTypeLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", typeButton);

        WebElement alertButton = wait.until(ExpectedConditions.presenceOfElementLocated(alertLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", alertButton);

        Alert alert = wait.until(ExpectedConditions.alertIsPresent()); // Waits for alert to appear
        if (tabId.equals("Alert with Textbox ")) {
            alert.sendKeys("Hello");
        }
        Thread.sleep(2000);

        alert.accept();

    }


}
