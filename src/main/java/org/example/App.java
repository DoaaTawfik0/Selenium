package org.example;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

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
        TestDropDownPage(driver);


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
            new Select(dropDown3Element).selectByVisibleText("Egypt"); // do the selection without creating variable to point to..
        } catch (Exception e) {
            System.out.println("Selection Failed: " + e.getMessage());
        }
    }
}
