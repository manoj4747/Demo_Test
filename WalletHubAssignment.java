package com.synchronoss.fa.cost_analytics.test.search.DisputeHistoryRegression;
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

public class WalletHubAssignment {

	public static WebDriver driver;
	static String urlJoin="https://wallethub.com/join/light";
	static String reviewurl="http://wallethub.com/profile/test_insurance_company/";
	static String verificationUrl="https://wallethub.com/profile/manojpeterdn1991/reviews/";

	//username and password for login
	String username="manojpeterdn1991@gmail.com";
	String password="";//password to login


	@BeforeMethod
	public void setup(){

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-notifications");
		String chromeDriverPath = ("C:\\webdriver\\chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);

		//set chrome driver path here
		//System.setProperty("webdriver.chrome.driver", "Drivers\\chromedriver.exe");
		driver=new ChromeDriver(options);
		driver.get(urlJoin);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

	}

	@Test
	public void postReviewOnWalletHub() throws InterruptedException{

		//Clicking on Login animate, entering credentials and logging in
		WebElement loginOption=driver.findElement(By.xpath("//li/a[contains(text(),'Login')]"));
		loginOption.click();

		WebElement emailId=driver.findElement(By.xpath("//input[@type='text' and contains(@placeholder,'Email')]"));
		emailId.sendKeys(username);

		WebElement password=driver.findElement(By.xpath("//input[@type='password' and contains(@placeholder,'Password')]"));
		password.sendKeys(password); Thread.sleep(2000);


		//Login button
		WebElement loginBtn=driver.findElement(By.xpath("//button//span[contains(text(),'Login')]"));
		loginBtn.click(); Thread.sleep(5000);

		//routing to review submission page
		driver.navigate().to(reviewurl);

		//hovering and selecting 4th star
		WebElement starsReview=driver.findElement(By.xpath("(//*[contains(@class,'rvs-star-svg')])[14]"));
		Actions builder=new Actions(driver);
		builder.moveToElement(starsReview); Thread.sleep(2000);
		builder.moveToElement(starsReview).click().perform();Thread.sleep(2000);

		//click on dropdown
		WebElement oSelectDropdown=driver.findElement(By.xpath("(//*[@class='dropdown-placeholder'])[2]"));
		oSelectDropdown.click(); Thread.sleep(2000);

		//select Health Insurance value
		WebElement oHealthInsurance=driver.findElement(By.xpath("//li[contains(text(),'Health Insurance')]"));
		oHealthInsurance.click(); Thread.sleep(2000);

		//clear review text area and adding reviews
		WebElement reviewArea=driver.findElement(By.xpath("//*[@id='reviews-section']//textarea"));
		reviewArea.clear();

		String reviewMessage = "";
		for (int i = 0; i < 20; i++) {
			reviewMessage += "WalletHub test!";
		}

		reviewArea.sendKeys(reviewMessage);

		//click on submit button
		WebElement oSubmit=driver.findElement(By.xpath("//*[@class='sub-nav-ct']/div[text()='Submit']"));
		oSubmit.click();



		WebDriverWait webDriverWait = new WebDriverWait(driver, 30000);
		//checking confirmation
		webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//h4[text()='Your review has been posted.']"))));

		//assert the review added by the user.
		driver.navigate().to(verificationUrl);
		String displayedText = driver.findElement(By.tagName("body")).getText();
		Assert.assertTrue(displayedText.contains(reviewMessage), "Review is not displayed in profile/review page, Failing Test!");
		System.out.println("Review by user is dispalyed on profile/review page, Test passed !!");

	}


	@AfterMethod
	public void resultProof(ITestResult result){

		if(ITestResult.FAILURE==result.getStatus()){
			try{
				// To create reference of TakesScreenshot
				TakesScreenshot screenshot=(TakesScreenshot)driver;
				// Call method to capture screenshot
				File src=screenshot.getScreenshotAs(OutputType.FILE);
				// Copy files to specific location
				//screenshot name is same name as  test case name
				FileUtils.copyFile(src, new File("screenshots\\"+result.getName()+".png"));
				System.out.println("Successfully screenshot captured !");
			}catch (Exception e){
				System.out.println("Exception while taking screenshot "+e.getMessage());
			}
		}
		driver.quit();

	}
}

