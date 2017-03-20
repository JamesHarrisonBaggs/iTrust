package edu.ncsu.csc.itrust.cucumber;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.selenium.iTrustSeleniumTest;

public class ApptRequestStepDefs extends iTrustSeleniumTest {

	public static final String ADDRESS = "http://localhost:8080/iTrust/";
	private WebDriver driver = null;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		driver = new HtmlUnitDriver();
		gen.clearAllTables();
		gen.standardData();
	}
	
	@Given("^Andy Programmer logs in with MID 2$")
	public void testAndy_Programmer_logs_in_with_MID() throws Throwable {
		driver = login("2", "pw");
	}

	@When("^Andy Programmer clicks Appointments, then Appointment Requests$")
	public void andy_Programmer_clicks_Appointments_then_Appointment_Requests() {
		try {
			driver.get("http://localhost:8080/iTrust/auth/patient/appointmentRequests.jsp");

		} catch (Throwable e) {
			fail("This shouldn't happen if everything works");
		}
	}

	@When("^Makes an appointment request for tomorrow at 9:45AM")
	public void makes_an_appointment_request_for() throws Throwable {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		cal.add(Calendar.DAY_OF_YEAR, 1);
		
		Select select;
		select = new Select(driver.findElement(By.name("HCP")));
		select.selectByValue("Gandalf Stormcrow");
		select = new Select(driver.findElement(By.name("apptType")));
		select.selectByValue("General Checkup");
		select = new Select(driver.findElement(By.name("time1")));
		select.selectByValue("09");
		select = new Select(driver.findElement(By.name("time2")));
		select.selectByValue("45");
		select = new Select(driver.findElement(By.name("time3")));
		WebElement element = driver.findElement(By.name("startDate"));
		element.clear();
		element.sendKeys(format.format(cal.getTime()));
		element.submit();
	}

	@When("^logs out$")
	public void logs_out() throws Throwable {
		driver.get("http://localhost:8080/iTrust/logout.jsp");
		driver.get(ADDRESS);
	}

	@When("^Gandalf Stormcrow logs in with MID 9000000003$")
	public void gandalf_Stormcrow_logs_in_with_MID() throws Throwable {
		driver = login("9000000003", "pw");
	}

	@When("^Gandalf Stormcrow clicks Appointments, then Appointment Requests$")
	public void gandalf_Stormcrow_clicks_Appointments_then_Appointment_Requests() throws Throwable {
		try {
			driver.get("http://localhost:8080/iTrust/auth/patient/appointmentRequests.jsp");
		} catch (Throwable e) {
			fail("This shouldn't happen if everything works");
		}
	}

	@Then("^Gandalf Stormcrow should have one appointment request with Andy Programmer$")
	public void gandalf_Stormcrow_should_have_one_appointment_request_with_Andy_Programmer() throws Throwable {
		// finding link text works because we assume that there aren't any other pending requests in the test environment
		try {
			//driver.findElement(By.linkText("Approve"));
			driver.getPageSource().contains("Request from: Andy Programmer");
		} catch (Exception e) {
			fail("There is no appointment");
		}
	}

	@Then("^Kelly Doctor does not have an appointment request with Andy Programmer$")
	public void kelly_Doctor_does_not_have_an_appointment_request_with_Andy_Programmer() throws Throwable {
		// log out of Gandalf, then login as Kelly Doctor
		driver.get("http://localhost:8080/iTrust/logout.jsp");
		driver.get(ADDRESS);
		driver = login("9000000000", "pw");
		
		// navigate to appointments
		try {
			driver.get("http://localhost:8080/iTrust/auth/patient/appointmentRequests.jsp");
		} catch (Throwable e) {
			fail("This shouldn't happen if everything works");
		}
		
		// finding link text works because we assume that there aren't any other pending requests in the test environment
		try {
			assertFalse(driver.getPageSource().contains("Request from: Andy Programmer"));
		} catch (Exception e) {
			// there should be an exception thrown because there should be no appointment request for Kelly
		}
	}
}
