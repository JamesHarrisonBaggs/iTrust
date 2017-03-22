package edu.ncsu.csc.itrust.model.obGyn;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.faces.bean.ManagedBean;

/**
 * A class representing an obstetrics patient initialization record
 * @author erein
 *
 */
@ManagedBean(name = "obsInitBean")
public class ObstetricsInitBean {

	/** MID of the patient */
	private long patientId;
	/** Date of initialization */
	private LocalDate initDate;
	/** Date of last menstrual period */
	private LocalDate lastMenstrualPeriod;
	/** Estimated due date */
	private LocalDate estimatedDueDate;
	/** Number of days pregnant */
	private int daysPregnant;
	/** String representing time pregnant */
	private String timePregnant;
	/** Boolean for if record is current */
	private boolean current;
	
	/**
	 * Construct an ObstetricsBean
	 */
	public ObstetricsInitBean() {
		
	}
	
	/**
	 * Calculates and sets the estimated due date and days pregnant
	 * Called from setLastMenstrualPeriod() and therefore updated with the LMP
	 */
	private void calculateData() {
		// Estimated due date = LMP + 280 days
		LocalDate edd = lastMenstrualPeriod.plus(280, ChronoUnit.DAYS);
		this.setEstimatedDueDate(edd);
		
		// Days pregnant = between LMP and today
		long daysPreg = ChronoUnit.DAYS.between(lastMenstrualPeriod.atStartOfDay(), LocalDate.now().atStartOfDay());
		this.setDaysPregnant((int) daysPreg);
		
		// TODO not a good way of storing days pregnant
		// should probably generate on-the-fly
		
		// String representing days pregnant
		StringBuilder sb = new StringBuilder();
		long weeks = daysPreg/7;
		sb.append(weeks);
		sb.append(weeks == 1 ? " week " : " weeks ");
		long days = daysPreg % 7;
		sb.append(days);
		sb.append(days == 1 ? " day" : " days");
		this.setTimePregnant(sb.toString());
	}
	
	/** GETTERS **/
	
	public long getPatientId() {
		return patientId;
	}

	// might need to change in XHTML
	public LocalDate getInitDate() {
		return initDate;
	}
	
	// return java.time.LocalDate as java.sql.Timestamp
	public Timestamp getInitTimestamp() {
		return Timestamp.valueOf(initDate.atStartOfDay());
	}

	public LocalDate getLastMenstrualPeriod() {
		return lastMenstrualPeriod;
	}
	
	// return java.time.LocalDate as java.sql.Timestamp
	public Timestamp getLMPTimestamp() {
		return Timestamp.valueOf(lastMenstrualPeriod.atStartOfDay());
	}

	public LocalDate getEstimatedDueDate() {
		return estimatedDueDate;
	}

	public int getDaysPregnant() {
		return daysPregnant;
	}
	
	public String getTimePregnant() {
		return timePregnant;
	}

	public boolean isCurrent() {
		return current;
	}
	
	/** SETTERS **/

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	// might need to change in XHTML
	public void setInitDate(LocalDate initDate) {
		this.initDate = initDate;
	}
	
	// set java.time.LocalDate from java.sql.Timestamp
	public void setInitTimestamp(Timestamp initDate) {
		this.initDate = initDate.toLocalDateTime().toLocalDate();
	}

	public void setLastMenstrualPeriod(LocalDate lastMenstrualPeriod) {
		this.lastMenstrualPeriod = lastMenstrualPeriod;
		this.calculateData();
	}

	// set java.time.LocalDate from java.sql.Timestamp
	public void setLMPTimestamp(Timestamp lmpDate) {
		this.lastMenstrualPeriod = lmpDate.toLocalDateTime().toLocalDate();
	}

	public void setEstimatedDueDate(LocalDate estimatedDueDate) {
		this.estimatedDueDate = estimatedDueDate;
	}

	public void setDaysPregnant(int daysPregnant) {
		this.daysPregnant = daysPregnant;
	}
	
	public void setTimePregnant(String timePregnant) {
		this.timePregnant = timePregnant;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}
	
}