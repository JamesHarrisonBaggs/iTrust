package edu.ncsu.csc.itrust.cucumber;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.*;
import cucumber.api.java.en.*;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ChildbirthOfficeVisitStepDefs {

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
	
	@Given("^an OB/GYN HCP has logged in with MID (.*) and password (.*)$")
	public void cov_hcp_login(String hcp, String pw) {
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
	
	@And("^selects Document Office Visit in Obstetrics Info$")
	public void cov_document() {
		driver.findElement(By.cssSelector("h2.panel-title")).click();
		driver.findElement(By.linkText("Document Office Visit")).click();
		try {
			assertEquals("iTrust - Please Select a Patient", driver.getTitle());
		} catch (Error e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@And("^enters patient's name (.*) and select the correct MID (.*)$")
	public void cov_select_patient(String name, String patient) {
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
	
	@And("^clicks on Create a New Office Visit$")
	public void cov_create_visit() {
		WebElement element = driver.findElement(By.id("newVisitButton"));
		// dumb workaround of issues with JS button
		String link = element.getAttribute("onclick").split("'")[1];
		String[] base = driver.getCurrentUrl().split("/iTrust");
		driver.navigate().to(base[0] + link);
		assertEquals("Office Visit", driver.getTitle());
	}
	
	@When("^enters Date (.*) and appointment type as Childbirth and click save$")
	public void cov_select_type(String date) {
		driver.findElement(By.id("basic_ov_form:ovdate")).clear();
		driver.findElement(By.id("basic_ov_form:ovdate")).sendKeys(date);
		driver.findElement(By.id("basic_ov_form:ovApptType")).click();
		//driver.findElement(By.xpath("//*[@id=\"basic_ov_form\"]/div[3]")).click();
		driver.findElement(By.id("basic_ov_form:submitVisitButton")).click();
	}
	
	@And("^chooses delivery type (.*), and enter drugs (.*), (.*), (.*), (.*), and (.*), and Pre-Estimate Flag (.*), then click Save Childbirth Visit$")
	public void cov_new_visit(String type, String pi, String no, String pe, String ea, String ms, String flag) {
		Select d_type = new Select(driver.findElement(By.xpath("//*[@id=\"manage_obstetrics_formChildError:deliveryType\"]")));
		d_type.deselectAll();
		d_type.selectByVisibleText(type);
		Select d_pre_e = new Select(driver.findElement(By.xpath("//*[@id=\"manage_obstetrics_formChildError:preScheduled\"]")));
		d_pre_e.deselectAll();
		d_pre_e.selectByVisibleText(flag);
		driver.findElement(By.id("manage_obstetrics_formChildError:pitocin")).clear();
		driver.findElement(By.id("manage_obstetrics_formChildError:pitocin")).sendKeys(pi);
		driver.findElement(By.id("manage_obstetrics_formChildError:nitrousOxide")).clear();
		driver.findElement(By.id("manage_obstetrics_formChildError:nitrousOxide")).sendKeys(no);
		driver.findElement(By.id("manage_obstetrics_formChildError:pethidine")).clear();
		driver.findElement(By.id("manage_obstetrics_formChildError:pethidine")).sendKeys(pe);
		driver.findElement(By.id("manage_obstetrics_formChildError:epiduralAnaesthesia")).clear();
		driver.findElement(By.id("manage_obstetrics_formChildError:epiduralAnaesthesia")).sendKeys(ea);
		driver.findElement(By.id("manage_obstetrics_formChildError:magnesiumSulfate")).clear();
		driver.findElement(By.id("manage_obstetrics_formChildError:magnesiumSulfate")).sendKeys(ms);
		driver.findElement(By.id("manage_obstetrics_formChildError:submitVisitButton")).click();
		assertEquals("Childbirth Visit Updated Successfully", driver.findElement(By.xpath("//*[@id=\"content-child\"]/div/span")).getText());
	}
	
	@And("^clicks New Baby, enters time (.*), and sex (.*), then click Save$")
	public void cov_new_baby(String time, String sex) {
		driver.findElement(By.id("j_idt257:newBaby")).click();
		driver.findElement(By.id("j_idt259:babyDate")).clear();
		driver.findElement(By.id("j_idt259:babyDate")).sendKeys(time);
		Select d_sex = new Select(driver.findElement(By.id("j_idt259:gender")));
		d_sex.deselectAll();
		d_sex.selectByVisibleText("sex");
		driver.findElement(By.id("j_idt259:saveNewBaby")).click();
		assertEquals("Baby Updated Successfully", driver.findElement(By.xpath("//*[@id=\"content-child\"]/table/tbody/tr/td[2]/div/span")).getText());
	}
	
	@Then("^clicks Save Childbirth Visit, a new Childbirth Office Visit is created and new patient is added$")
	public void cov_save() {
		driver.findElement(By.id("manage_obstetrics_formChildError:submitVisitButton")).click();
		assertEquals("Childbirth Visit Updated Successfully", driver.findElement(By.xpath("//*[@id=\"content-child\"]/div/span")).getText());
	}
	
	@And("^clicks on an existed Childbirth Office Visit (.*)$")
	public void cov_select_exisited(String date) {
		int i = 1;
		while(true) {
			String time = driver.findElement(By.xpath("//*[@id=\"previousVisits\"]/tbody/tr[" + i + "]/td[3]")).getText();
			if (time.equals(date)) break;
			i++;
		}
		driver.findElement(By.xpath("//*[@id=\"previousVisits\"]/tbody/tr[" + i + "]/td[1]/a")).click();
	}
	
	@When("^edit the delivery type (.*), and clicks Save Childbirth Visit$")
	public void voc_edit(String type) {
		Select d_type = new Select(driver.findElement(By.xpath("//*[@id=\"manage_obstetrics_formChildError:deliveryType\"]")));
		d_type.deselectAll();
		d_type.selectByVisibleText(type);
		driver.findElement(By.id("manage_obstetrics_formChildError:submitVisitButton")).click();
	}
	
	@Then("^the Childbirth Office Visit is modified$")
	public void voc_saved() {
		assertEquals("Childbirth Visit Updated Successfully", driver.findElement(By.xpath("//*[@id=\"content-child\"]/div/span")).getText());
	}
	
	@When("^enters patient's name (.*)$")
	public void voc_find(String name) {
		driver.findElement(By.name("FIRST_NAME")).clear();
		driver.findElement(By.name("FIRST_NAME")).sendKeys(name);
		driver.findElement(By.name("NAME_SUBMIT")).click();
	}
	
	@Then("^the patient can not be found in system$")
	public void voc_no_patient() {
		String search_result = driver.findElement(By.className("searchResults")).getText();
		assertEquals("Found 0 Records", search_result);
	}
}
