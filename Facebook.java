package tests;
import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Facebook
{
    public static WebDriver driver;
    static String baseurl="http://www.facebook.com";
    //change username and password here for login
    String phoneNumber="9742776357";
    String password=""; //add password to login
    String post_msg="Hello World";

    @BeforeMethod
    public void setup(){

        //disabling chrome notifications
        ChromeOptions options = new ChromeOptions();
        options.addArguments("disable-notifications");
        //launch incognito mode
        options.addArguments("incognito");

        //set chrome driver path here
        String chromeDriverPath = ("C:\\webdriver\\chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);


        //set chrome driver path here
        //System.setProperty("webdriver.chrome.driver", "Drivers\\chromedriver.exe");
        driver=new ChromeDriver(options);
        driver.get(baseurl);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

    }


    @Test
    public void Facebook() throws InterruptedException {


        //click on English (UK) to change into english language
        WebElement oEnglish = driver.findElement(By.xpath("//a[text()='English (UK)']"));
        oEnglish.click();

        //enter email or phonenumber
        WebElement oEmail = driver.findElement(By.id("email"));
        oEmail.sendKeys(phoneNumber);

        //Enter password
        WebElement oPassword = driver.findElement(By.id("pass"));
        oPassword.sendKeys(password);

        //
        WebElement oLoginBtn = driver.findElement(By.xpath("//button[text()='Log In']"));
        oLoginBtn.click();

        Thread.sleep(3000);
        String title = driver.getTitle();
        if (title.equalsIgnoreCase("facebook")) {
            System.out.println("Logged in sucessfully");
        }

        //Click on What's on Your Mind?
        WebElement oTextArea = driver.findElement(By.cssSelector("div[role='button'] > div > span[style*='webkit-box-orient']"));
        oTextArea.click();
        Thread.sleep(3000);

        //Click on the expanded text area
        WebElement oTextAreaExpanded = driver.findElement(By.cssSelector("div[aria-describedby*='placeholder']"));
        oTextAreaExpanded.click();
        oTextAreaExpanded.sendKeys(post_msg);


        //Click On Post Button
        WebElement oPostBtn = driver.findElement(By.cssSelector("div[aria-label='Post']"));
        Actions builder = new Actions(driver);
        builder.moveToElement(oPostBtn).build().perform();
        oPostBtn.click();
        Thread.sleep(3000);
        //Assert
        Assert.assertTrue(driver.getPageSource().contains(post_msg), post_msg + " status message post has been failed, Failing Test!\n\n");

        System.out.println(post_msg + " status message has been sucessfully posted, Pass!\n\n");
    }


    @AfterMethod
    public void teardown(ITestResult result){

        if(ITestResult.FAILURE==result.getStatus()){

            try{

                // To create reference of TakesScreenshot
                TakesScreenshot screenshot=(TakesScreenshot)driver;
                // Call method to capture screenshot
                File src=screenshot.getScreenshotAs(OutputType.FILE);

                // Copy files to specific location
                // result.getName() will return name of test case so that screenshot name will be same as test case name
                FileUtils.copyFile(src, new File("screenshots\\"+result.getName()+".png"));
                System.out.println("Successfully captured a screenshot");

            }catch (Exception e){

                System.out.println("Exception while taking screenshot "+e.getMessage());
            }
        }
        driver.quit();

    }

}