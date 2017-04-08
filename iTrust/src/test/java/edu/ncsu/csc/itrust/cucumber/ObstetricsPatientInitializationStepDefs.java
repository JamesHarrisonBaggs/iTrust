package edu.ncsu.csc.itrust.cucumber;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;

import cucumber.api.java.Before;
import cucumber.api.java.en.*;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ObstetricsPatientInitializationStepDefs {

	public static final String ADDRESS = "http://localhost:8080/iTrust/";
	protected TestDataGenerator gen = new TestDataGenerator();
	private WebDriver driver;

	// ignore pesky CSS errors
	private class SilentHtmlUnitDriver extends HtmlUnitDriver {
		public SilentHtmlUnitDriver() {
			super();
			this.getWebClient().setCssErrorHandler(new SilentCssErrorHandler());
		}
	}

	@Before
	public void setUp() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		driver = new SilentHtmlUnitDriver(); // true to enable Javascript
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

	@And("^I select Obstetrics Info, then go to Obstetric Care$")
	public void select_obstetric_initialization() {
		driver.findElement(By.cssSelector("h2.panel-title")).click();
		driver.findElement(By.linkText("Obstetric Care")).click();
		try {
			assertEquals("iTrust - Please Select a Patient", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}

	@And("^I type in patient's MID (.*) and name (.*) and select the patient$")
	public void select_patient(String patient, String name) {
		driver.findElement(By.name("FIRST_NAME")).clear();
		driver.findElement(By.name("FIRST_NAME")).sendKeys(name);
		driver.findElement(By.name("NAME_SUBMIT")).click();
		driver.findElement(By.name("user_" + patient)).click();
		assertEquals("iTrust - Obstetrics Care", driver.getTitle());
	}

	@And("^I realize it is a wrong patient, so I click on Select a Different Patient$")
	public void wrong_patient() {
		driver.navigate().back();
		// driver.findElement(By.xpath("//*[@id=\"j_idt9\"]/span/a")).click();
		// List<String> browserTabs = new ArrayList<String>
		// (driver.getWindowHandles());
		// driver.switchTo().window(browserTabs.get(0));
	}

	@And("^I enter patient's name (.*) and the patient is not in the system$")
	public void invalid_patient(String name) {
		driver.findElement(By.name("FIRST_NAME")).clear();
		driver.findElement(By.name("FIRST_NAME")).sendKeys(name);
		driver.findElement(By.name("NAME_SUBMIT")).click();
		assertEquals("iTrust - Please Select a Patient", driver.getTitle());
	}

	@And("^I click on create new initialization$")
	public void new_initialization() {
		driver.findElement(By.name("initButtonForm:createPatientInitRecord")).click();
		try {
			assertEquals("iTrust - View Obstetrics Initilizations", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}

	@When("^I enter the patient's LMP (.*)$")
	public void enter_last_menstrual_period(String date) {
		driver.findElement(By.name("newInitializationForm:lmp_input")).clear();
		driver.findElement(By.name("newInitializationForm:lmp_input")).sendKeys(date);
		driver.findElement(By.name("newInitializationForm:submitDateButton")).click();
	}

	@And("^I click Save Obstetrics Initialization$")
	public void save_obstetrics_initialization() {
		try {
			driver.findElement(By.name("newInitializationForm:submitDateButton")).click();
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}

	@And("I can see Expected Delivery Date and Weeks Pregnant (.*)$")
	public void view_edd_and_weekspregnant(String date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Date edDate = format.parse(date);
		Calendar c = Calendar.getInstance();
		c.setTime(edDate);
		c.add(Calendar.DATE, 280);
		edDate = c.getTime();
		String edd = format.format(edDate.getTime());
		assertEquals(edd, driver.findElement(By.id("newInitializationForm:edd")).getText());

		// TODO need to test weeks pregnant
		// assertEquals(wksPreg,
		// driver.findElement(By.id("newInitializationForm:tpp")).getText());
	}

	@And("I click Return to Obstetrics Care")
	public void return_to_obstetrics_care() {
		try {
			driver.findElement(By.name("newInitializationForm:returnOBHome")).click();
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}

	@Then("^a new obstetric initialization is created (.*)$")
	public void initialization_created(String time) {
		assertEquals("iTrust - Obstetrics Care", driver.getTitle());
		driver.findElement(By.linkText(time)).click();
		assertEquals("iTrust - View Single Initialization", driver.getTitle());
	}

	@When("^I select the patient's initialization at (.*)$")
	public void select_initialization(String time) {
		driver.findElement(By.xpath("//*[@id=\"PIRTable\"]/tbody/tr[1]/td/a")).click();
	}

	@When("^I re-enter the patient's MID (.*) and name (.*) and select the patient$")
	public void re_enter_patient(String rpatient, String rname) {
		assertEquals("iTrust - Please Select a Patient", driver.getTitle());
		driver.findElement(By.name("FIRST_NAME")).clear();
		driver.findElement(By.name("FIRST_NAME")).sendKeys(rname);
		driver.findElement(By.name("NAME_SUBMIT")).click();
		// select patient button
		driver.findElement(By.name("user_" + rpatient)).click();
	}

	@And("^I select record prior pregnancy$")
	public void record_prior_pregnancy() {
		driver.findElement(By.id("pregButtonForm:newPreg")).click();
	}

	@And("^I enter date of birth (.*), year of conception (.*), Number of week pregnant (.*), Number of hours in labor (.*), Weight gained during pregnancy (.*), Delivery type (.*), Number of multiple (.*)$")
	public void enter_pregnancy_data(String dob, String yc, String wk, String hrs, String gain, String type,
			String num) {
		System.err.println(dob + "," + yc + "," + wk + "," + hrs + "," + gain + "," + type + "," + num);
		driver.findElement(By.id("pregnancyForm:dob")).sendKeys(dob);
		driver.findElement(By.id("pregnancyForm:yearOfConception")).sendKeys(yc);
		driver.findElement(By.id("pregnancyForm:numberOfWeeks")).sendKeys(wk);
		driver.findElement(By.id("pregnancyForm:numberOfHours")).sendKeys(hrs);
		driver.findElement(By.id("pregnancyForm:weightGain")).sendKeys(gain);
		Select delivery = new Select(driver.findElement(By.id("pregnancyForm:delivery_drop_down")));
		delivery.selectByVisibleText(type);
		Select number = new Select(driver.findElement(By.id("pregnancyForm:multiple_drop_down")));
		number.selectByVisibleText(num);
	}

	@And("^I select create new prior pregnancy$")
	public void create_new_pregnancy_return() {
		// create pregnancy
		driver.findElement(By.id("pregnancyForm:createNewPreg")).click();
		// verify no errors
		WebElement element = driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/span"));
		assertEquals("Prior pregnancy added", element.getText());
	}

	@And("^I select return to obstetrics care$")
	public void return_from_pregnancy() {
		driver.findElement(By.id("pregnancyForm:returnToOBCare")).click();
		assertEquals("iTrust - Obstetrics Care", driver.getTitle());
	}

	@Then("^a new pregnancy is created (.*)$")
	public void new_pregnancy_created(String date) {
		WebElement element = driver.findElement(By.xpath("//*[@id=\"preg_table\"]/tbody/tr[1]/td[1]"));
		assertEquals(date, element.getText());
	}

	@Then("^I can view the initialization$")
	public void view_initialization() {
		try {
			assertEquals("iTrust - View Single Initialization", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}

	@Then("^I can see the Patient's obstetric initialization page$")
	public void obstetrics_page() {
		try {
			assertEquals("iTrust - Obstetrics Care", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}

	@Then("^I select create new prior pregnancy but see an error$")
	public void incorrect_pregnancy_info() {
		driver.findElement(By.id("pregnancyForm:createNewPreg")).click();
		WebElement element = driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/span"));
		if (!element.getText().contains("This form has not been validated correctly")) {
			fail("Form validation failure");
			System.err.println(element.getText());
		}
	}

}
