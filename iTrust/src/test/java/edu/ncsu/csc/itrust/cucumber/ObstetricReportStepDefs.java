package edu.ncsu.csc.itrust.cucumber;

import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import cucumber.api.java.*;
import cucumber.api.java.en.*;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ObstetricReportStepDefs {

	public static final String ADDRESS = "http://localhost:8080/iTrust/";
	protected TestDataGenerator gen = new TestDataGenerator();
	private WebDriver driver;
	private String date;
	
	@Before
	public void setUp() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		driver = new HtmlUnitDriver(); // true to enable Javascript
		driver.get(ADDRESS);
	}
	
	@Given("^I logged in with MID 9000000012 and password pw as an OBGYN HCP$")
	public void or_log_in() {
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		WebElement user = driver.findElement(By.name("j_username"));
		WebElement pass = driver.findElement(By.name("j_password"));
		user.sendKeys("9000000012");
		pass.sendKeys("pw");
		pass.submit();
		// ensure logged in
		if (driver.getTitle().equals("iTrust - Login")) {
			Assert.fail("Error logging in");
		}
	}
	
	@And("^I see patient Aaron with MID 85 has an obstetric initialization$")
	public void or_init() {
		driver.findElement(By.cssSelector("h2.panel-title")).click();
		driver.findElement(By.linkText("Obstetric Care")).click();
		try {
			assertEquals("iTrust - Please Select a Patient", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
		driver.findElement(By.name("LAST_NAME")).clear();
		driver.findElement(By.name("LAST_NAME")).sendKeys("Nicholson");
		driver.findElement(By.name("NAME_SUBMIT")).click();
		try {
			driver.findElement(By.name("user_85")).click();
			assertEquals("iTrust - Obstetrics Care", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
		try {
			WebElement baseTable = driver.findElement(By.id("PIRTable"));
			List<WebElement> tableRow = baseTable.findElements(By.tagName("td"));
			date = tableRow.get(2).getText();
		} catch (Error e) {	
			Assert.fail(e.getMessage());
		}
	}
	
	@And("^I also see an OBGYN office visit$")
	public void or_visit() {
		driver.findElement(By.cssSelector("h2.panel-title")).click();
		driver.findElement(By.linkText("Document Office Visit")).click();
		try {
			assertEquals("Document Office Visit", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
		try {
			WebElement baseTable = driver.findElement(By.id("previousVisits"));
			List<WebElement> tableRow = baseTable.findElements(By.tagName("td"));
			assertEquals("OB/GYN", tableRow.get(1).getText());
		} catch (Error e) {	
			Assert.fail(e.getMessage());
		}
	}
	
	@When("^I go to Labor and Delivery Report$")
	public void or_report() {
		driver.findElement(By.cssSelector("h2.panel-title")).click();
		driver.findElement(By.linkText("Labor and Delivery Report")).click();
		try {
			assertEquals("iTrust - Please Select a Patient", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
		driver.findElement(By.name("LAST_NAME")).clear();
		driver.findElement(By.name("LAST_NAME")).sendKeys("Nicholson");
		driver.findElement(By.name("NAME_SUBMIT")).click();
		try {
			driver.findElement(By.name("user_85")).click();
			assertEquals("iTrust - Obstetrics Report", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Then("^I can see all above information is in the report$")
	public void or_view() {
		try {
			String edd = driver.findElement(By.id("edd")).getText();
			assertEquals(date, edd);
			String bloodType = driver.findElement(By.id("bloodType")).getText();
			assertEquals("AB+", bloodType);
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@And("^I select Obstetrics Info, then go to Labor and Delivery Report$")
	public void or_vr() {
		driver.findElement(By.cssSelector("h2.panel-title")).click();
		driver.findElement(By.linkText("Labor and Delivery Report")).click();
		try {
			assertEquals("iTrust - Please Select a Patient", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@When("^I type in patient's MID 1111$")
	public void or_select_1111() {
		driver.findElement(By.name("FIRST_NAME")).clear();
		driver.findElement(By.name("FIRST_NAME")).sendKeys("1111");
		driver.findElement(By.name("NAME_SUBMIT")).click();
	}
	
	@Then("^the patient is not recorded in the system$")
	public void or_invalid_patient() {
		String search_result = driver.findElement(By.className("searchResults")).getText();
		assertEquals("Found 0 Records", search_result);
	}
	
	@When("^I type in patient's MID 12$")
	public void or_select_12() {
		driver.findElement(By.name("FIRST_NAME")).clear();
		driver.findElement(By.name("FIRST_NAME")).sendKeys("Volcano");
		driver.findElement(By.name("NAME_SUBMIT")).click();
		try {
			driver.findElement(By.name("user_12")).click();
			assertEquals("iTrust - Obstetrics Report", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Then("^the patient is not eligible for obstetric care$")
	public void or_ineligible_patient() {
		String message = driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/span")).getText();
		assertTrue(message.contains("is ineligible for obstetric care or does not have an obstetrics initialization"));
	}
}
