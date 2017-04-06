package edu.ncsu.csc.itrust.cucumber;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.*;

import cucumber.api.java.*;
import cucumber.api.java.en.*;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ObstetricsOfficeVisitStepDefs {
	
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
	
	@Given("^I logged in as an OB/GYN HCP with MID (.*) and password (.*) for Obstetrics Office Visit$")
	public void log_in_oov(String hcp, String pw) {
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
	
	@And("^I select Obstetrics Info, then go to Document Office Visit$")
	public void document_visit() {
		driver.findElement(By.cssSelector("h2.panel-title")).click();
		driver.findElement(By.linkText("Document Office Visit")).click();
		try {
			assertEquals("iTrust - Please Select a Patient", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@And("^I enter patient's name (.*) and select the patient MID (.*)$")
	public void select_patient(String name, String patient) {
		driver.findElement(By.name("FIRST_NAME")).clear();
		driver.findElement(By.name("FIRST_NAME")).sendKeys(name);
		driver.findElement(By.name("NAME_SUBMIT")).click();
		try {
			driver.findElement(By.name("user_" + patient)).click();
			assertEquals("Document Office Visit", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@And("^I click Create a New Office Visit$")
	public void create_visit() {
		WebElement element = driver.findElement(By.id("newVisitButton"));
		// dumb workaround of issues with JS button
		String link = element.getAttribute("onclick").split("'")[1];
		String[] base = driver.getCurrentUrl().split("/iTrust");
		driver.navigate().to(base[0] + link);
		assertEquals("Office Visit", driver.getTitle());
	}
	
	@When("^I enter Date (.*) and choose appointment type as OB/GYN and click save$")
	public void select_type(String date) {
		driver.findElement(By.id("basic_ov_form:ovdate")).clear();
		driver.findElement(By.id("basic_ov_form:ovdate")).sendKeys(date);
		driver.findElement(By.id("basic_ov_form:ovApptType")).click();
		//driver.findElement(By.xpath("//*[@id=\"basic_ov_form\"]/div[3]")).click();
		driver.findElement(By.id("basic_ov_form:submitVisitButton")).click();
	}
	
	@And("^I click on Obstetrics, and enter weight (.*), blood pressure (.*), FHR (.*), multiples (.*), LLP (.*), RHflag (.*), and click Save Obstetrics Visit$")
	public void office_visit_info(String weight, String bp, String fhr, String num, String llp, String rh) {
		//driver.findElement(By.linkText("Obstetrics")).click();
		driver.findElement(By.id("manage_obstetrics_formError:weight")).clear();
		driver.findElement(By.id("manage_obstetrics_formError:weight")).sendKeys(weight);
		driver.findElement(By.id("manage_obstetrics_formError:bloodPressure")).clear();
		driver.findElement(By.id("manage_obstetrics_formError:bloodPressure")).sendKeys(bp);
		driver.findElement(By.id("manage_obstetrics_formError:fetalHeartRate")).clear();
		driver.findElement(By.id("manage_obstetrics_formError:fetalHeartRate")).sendKeys(fhr);
		driver.findElement(By.id("manage_obstetrics_formError:multiples")).clear();
		driver.findElement(By.id("manage_obstetrics_formError:multiples")).sendKeys(num);
		Select s_llp = new Select(driver.findElement(By.id("manage_obstetrics_formError:lowLyingPlacenta")));
		s_llp.deselectAll();
		s_llp.selectByVisibleText(llp);
		Select s_rh = new Select(driver.findElement(By.id("manage_obstetrics_formError:rhFlag")));
		s_rh.deselectAll();
		s_rh.selectByVisibleText(llp);
		driver.findElement(By.id("manage_obstetrics_formError:editObstetricVisit")).click();
	}
	
	@And("^I click on Add Ultrasound, and enter id (.*), CRL (.*), BPD (.*), HC (.*), FL (.*), OFD (.*), AC (.*), HL (.*), EFW (.*), and click save Ultrasound$")
	public void ultrasound_info(String id, String crl, String bpd, String hc, String fl, String ofd, String ac, String hl, String efw) {
		//driver.findElement(By.linkText("Obstetrics")).click();
		driver.findElement(By.id("j_idt155:addUltraSound")).click();
		driver.findElement(By.id("ultrasound_formSuccess:fetusId")).clear();
		driver.findElement(By.id("ultrasound_formSuccess:fetusId")).sendKeys(id);
		driver.findElement(By.id("ultrasound_formSuccess:crl")).clear();
		driver.findElement(By.id("ultrasound_formSuccess:crl")).sendKeys(crl);
		driver.findElement(By.id("ultrasound_formSuccess:bpd")).clear();
		driver.findElement(By.id("ultrasound_formSuccess:bpd")).sendKeys(bpd);
		driver.findElement(By.id("ultrasound_formSuccess:hc")).clear();
		driver.findElement(By.id("ultrasound_formSuccess:hc")).sendKeys(hc);
		driver.findElement(By.id("ultrasound_formSuccess:fl")).clear();
		driver.findElement(By.id("ultrasound_formSuccess:fl")).sendKeys(fl);
		driver.findElement(By.id("ultrasound_formSuccess:ofd")).clear();
		driver.findElement(By.id("ultrasound_formSuccess:ofd")).sendKeys(ofd);
		driver.findElement(By.id("ultrasound_formSuccess:ac")).clear();
		driver.findElement(By.id("ultrasound_formSuccess:ac")).sendKeys(ac);
		driver.findElement(By.id("ultrasound_formSuccess:hl")).clear();
		driver.findElement(By.id("ultrasound_formSuccess:hl")).sendKeys(hl);
		driver.findElement(By.id("ultrasound_formSuccess:efw")).clear();
		driver.findElement(By.id("ultrasound_formSuccess:efw")).sendKeys(efw);
		driver.findElement(By.id("ultrasound_formSuccess:saveUltraSound")).click();
		try {
			String message = driver.findElement(By.xpath("//*[@id=\"ultrasound_formSuccess\"]/table/tbody/tr[1]/td/div/span")).getText();
			assertEquals("Ultrasound Updated Successfully", message);
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@And("^I click Go Back To Office Vist, and click Save Obsteterics Visit$")
	public void save_visit() {
		driver.findElement(By.id("ultrasound_formSuccess:goBack")).click();
		//driver.findElement(By.linkText("Obstetrics")).click();
		driver.findElement(By.id("manage_obstetrics_formError:editObstetricVisit")).click();
	}
	
	@Then("^a new Obstetrics Office Visit is created for the patient$")
	public void office_visit_created() {
		try {
			String message = driver.findElement(By.xpath("//*[@id=\"manage_obstetrics_formError\"]/div/span")).getText();
			assertEquals("Obstetrics Visit Updated Successfully", message);
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	/*
	@And("^the next office visit will be scheduled at a federal holiday, so it changes to (.*)$")
	public void next_visit(String appt) {
		//calendar needs update
	}*/
	
	@And("^I click on an exisited OBGYN office visit (.*)$")
	public void choose_visit(String date) {
		String time = driver.findElement(By.xpath("//*[@id=\"previousVisits\"]/tbody/tr[5]/td[3]")).getText();
		assertEquals(date, time);
		driver.findElement(By.xpath("//*[@id=\"previousVisits\"]/tbody/tr[5]/td[1]/a")).click();
		//driver.findElement(By.linkText("Obstetrics")).click();
		
	}
	
	@When("^I edit weight (.*), and click save Obstetrics Visit$")
	public void edit_visit(String weight) {
		driver.findElement(By.id("manage_obstetrics_formError:weight")).clear();
		driver.findElement(By.id("manage_obstetrics_formError:weight")).sendKeys(weight);
		driver.findElement(By.id("manage_obstetrics_formError:editObstetricVisit")).click();
	}
	
	@Then("^the Obstetrics Office Visit is changed$")
	public void visit_changed() {
		try {
			driver.findElement(By.xpath("//*[@id=\"manage_obstetrics_formError\"]/div/span"));
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	
	
	@When("^I enter patient's MID (.*)$")
	public void search_by_mid(String patient) {
		driver.findElement(By.name("FIRST_NAME")).clear();
		driver.findElement(By.name("FIRST_NAME")).sendKeys(patient);
		driver.findElement(By.name("NAME_SUBMIT")).click();
	}
	
	@Then("^the patient is not exist$")
	public void invalid_patient() {
		String search_result = driver.findElement(By.className("searchResults")).getText();
		assertEquals("Found 0 Records", search_result);
	}
	
	@Then("^the patient is not eligiable for Obsteterics care$")
	public void not_eligiable() {
		String err_message = driver.findElement(By.id("officevisitinfo-message")).getText();
		assertEquals("Patient is not eligiable for Obsteterics care", err_message);
	}
	
	@Given("^I logged in as a none OB/GYN HCP with MID (.*) and password (.*) for Obstetrics Office Visit$")
	public void n_hcp_oov(String hcp, String pw) {
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
	
	@Then("^a message says I can't create an OBGYN appointment because I am not an OBGYN HCP$")
	public void n_hcp_message() {
		String err_message = driver.findElement(By.id("officevisitinfo-message")).getText();
		assertEquals("Can't create OB/GYN appointment, because you are not an OB/GYN HCP", err_message);
	}
}