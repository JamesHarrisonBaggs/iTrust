/**
 * 
 */
package edu.ncsu.csc.itrust.cucumber;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import cucumber.api.java.Before;
import cucumber.api.java.en.*;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
//import edu.ncsu.csc.itrust.exception.DBException;
//import edu.ncsu.csc.itrust.model.old.beans.TransactionBean;
//import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
//import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

/**
 * @author awburns, ereinst Some code taken from existing Selenium test cases
 * @IMPORTANT You must have Firefox installed to run this test properly
 */
public class ViewInboxMessageStepDefs {

	public static final String ADDRESS = "http://localhost:8080/iTrust/";
	protected TestDataGenerator gen = new TestDataGenerator();
	private WebDriver driver;
	private String stamp;
	
	/**
	 * from edu.ncsu.csc.itrust.selenium.AppointmentTypeTest.java
	 */
	@Before
	public void setUp() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		driver = new HtmlUnitDriver(); // enable Javascript
		driver.get(ADDRESS);
	}

	@Given("^I log in as HCP 9000000000$")
	public void log_in_user() throws Exception {
		driver.findElement(By.id("j_username")).sendKeys("9000000000");
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.xpath("//input[@value='Login']")).click();
		// assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
	}

	@And("^I navigate to the inbox$")
	public void navigate_to_inbox() throws Throwable {
		driver.get(ADDRESS + "auth/hcp-patient/messageOutbox.jsp");
		Thread.sleep(500);
		// assertLogged(TransactionType.OUTBOX_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.linkText("Message Outbox")).click();
		// assertLogged(TransactionType.OUTBOX_VIEW, 9000000000L, 0L, "");
	}

	@And("^I send a new message and log out$")
	public void send_message() throws Throwable {
		// compose message
		driver.findElement(By.linkText("Compose a Message")).click();
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("2");
		driver.findElement(By.xpath("//input[@value='2']")).submit();
		driver.findElement(By.name("subject")).clear();
		driver.findElement(By.name("subject")).sendKeys("Visit Request");
		driver.findElement(By.name("messageBody")).clear();
		driver.findElement(By.name("messageBody")).sendKeys("We really need to have a visit.");

		// send message
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		Thread.sleep(500);
		// assertLogged(TransactionType.MESSAGE_SEND, 9000000000L, 2L, "");

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		stamp = dateFormat.format(date);

		// log out
		driver.get(ADDRESS + "logout.jsp");
	}

	@And("^I log in as a patient and respond to the message$")
	public void patient_login() throws Throwable {
		// log in as patient
		driver.findElement(By.id("j_username")).sendKeys("2");
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.xpath("//input[@value='Login']")).click();
		Thread.sleep(500);
		// assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");

		// navigate to inbox
		driver.get(ADDRESS + "auth/hcp-patient/messageInbox.jsp");
		Thread.sleep(500);
		// assertLogged(TransactionType.INBOX_VIEW, 2L, 0L, "");

		// read message and send reply
		driver.findElement(By.linkText("Read")).click();
		driver.findElement(By.linkText("Reply")).click();
		driver.findElement(By.name("messageBody")).clear();
		driver.findElement(By.name("messageBody")).sendKeys("Which office visit did you update?");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		Thread.sleep(1000);
		// assertLogged(TransactionType.MESSAGE_SEND, 2L, 9000000000L, "");

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		stamp = dateFormat.format(date);

		// log out
		driver.get(ADDRESS + "logout.jsp");

	}

	@When("^I log back in as the HCP$")
	public void log_back_in() throws Exception {
		driver.findElement(By.id("j_username")).sendKeys("9000000000");
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.xpath("//input[@value='Login']")).click();
		Thread.sleep(500);
		// assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
	}

	/**
	 * Code used to send message taken from
	 * iTrust.src/test/java.edu.ncsu.csc.itrust.selenium.MessagingUseCaseTest.java
	 * and has been modified
	 * 
	 */
	@And("^click on the unread message")
	public void view_inbox_unread() throws Exception {
		driver.get(ADDRESS + "auth/hcp-patient/messageInbox.jsp");
		Thread.sleep(500);
		// assertLogged(TransactionType.INBOX_VIEW, 9000000000L, 0L, "");
		Assert.assertTrue(driver.getPageSource().contains("Andy Programmer"));
		Assert.assertTrue(driver.getPageSource().contains("RE: Visit Request"));
		Assert.assertTrue(driver.getPageSource().contains(stamp));
		driver.findElement(By.linkText("Read")).click();
	}

	@When("^click the browser's back buttons$")
	public void click_back_button() throws Throwable {
		driver.navigate().back();

	}

	@Then("^I should be returned to the user's inbox")
	public void view_message() throws Exception {
		Thread.sleep(1000);
		// assertLogged(TransactionType.INBOX_VIEW, 9000000000L, 0L, "");
		Thread.sleep(1000);
	}

	/**
	 * from
	 * http://stackoverflow.com/questions/38503252/how-to-check-if-a-label-is-bold-with-selenium-webdriver-using-java
	 */
	@And("^the message should be designated as read and un-bolded$")
	public void the_message_should_be_designated_as_read_and_un_bolded() throws Throwable {
		try {
			String weight = driver.findElement(By.linkText("Read")).getCssValue("font-weight");
			if (weight.equals("bold") || weight.equals("bolder") || Integer.parseInt(weight) >= 700) {
				Assert.fail("This message should have been marked as read");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Assert.fail("HtmlUnitDriver encountered an issue with Javascript");
		}
	}
	
}
