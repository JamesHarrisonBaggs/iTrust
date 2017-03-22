package edu.ncsu.csc.itrust.controller.obGyn;

import java.util.Date;
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
	private Date initializationDate;
	/** Date of last menstrual period */
	private Date lastMenstrualPeriod;
	/** Estimated due date */
	private Date estimatedDueDate;
	/** Number of days pregnant */
	private int daysPregnant;
	/** Date Created */
	private Date dateCreated;
	
	/**
	 * Construct an ObstetricsBean
	 */
	public ObstetricsInitBean() {
		
	}
	
	/** GETTERS **/

	/**
	 * @return the patientId
	 */
	public long getPatientId() {
		return patientId;
	}

	/**
	 * @return the initializationDate
	 */
	public Date getInitializationDate() {
		return initializationDate;
	}

	/**
	 * @return the lastMenstrualPeriod
	 */
	public Date getLastMenstrualPeriod() {
		return lastMenstrualPeriod;
	}

	/**
	 * @return the estimatedDueDate
	 */
	public Date getEstimatedDueDate() {
		return estimatedDueDate;
	}

	/**
	 * @return the daysPregnant
	 */
	public int getDaysPregnant() {
		return daysPregnant;
	}

	/** SETTERS **/
	
	/**
	 * @param patientId the patientId to set
	 */
	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	/**
	 * @param initializationDate the initializationDate to set
	 */
	public void setInitializationDate(Date initializationDate) {
		this.initializationDate = initializationDate;
	}

	/**
	 * @param lastMenstrualPeriod the lastMenstrualPeriod to set
	 */
	public void setLastMenstrualPeriod(Date lastMenstrualPeriod) {
		this.lastMenstrualPeriod = lastMenstrualPeriod;
		// TODO EDD = LMP + 280 days
		this.setEstimatedDueDate(lastMenstrualPeriod);
		// TODO Time pregnant
		this.setDaysPregnant(0);
	}

	/**
	 * @param estimatedDueDate the estimatedDueDate to set
	 */
	private void setEstimatedDueDate(Date estimatedDueDate) {
		this.estimatedDueDate = estimatedDueDate;
	}

	/**
	 * @param daysPregnant the daysPregnant to set
	 */
	private void setDaysPregnant(int daysPregnant) {
		this.daysPregnant = daysPregnant;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
}