package edu.ncsu.csc.itrust.cucumber;

import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import cucumber.api.java.en.*;

/**
 * A Selenium test for viewing basic health information
 *
 */
public class ViewHealthInfoStepDefs {
	
	private WebDriver driver;
	private String baseUrl;
	private String actMsg;
	private String patientName;
	private String patientMid;
	
	public ViewHealthInfoStepDefs() {
		driver = new HtmlUnitDriver();
		baseUrl = "http://localhost:8080/iTrust/";
	}
	
	@Given("^I have logged in as HCP: (.+) with password: (.+)$")
	public void valid_login(String userName, String password ) {
		// access home page
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		driver.get(baseUrl + "auth/forwardUser.jsp");
		
		// input username and password
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(userName);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(password);
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		
		// check that homepage is accessed
		try {
			assertEquals("iTrust - HCP Home", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
		
	}
	
	@And("^I select \"iTrust - Basic Health Information\"$")
	public void nav_to_page() {
		// select Basic Health Info in sidebar
		driver.findElement(By.cssSelector("h2.panel-title")).click();
		driver.findElement(By.linkText("Basic Health Information")).click();
		try {
			assertEquals("iTrust - Please Select a Patient", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@When("^I input values name (.+), MID (.+) and select the patient$")
	public void enter_valid_values(String name, String mid) {
		patientName = name;
		patientMid = mid;
		// input name and MID
		driver.findElement(By.name("FIRST_NAME")).sendKeys(patientName);
		driver.findElement(By.name("NAME_SUBMIT")).click();
		// select patient button
		driver.findElement(By.name("user_"+patientMid)).click();
		try {
			assertEquals("iTrust - View Patient Records", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}

	@Then("^I can see the health information of patient (.+) (.+)$") 
	public void info_updated_correct(String firstName, String lastName) {
		// check that page title includes name of patient
		actMsg = driver.findElement(By.id("header")).getText();
		try {
			assertEquals(actMsg, "Basic Health Information of " +lastName+", "+firstName);
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	
}