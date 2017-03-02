package edu.ncsu.csc.itrust.cucumber;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.junit.Assert;
import cucumber.api.java.Before;
import cucumber.api.java.en.*;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class HealthTrackerStepDefs {

	public static final String ADDRESS = "http://localhost:8080/iTrust/";
	protected TestDataGenerator gen = new TestDataGenerator();
	private WebDriver driver;
	
	private String patientName;
	private String patientId;
	private String[] patientData = new String[15];

	@Before
	public void setUp() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		driver = new HtmlUnitDriver(); // true to enable Javascript
		driver.get(ADDRESS);
	}

	/** Navigate to HealthTracker Page **/

	@Given("^I have logged in as the HCP with MID (.*) and password (.*)$")
	public void login_as_HCP(String username, String password) throws Throwable {
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

	@And("^I select Patient Information, then Health Tracker Data$")
	public void select_health_tracker_data() throws Throwable {
		// select Health Tracker Data in sidebar
		driver.findElement(By.cssSelector("h2.panel-title")).click();
		driver.findElement(By.linkText("Health Tracker Data")).click();
		try {
			assertEquals("iTrust - Please Select a Patient", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}

	@And("^I input patient first name (.+) and MID (.+) and select the patient$")
	public void enter_patient_id(String name, String id) throws Throwable {
		// save locally
		patientName = name;
		patientId = id;
		// input name and MID
		driver.findElement(By.name("FIRST_NAME")).sendKeys(patientName);
		driver.findElement(By.name("NAME_SUBMIT")).click();
		// select patient button
		driver.findElement(By.name("user_" + patientId)).click();
		try {
			assertEquals("iTrust - View Health Tracker Data", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}

	/** Navigate Around HealthTracker Page **/

	@And("^select View to navigate to the View page$")
	public void navigate_to_view() {
		driver.findElement(By.linkText("View")).click();
	}

	@And("^select Edit to navigate to the Edit page$")
	public void navigate_to_edit() {
		driver.findElement(By.linkText("Edit")).click();
	}

	@And("^select Upload to navigate to the Upload page$")
	public void navigate_to_upload() {
		driver.findElement(By.linkText("Upload")).click();
	}

	@And("^select Summary to navigate to the Summary page$")
	public void navigate_to_summary() {
		driver.findElement(By.linkText("Summary")).click();
	}

	/** View and Enter **/

	@When("^I enter date (.*), calories (.*), steps (.*), distance (.*), floors (.*), activity calories (.*), minutes sedentary (.*), lightly active (.*), fairly active (.*), very active (.*), active hours (.*), heartrate low (.*), heartrate high (.*), heartrate average (.*), and UV exposure (.*)$")
	public void enter_data(String date, String cals, String steps, String dist, String fs, String acal, String mins,
			String minl, String minf, String minv, String ahrs, String hlo, String hhi, String havg, String uvex)
			throws Throwable {

		// parse a date as MM/DD/YYYY
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		Date d = format.parse(date);
		cal.setTime(d);
		String strDate = format.format(cal.getTime());
		// save date in YYYY-MM-DD
		SimpleDateFormat standard = new SimpleDateFormat("YYYY-MM-dd");

		// Save patient data
		patientData[0] = standard.format(cal.getTime());
		patientData[1] = cals;
		patientData[2] = steps;
		patientData[3] = dist;
		patientData[4] = fs;
		patientData[5] = acal;
		patientData[6] = mins;
		patientData[7] = minl;
		patientData[8] = minf;
		patientData[9] = minv;
		patientData[10] = ahrs;
		patientData[11] = hlo;
		patientData[12] = hhi;
		patientData[13] = havg;
		patientData[14] = uvex;

		// Enter patient data
		WebElement element = driver.findElement(By.id("htform:date"));
		element.clear();
		element.sendKeys(strDate);

		element = driver.findElement(By.id("htform:calories"));
		element.clear();
		element.sendKeys(cals);

		element = driver.findElement(By.id("htform:steps"));
		element.clear();
		element.sendKeys(steps);

		element = driver.findElement(By.id("htform:distance"));
		element.clear();
		element.sendKeys(dist);

		element = driver.findElement(By.id("htform:floors"));
		element.clear();
		element.sendKeys(fs);

		element = driver.findElement(By.id("htform:activityCalories"));
		element.clear();
		element.sendKeys(acal);

		element = driver.findElement(By.id("htform:minutesSedentary"));
		element.clear();
		element.sendKeys(mins);

		element = driver.findElement(By.id("htform:minutesLightlyActive"));
		element.clear();
		element.sendKeys(minl);

		element = driver.findElement(By.id("htform:minutesFairlyActive"));
		element.clear();
		element.sendKeys(minf);

		element = driver.findElement(By.id("htform:minutesVeryActive"));
		element.clear();
		element.sendKeys(minv);

		element = driver.findElement(By.id("htform:activeHours"));
		element.clear();
		element.sendKeys(ahrs);

		element = driver.findElement(By.id("htform:heartrateLow"));
		element.clear();
		element.sendKeys(hlo);

		element = driver.findElement(By.id("htform:heartrateHigh"));
		element.clear();
		element.sendKeys(hhi);

		element = driver.findElement(By.id("htform:heartrateAverage"));
		element.clear();
		element.sendKeys(havg);

		element = driver.findElement(By.id("htform:uvExposure"));
		element.clear();
		element.sendKeys(uvex);

	}

	@When("^click the button to save the data$")
	public void save_data() throws Throwable {
		driver.findElement(By.id("htform:submit_data")).click();
	}

	@And("^enter the date to view (.*)$")
	public void select_date(String date) throws Throwable {
		// format a date
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(format.parse(date));
		String dateStr = format.format(cal.getTime());

		// enter into search box
		WebElement element = driver.findElement(By.id("viewForm:searchDate"));
		element.clear();
		element.sendKeys(dateStr);
		element.submit();
	}

	@Then("^I can view the data I entered$")
	public void view_entered_data() throws Throwable {

		// grab data from results table through XPath
		String[] resultData = new String[patientData.length];
		for (int i = 1; i <= patientData.length; i++) {
			resultData[i - 1] = driver.findElement(By.xpath("//*[@id=\"viewForm:DataTable\"]/tbody/tr/td[" + i + "]"))
					.getText();
		}

		// assert equals patient data
		for (int i = 0; i < patientData.length; i++) {
			// System.out.println("Entered:\t" + patientData[i]);
			// System.out.println("Returned:\t" + resultData[i]);
			assertEquals(patientData[i], resultData[i]);
		}
	}

	/** Test Upload **/

	@When("^I click the button to upload the file (.*)$")
	public void click_upload_button(String filename) throws Throwable {
		WebElement element;
		try {
			// multiplatform support!
			String os = System.getProperty("os.name");
			String dir = System.getProperty("user.dir");
			if (os.startsWith("Windows")) {
				filename = filename.replace("/", "\\");
			}
			String path = dir + filename;
			
			// helpful prints
			System.out.println("path: " + path);
			System.out.println("os.name: " + os);
			System.out.println("user.dir: " + dir);
			
			element = driver.findElement(By.id("uploadFile"));
			element.sendKeys(path);
			driver.findElement(By.id("uploadButton")).click();
			
		} catch (Throwable e) {
			Assert.fail("Unable to upload file: " + e.getMessage());
		}
	}

	@Then("^I can view the data: (.*), (.*), (.*), (.*), (.*), (.*), (.*), (.*), and (.*)$")
	public void view_uploaded_data(String cals, String steps, String dist, String fs, String mins, String minl,
			String minf, String minv, String acal) throws Throwable {

		// only care about first 10 values for FitBit data
		int length = 10;
		
		// grab data from results table through XPaths
		WebElement element;
		String[] resultData = new String[length];
		for (int i = 1; i <= length; i++) {
			element = driver.findElement(By.xpath("//*[@id=\"viewForm:DataTable\"]/tbody/tr/td[" + i + "]"));
			resultData[i - 1] = element.getText();
		}
		// wipe out date
		resultData[0] = null;

		// assert equals expected data
		String[] expectedData = { null, cals, steps, dist, fs, acal, mins, minl, minf, minv };
		for (int i = 0; i < length; i++) {
			// System.out.println("String:\t" + expectedData[i] + ", " + resultData[i]);
			assertEquals(expectedData[i], resultData[i]);
		}
		
	}

	/** Test Summary **/

	@Then("^I can see a summary of the data$")
	public void view_summary() throws Throwable {
		WebElement element = driver.findElement(By.id("chart_div"));
		// TODO not sure how to test chart
		// element = driver.findElement(By.xpath("//*[@id=\"chart_div\"]/div/div[1]/div/svg/g[1]/text"));
		// assertEquals("Steps Over Time", element.getText());
	}

}