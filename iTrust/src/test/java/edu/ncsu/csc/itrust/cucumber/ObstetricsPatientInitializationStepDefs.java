package edu.ncsu.csc.itrust.cucumber;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.Before;
import cucumber.api.java.en.*;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ObstetricsPatientInitializationStepDefs {

	public static final String ADDRESS = "http://localhost:8080/iTrust/";
	protected TestDataGenerator gen = new TestDataGenerator();
	private WebDriver driver;
	
	@Before
	public void setUp() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		driver = new HtmlUnitDriver(); // true to enable Javascript
		driver.get(ADDRESS);
	}
	
	@Given("^I logged in as an OBGYN HCP with MID (.*) and password (.*)$")
	public void OBGYN_log_in(String hcp, String pw) {
		// log in using the given username and password
				driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
				WebElement user = driver.findElement(By.name("j_username"));
				WebElement pass = driver.findElement(By.name("j_password"));
				user.sendKeys(hcp);
				pass.sendKeys(pw);
				pass.submit();
				// ensure logged in
				if (driver.getTitle().equals("iTrust - Login")) {
					Assert.fail("Error logging in");
				}
	}
	
	@And("^I select Obstetrics Info, then go to Patient Obstetric Initialization$")
	public void select_obstetric_initialization() {
		driver.findElement(By.cssSelector("h2.panel-title")).click();
		driver.findElement(By.linkText("Patient Obstetric Initialization")).click();
		try {
			assertEquals("iTrust - Please Select a Patient", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@And("^I enter patient's MID (.*) and name (.*) and select the patient$")
	public void select_patient(String patient, String name) {
		driver.findElement(By.name("FIRST_NAME")).clear();
		driver.findElement(By.name("FIRST_NAME")).sendKeys(name);
		driver.findElement(By.name("NAME_SUBMIT")).click();
		try {
			driver.findElement(By.name("user_" + patient)).click();
			assertEquals("iTrust - View Obstetrics Initilizations", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@And("^I realize it is a wrong patient, so I click on Select a Different Patient$")
	public void wrong_patient() {
		driver.findElement(By.xpath("//*[@id=\"j_idt9\"]/span/a")).click();
		List<String> browserTabs = new ArrayList<String> (driver.getWindowHandles());            
		driver.switchTo().window(browserTabs.get(1));
	}
	
	@And("^I enter patient's name (.*) and the patient is not in the system$")
	public void invalid_patient(String name) {	
		driver.findElement(By.name("FIRST_NAME")).clear();
		driver.findElement(By.name("FIRST_NAME")).sendKeys(name);
		driver.findElement(By.name("NAME_SUBMIT")).click();
		try {
			assertEquals("iTrust - Please Select a Patient", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@And("^I click on create new initialization$")
	public void new_initialization() {
		driver.findElement(By.name("j_idt27:createPatientInitRecord")).click();
		try {
			WebElement element = driver.findElement(By.name("obgyn_controller"));
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@When("^I enter the patient's LMP (.*)$")
	public void edit_LMP(String date) throws ParseException {
		driver.findElement(By.name("obgyn_controller:datepicker")).clear();
		driver.findElement(By.name("obgyn_controller:datepicker")).sendKeys(date);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date edd = format.parse(date);
		Calendar c = Calendar.getInstance();
		c.setTime(edd);
		c.add(Calendar.DATE, 280);
		edd = c.getTime();
		String EDD = format.format(edd.getTime());
		assertEquals("Expected Delivery Date: " + EDD, driver.findElement(By.xpath("//*[@id=\"obgyn_controller\"]/table/tbody/tr[2]/td")).getText());
	}
	
	@When("^I select the patient's initialization at (.*)$")
	public void select_initialization(String time) {
		driver.findElement(By.xpath("//*[@id=\"PIRTable\"]/tbody/tr[1]/td/a")).click();
	}
	
	@When("^I re-enter the patient's MID (.*) and name (.*) and select the patient$")
	public void re_enter_patient(String rpatient, String rname) {
		try {
			assertEquals("iTrust - Please Select a Patient", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
		driver.findElement(By.name("FIRST_NAME")).clear();
		driver.findElement(By.name("FIRST_NAME")).sendKeys(rname);
		driver.findElement(By.name("NAME_SUBMIT")).click();
		// select patient button
		driver.findElement(By.name("user_" + rpatient)).click();
	}
	
	@And("^I add a new prior pregnancy, Year of conception (.*), Number of week pregnant (.*), Number of hours in labor (.*), Weight gained during pregnancy (.*), Delivery type (.*), Number of multiple (.*)$")
	public void prior_pregnancy(String yc, String wk, String hrs, String gain, String type, String num) {
		driver.findElement(By.name("obgyn_controller:newPreg")).click();
		driver.findElement(By.name("yearOfConception")).sendKeys(yc);
		driver.findElement(By.name("numberOfWeeks")).sendKeys(wk);
		driver.findElement(By.name("numberOfHours")).sendKeys(hrs);
		driver.findElement(By.name("weightGain")).sendKeys(gain);
		Select delivery = new Select(driver.findElement(By.xpath("delivery_drop_down")));
		delivery.deselectAll();
		delivery.selectByVisibleText(type);
		Select number = new Select(driver.findElement(By.xpath("number_drop_down")));
		number.deselectAll();
		number.selectByValue(num);
	}
	
	@And("^I click Calculate Estimated Delivery Date, And Save Initilization and Return to Obstetrics Home$")
	public void create_initialization() {
		try {
			driver.findElement(By.name("obgyn_controller:submitDateButton")).click();
			driver.findElement(By.name("obgyn_controller:returnOBHome")).click();
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Then("^a new obstetric initialization is created (.*)$")
	public void initialization_created(String time) {
		try {
			assertEquals("iTrust - View Obstetrics Initilizations", driver.getTitle());
			driver.findElement(By.linkText(time)).click();
			assertEquals("iTrust - View Single Initilization", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Then("^I can view the initialization$")
	public void view_initialization() {
		try {
			assertEquals("iTrust - View Single Initilization", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	@Then("^I can see the Patient's obstetric initialization page$")
	public void obstetrics_page() {
		try {
			assertEquals("iTrust - View Obstetrics Initilizations", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Then("^the system asked to enter correct prior pregnancy info$")
	public void incorrect_pregnancy_info() {
		try {
			driver.findElement(By.name("obgyn_controller:submitDateButton")).click();
			assertEquals("pregnancy info is not corret", driver.findElement(By.name("error_message")).getText());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}

}
