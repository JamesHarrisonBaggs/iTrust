package edu.ncsu.csc.itrust.model.obgyn;

import java.time.LocalDate;

import javax.faces.bean.ManagedBean;

import java.sql.Timestamp;
@ManagedBean(name = "pregBean")
public class Pregnancy {
		
	/** MID of the patient */
	private long patientId;
	/** Date of birth */
	private LocalDate dateOfBirth;
	/** Year of conception */
	private int yearOfConception;
	/** Number of days pregnant */
	private int daysPregnant;
	/** Number of hours in labor */
	private int hoursInLabor;
	/** Weight gain during pregnancy */
	private double weightGain;
	/** Type of delivery */
	private String deliveryType;
	/** Whether the pregnant is a multiple */
	private int amount;
	
	/**
	 * Enumerated type representing a fixed number of delivery types
	 */
	public enum DeliveryType {
		VAGINAL_DELIVERY, VACUUM_ASSIST, FORCEPS_ASSIST, C_SECTION, MISCARRIAGE;
	}
	
	// TODO update so this enum is utilized
	
	/**
	 * @return the dateOfBirth in java.sql.Timestamp form
	 */
	public Timestamp getDOBTimestamp() {
		return Timestamp.valueOf(dateOfBirth.atStartOfDay());
	}
	
	/**
	 * @param dateOfBirth the dateOfBirth as java.sql.Timestamp
	 */
	public void setDOBTimestamp(Timestamp dateOfBirth) {
		this.dateOfBirth = dateOfBirth.toLocalDateTime().toLocalDate();
	}

	/** GETTERS **/
	
	public long getPatientId() {
		return patientId;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public int getYearOfConception() {
		return yearOfConception;
	}

	public int getDaysPregnant() {
		return daysPregnant;
	}

	public int getHoursInLabor() {
		return hoursInLabor;
	}

	public double getWeightGain() {
		return weightGain;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public int getAmount() {
		return amount;
	}
	
	/** SETTERS */

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setYearOfConception(int yearOfConception) {
		this.yearOfConception = yearOfConception;
	}

	public void setDaysPregnant(int daysPregnant) {
		this.daysPregnant = daysPregnant;
	}

	public void setHoursInLabor(int hoursInLabor) {
		this.hoursInLabor = hoursInLabor;
	}

	public void setWeightGain(double weightGain) {
		this.weightGain = weightGain;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
