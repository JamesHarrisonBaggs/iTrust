package edu.ncsu.csc.itrust.cucumber;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.selenium.iTrustSeleniumTest;

public class CalendarStepDefs extends iTrustSeleniumTest{

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
	
	@Test
	@Given("^Random Person has an appointment for tomorrow$")
	public void testRandom_Person_has_an_appointment_for_tomorrow() throws Throwable {
		driver = login("1", "pw");
		driver.get("http://localhost:8080/iTrust/auth/patient/appointmentRequests.jsp");
		
		// actually schedule an appointment for tomorrow, and then approve it
		// this guarantees that there is at least one appointment to test for on the calendar
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		cal.add(Calendar.DAY_OF_YEAR, 1);
		
		Select select;
		select = new Select(driver.findElement(By.name("HCP")));
		select.selectByValue("Kelly Doctor");
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

		// then logout of random person and login as kelly doctor to approve the appointment
		// so that it appears on the calendar
		driver.get("http://localhost:8080/iTrust/logout.jsp");
		driver.get(ADDRESS);
		driver = login("9000000000", "pw");
		driver.get("http://localhost:8080/iTrust/auth/hcp/viewMyApptRequests.jsp");
		driver.get("http://localhost:8080/iTrust/auth/hcp/viewMyApptRequests.jsp?req_id=2&status=approve");
		driver.get("http://localhost:8080/iTrust/logout.jsp");
	}
	
	@Given("^Random Person logs in with MID 1")
	public void patient_logs_in() throws Throwable {
		driver = login("1", "pw");
	}

	@When("^Random Person clicks View, then Full Calendar")
	public void view_calendar() {
		try {
			driver.get("http://localhost:8080/iTrust/calendar.jsp");
		} catch (Throwable e) {
			// this should not happen anymore
			fail("This was supposed to happen if it hasn't been fixed");
		}
	}
	
	@Then("^Random Person should be taken to a page where they can view their upcoming appointments")
	public void check_calendar() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		try {
			// this checks specifically if the first appointment is made
			driver.get("http://localhost:8080/iTrust/auth/patient/viewAppt.jsp?apt=20");
			assertTrue(driver.getPageSource().contains("HCP"));
		} catch (Throwable e) {
			fail("No appointment found.");
		}

	}
}
