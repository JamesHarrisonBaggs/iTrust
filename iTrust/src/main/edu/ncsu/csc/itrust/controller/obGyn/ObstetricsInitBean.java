package edu.ncsu.csc.itrust.controller.obGyn;

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
	/** Boolean for if record is current */
	private boolean current;
	/** Date of last menstrual period */
	private LocalDate lastMenstrualPeriod;
	/** Estimated due date */
	private LocalDate estimatedDueDate;
	/** Number of days pregnant */
	private long daysPregnant;
	/** String representing time pregnant */
	private String daysPregnantStr;
	
	/**
	 * Construct an ObstetricsBean
	 */
	public ObstetricsInitBean() {
		
	}
	
	/**
	 * Calculates and sets the estimated due date and days pregnant
	 * Called from setLastMenstrualPeriod() and therefore updated with the LMP
	 */
	private void calculateDueDateAndDaysPregnant() {
		// Estimated due date = LMP + 280 days
		LocalDate edd = lastMenstrualPeriod.plus(280, ChronoUnit.DAYS);
		this.setEstimatedDueDate(edd);
		
		// Days pregnant = between LMP and today
		long daysPreg = ChronoUnit.DAYS.between(lastMenstrualPeriod.atStartOfDay(), LocalDate.now().atStartOfDay());
		this.setDaysPregnant(daysPreg);
		
		// TODO not a good way of storing days pregnant
		
		// String representing days pregnant
		StringBuilder sb = new StringBuilder();
		long weeks = daysPreg/7;
		sb.append(weeks);
		sb.append(weeks == 1 ? " week " : " weeks ");
		long days = daysPreg % 7;
		sb.append(days);
		sb.append(days == 1 ? " day" : " days");
		this.setDaysPregnantStr(sb.toString());
	}
	
	/** GETTERS **/
	
	/**
	 * @return the patientId
	 */
	public long getPatientId() {
		return patientId;
	}

	/**
	 * @return the initDate
	 */
	public LocalDate getInitDate() {
		return initDate;
	}

	/**
	 * @return the current
	 */
	public boolean isCurrent() {
		return current;
	}

	/**
	 * @return the lastMenstrualPeriod
	 */
	public LocalDate getLastMenstrualPeriod() {
		return lastMenstrualPeriod;
	}
	
	/**
	 * @return the lastMenstrualPeriod as java.sql.Timestamp
	 */
	public Timestamp getLMPTimestamp() {
		return Timestamp.valueOf(lastMenstrualPeriod.atStartOfDay());
	}

	/**
	 * @return the estimatedDueDate
	 */
	public LocalDate getEstimatedDueDate() {
		return estimatedDueDate;
	}

	/**
	 * @return the daysPregnant
	 */
	public long getDaysPregnant() {
		return daysPregnant;
	}

	/**
	 * @return the daysPregnantStr
	 */
	public String getDaysPregnantStr() {
		return daysPregnantStr;
	}
	
	/** SETTERS **/

	/**
	 * @param patientId the patientId to set
	 */
	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	/**
	 * @param initDate the initDate to set
	 */
	public void setInitDate(LocalDate initDate) {
		this.initDate = initDate;
	}
	
	/**
	 * @param initDate - set java.time.LocalDate from java.sql.Timestamp
	 */
	public void setInitDateTimestamp(Timestamp initDate) {
		this.initDate = initDate.toLocalDateTime().toLocalDate();
	}

	/**
	 * @param current the current to set
	 */
	public void setCurrent(boolean current) {
		this.current = current;
	}
	
	/**
	 * @param lastMenstrualPeriod the lastMenstrualPeriod to set
	 */
	public void setLastMenstrualPeriod(LocalDate lastMenstrualPeriod) {
		this.lastMenstrualPeriod = lastMenstrualPeriod;
		this.calculateDueDateAndDaysPregnant();
	}
	
	/**
	 * @param lastMenstrualPeriod - set java.time.LocalDate from java.sql.Timestamp
	 */
	public void setLMPTimestamp(Timestamp lastMenstrualPeriod) {
		this.lastMenstrualPeriod = lastMenstrualPeriod.toLocalDateTime().toLocalDate();
	}

	/**
	 * @param estimatedDueDate the estimatedDueDate to set
	 */
	public void setEstimatedDueDate(LocalDate estimatedDueDate) {
		this.estimatedDueDate = estimatedDueDate;
	}

	/**
	 * @param daysPregnant the daysPregnant to set
	 */
	public void setDaysPregnant(long daysPregnant) {
		this.daysPregnant = daysPregnant;
	}

	/**
	 * @param daysPregnantStr the daysPregnantStr to set
	 */
	public void setDaysPregnantStr(String daysPregnantStr) {
		this.daysPregnantStr = daysPregnantStr;
	}
	
}