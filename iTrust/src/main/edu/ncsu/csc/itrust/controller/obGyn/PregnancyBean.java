package edu.ncsu.csc.itrust.controller.obGyn;

import java.util.Date;

public class PregnancyBean {
	
	/** MID of the patient */
	private long patientId;
	/** Date of birth */
	private Date dateOfBirth;
	/** Year of conception */
	private int yearOfConception;
	/** Number of days pregnant */
	private int daysPregnant;
	/** Number of hours in labor */
	private int hoursInLabor;
	/** Weight gain during pregnancy */
	private double weightGain;
	/** Type of delivery (vaginal delivery, vacuum assist, forceps assist, caesarean section, miscarriage */
	private String deliveryType;
	/** Whether the pregnant is a multiple */
	private int amount;
	
	/**
	 * Construct a PregnancyBean
	 */
	public PregnancyBean() {
		
	}

	/** GETTERS **/
	
	/**
	 * @return the patientId
	 */
	public long getPatientId() {
		return patientId;
	}

	/**
	 * @return the dateOfBirth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @return the yearOfConception
	 */
	public int getYearOfConception() {
		return yearOfConception;
	}

	/**
	 * @return the daysPregnant
	 */
	public int getDaysPregnant() {
		return daysPregnant;
	}

	/**
	 * @return the hoursInLabor
	 */
	public int getHoursInLabor() {
		return hoursInLabor;
	}

	/**
	 * @return the weightGain
	 */
	public double getWeightGain() {
		return weightGain;
	}

	/**
	 * @return the deliveryType
	 */
	public String getDeliveryType() {
		return deliveryType;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}
	
	/** SETTERS **/

	/**
	 * @param patientId the patientId to set
	 */
	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @param yearOfConception the yearOfConception to set
	 */
	public void setYearOfConception(int yearOfConception) {
		this.yearOfConception = yearOfConception;
	}

	/**
	 * @param daysPregnant the daysPregnant to set
	 */
	public void setDaysPregnant(int daysPregnant) {
		this.daysPregnant = daysPregnant;
	}

	/**
	 * @param hoursInLabor the hoursInLabor to set
	 */
	public void setHoursInLabor(int hoursInLabor) {
		this.hoursInLabor = hoursInLabor;
	}

	/**
	 * @param weightGain the weightGain to set
	 */
	public void setWeightGain(double weightGain) {
		this.weightGain = weightGain;
	}

	/**
	 * @param deliveryType the deliveryType to set
	 */
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

}
