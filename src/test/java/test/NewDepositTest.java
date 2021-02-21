package test;

// this test fails because the add deposit fields are not coded
// this might change after today 2/20/2021

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NewDepositTest {

	WebDriver driver;

	@BeforeMethod
	public void launchBrowser() {
		System.setProperty("webdriver.chrome.driver", "./driver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("https://techfios.com/billing/?ng=login/");
	}

	/*
	 * @Test(priority = 2) public void userShouldBeAbleToAddNewDeposit() throws
	 * InterruptedException {
	 * 
	 * driver.findElement(By.xpath("//input[@type='text']")).sendKeys(
	 * "dem@techfios.com");
	 * driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(
	 * "abc123"); driver.findElement(By.xpath("//button[@type='submit']")).click();
	 * String expectedTitle = "Dashboard- iBilling";
	 * Assert.assertEquals(driver.getTitle(), expectedTitle,
	 * "Dashboard page did not display");
	 * 
	 * }
	 */

	@Test(priority = 1)
	public void userShouldBeAbleToAddNewDeposit2() throws InterruptedException {

		driver.findElement(By.xpath("//input[@type='text']")).sendKeys("demo@techfios.com");
		driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys("abc123");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		String expectedTitle = "Dashboard- iBilling";
		Assert.assertEquals(driver.getTitle(), expectedTitle, "Dashboard page did not display");

		// driver.findElement(By.xpath("//span[contains(text(),'Transactions')]")).click();
		// driver.findElement(By.xpath("//ul[@id='side-menu']/descendant::span[text()='Transactions']")).click();
		By TRANSACTIONS_MENU_LOCATOR = By.xpath("//ul[@id='side-menu']/descendant::span[text()='Transactions']");
		By NEW_DEPOSIT_PAGE_LOCATOR = By.linkText("New Deposit");

		// driver.findElement(By.xpath("//a[@href='https://techfios.com/billing/?ng=transactions/deposit/']")).click();
		driver.findElement(TRANSACTIONS_MENU_LOCATOR).click();
		waitForElement(NEW_DEPOSIT_PAGE_LOCATOR, driver, 20);
		driver.findElement(NEW_DEPOSIT_PAGE_LOCATOR).click();

		// Select the type of Account to add deposit
		Select select = new Select(driver.findElement(By.cssSelector("select#account")));
		select.selectByVisibleText("Swimming");
		Thread.sleep(3000);

		// Type a unique description with random numbers
		String expectedDescription = "AutomationTest" + new Random().nextInt(999);
		driver.findElement(By.id("description")).sendKeys(expectedDescription);

		// Enter the amount and click submit
		driver.findElement(By.id("amount")).sendKeys("100,000");
		driver.findElement(By.id("submit")).click();

		// wait for the element to disappear from the deposit entry fiel
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[contains@class,'blockUI']")));

		// validate with explicit wait
		waitForElement(By.linkText(expectedDescription), driver, 60);

		// Inspect the items posted for the first expected description
		List<WebElement> descriptionElements = driver.findElements(By.xpath("//table/descendant::a"));
		descriptionElements.get(0).getText();
		Thread.sleep(3000);

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("scroll(0,800)");// scroll down
		js.executeScript("scroll(0,-800)");// scroll up

		// validate with assert
		Assert.assertTrue(isDescriptionMatching(expectedDescription, descriptionElements), "Deposit unsuccessful");

	}

	private boolean isDescriptionMatching(String expectedDescription, List<WebElement> descriptionElements) {
		for (int i = 0; i < descriptionElements.size(); i++) {
			if (expectedDescription.equalsIgnoreCase(descriptionElements.get(i).getText())) {
				return true;
			}
		}
		return false;
	}

	private void waitForElement(By locator, WebDriver driver1, int time) {
		new WebDriverWait(driver1, time).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));

	}

	@AfterMethod
	public void closeEverything() {
		driver.close();
		driver.quit();

	}
}
