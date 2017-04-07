package edu.ncsu.csc.itrust.model.obgyn;

import java.time.LocalDate;
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
	
	public ObstetricsInit() {
		this.patientId = -1;
		this.initDate = LocalDate.now();
		this.lastMenstrualPeriod = LocalDate.now();
	}
		
	public long getPatientId() {
		return patientId;
	}

	public LocalDate getInitDate() {
		return initDate;
	}
	
	public LocalDate getLastMenstrualPeriod() {
		return lastMenstrualPeriod;
	}
	
	/**
	 * Returns the estimated due date, which is 280 days past the last menstrual period
	 */
	public LocalDate getEstimatedDueDate() {
		if (lastMenstrualPeriod != null) {
			return lastMenstrualPeriod.plus(280, ChronoUnit.DAYS);			
		}
		return null;
	}

	/**
	 * Returns the number of days pregnant, which is the number of days between
	 * the last menstrual period and initialization date. Note that this is misleading
	 * on days following the initialization date, but also avoids cases further
	 * in the future where it says something like "8000 days pregnant". 
	 */
	public long getDaysPregnant() {
		if (lastMenstrualPeriod != null && initDate != null) {
			return ChronoUnit.DAYS.between(lastMenstrualPeriod.atStartOfDay(), initDate.atStartOfDay());						
		}
		return -1;
	}

	/**
	 * Returns a String describing the time pregnant at the time of initialization
	 */
	public String getTimePregnant() {
		long daysPregnant = getDaysPregnant();
		
		// parse weeks pregnant
		StringBuilder sb = new StringBuilder();
		long weeks = daysPregnant/7;
		sb.append(weeks);
		sb.append(weeks == 1 ? " week " : " weeks ");
		
		// add days pregnant
		long days = daysPregnant % 7;
		sb.append(days);
		sb.append(days == 1 ? " day" : " days");
		return sb.toString();
	}

	/** SETTERS **/

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public void setInitDate(LocalDate initDate) {
		this.initDate = initDate;
	}
	
	public void setLastMenstrualPeriod(LocalDate lastMenstrualPeriod) {
		this.lastMenstrualPeriod = lastMenstrualPeriod;
	}

}