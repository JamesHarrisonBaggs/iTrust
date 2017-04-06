package edu.ncsu.csc.itrust.model.obgyn;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.faces.bean.ManagedBean;

/**
 * A class representing an obstetrics patient initialization record
 * @author erein
 *
 */
@ManagedBean(name = "obsInitBean")
public class ObstetricsInit {

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
	
	public ObstetricsInit() {
		this(false);
	}
	
	public ObstetricsInit(boolean current) {
		if (current) {
			this.setInitDate(LocalDate.now());
			this.setLastMenstrualPeriod(LocalDate.now());
		}
	}

	/**
	 * Calculates and sets the estimated due date and days pregnant
	 */
	public void calculateData() {
		// Estimated due date = LMP + 280 days
		LocalDate edd = lastMenstrualPeriod.plus(280, ChronoUnit.DAYS);
		this.setEstimatedDueDate(edd);
		
		// determine if past or current initialization
		boolean dueDateIsFuture = estimatedDueDate.isAfter(LocalDate.now());
		this.setCurrent(dueDateIsFuture);

		// If in future:  days pregnant = days between LMP and now
		// If in past:    days pregnant = days between LMP and EDD
		LocalDateTime end = dueDateIsFuture ? LocalDate.now().atStartOfDay() : estimatedDueDate.atStartOfDay();
		this.setDaysPregnant((int) ChronoUnit.DAYS.between(lastMenstrualPeriod.atStartOfDay(), end));
				
		// String representing days pregnant
		StringBuilder sb = new StringBuilder();
		int weeks = daysPregnant/7;
		sb.append(weeks);
		sb.append(weeks == 1 ? " week " : " weeks ");
		int days = daysPregnant % 7;
		sb.append(days);
		sb.append(days == 1 ? " day" : " days");
		this.timePregnant = sb.toString();
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
		if (lastMenstrualPeriod != null) {
			calculateData();
		}
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
	
	public void setInitTimestamp(Timestamp initDate) {
		this.setInitDate(initDate.toLocalDateTime().toLocalDate());
	}
	
	public void setLastMenstrualPeriod(LocalDate lastMenstrualPeriod) {
		this.lastMenstrualPeriod = lastMenstrualPeriod;
		if (this.lastMenstrualPeriod != null) {
			calculateData();
		}
	}

	// set java.time.LocalDate from java.sql.Timestamp
	public void setLMPTimestamp(Timestamp lmpDate) {
		this.setLastMenstrualPeriod(lmpDate.toLocalDateTime().toLocalDate());
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}
	
	private void setDaysPregnant(int daysPregnant) {
		this.daysPregnant = daysPregnant;
	}
	
	private void setEstimatedDueDate(LocalDate estimatedDueDate) {
		this.estimatedDueDate = estimatedDueDate;
	}
	
}