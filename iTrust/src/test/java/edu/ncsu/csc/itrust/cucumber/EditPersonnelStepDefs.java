package edu.ncsu.csc.itrust.cucumber;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

/**
 * Tests editMyDemographics page for staff with valid inputs.
 * @author mcgibson
 */
public class EditPersonnelStepDefs {
	
	public static final String ADDRESS = "http://localhost:8080/iTrust/";
	protected TestDataGenerator gen = new TestDataGenerator();
	private WebDriver driver;
	
	public EditPersonnelStepDefs() throws Exception {

	}
	
	/**
	 * taken from edu.ncsu.csc.itrust.selenium.AppointmentTypeTest.java
	 */
	@Before
	public void setUp() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		driver = new HtmlUnitDriver(); // true to enable Javascript
		driver.get(ADDRESS);
	}

	@Given("^I log in as the HCP with MID (.*) and password (.*)$")
	public void hcp_is_an_HCP_with_MID(String username, String password) throws Throwable {
		// log in using the given username and password
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		WebElement user = driver.findElement(By.name("j_username"));
		WebElement pass = driver.findElement(By.name("j_password"));
		user.sendKeys(username);
		pass.sendKeys(password);
		pass.submit();
		// ensure logged in
		if (driver.getTitle().equals("iTrust - Login")) {
			Assert.fail("Error logging in");
		}
	}
	
	@When("^I enter a (.+), (.+), (.+), (.+), (.+), (.+), (.+), and (.+) to the edit page$")
	public void editInfo(String validHCPFirstName, String validHCPLastName, String validHCPAddress, String validCity, String validState, String validZip, String validPhone, String validEmail) {
		driver.get(ADDRESS + "auth/staff/editMyDemographics.jsp");
		driver.findElement(By.name("firstName")).clear();
		driver.findElement(By.name("firstName")).sendKeys(validHCPFirstName);
		driver.findElement(By.name("lastName")).clear();
		driver.findElement(By.name("lastName")).sendKeys(validHCPLastName);
		driver.findElement(By.name("streetAddress1")).clear();
		driver.findElement(By.name("streetAddress1")).sendKeys(validHCPAddress);
		driver.findElement(By.name("city")).clear();
		driver.findElement(By.name("city")).sendKeys(validCity);
		driver.findElement(By.name("zip")).clear();
		driver.findElement(By.name("zip")).sendKeys(validZip);
		driver.findElement(By.name("phone")).clear();
		driver.findElement(By.name("phone")).sendKeys(validPhone);
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys(validEmail);
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
	}
	
	@Then("^the staff page should say Information Successfully Updated with (\\d+)$")
	public void checkInfo(long MID) throws InterruptedException {
		Assert.assertTrue(driver.getPageSource().contains("Information Successfully Updated"));
	}
}