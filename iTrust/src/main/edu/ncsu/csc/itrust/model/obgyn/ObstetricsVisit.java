package edu.ncsu.csc.itrust.model.obgyn;

import java.time.LocalDateTime;
import javax.faces.bean.ManagedBean;

@ManagedBean(name="obstetrics_visit")
public class ObstetricsVisit {
	
	/** The MID of the patient */
	private long patientId;
	/** ID of the associated office visit */
	private long visitId;
	/** The date of the office visit */
	private LocalDateTime visitDate;
	/** Number of weeks pregnant */
	private int weeksPregnant;
	/** The patient weight in lbs */
	private double weight;
	/** The blood pressure in mmHg */
	private String bloodPressure;
	/** The heart rate of the fetus in bpm */
	private int fetalHeartRate;
	/** The number of fetuses */
	private int amount;
	/** Boolean for if the placenta is low lying */
	private boolean lowLyingPlacenta;
	/** Boolean for RH-Flag */
	private boolean rhFlag;
	
	public ObstetricsVisit() {
		bloodPressure = "000/000";
	}
	/** GETTERS **/
	public long getPatientId() {
		return patientId;
	}
	public long getVisitId() {
		return visitId;
	}
	public LocalDateTime getVisitDate() {
		return visitDate;
	}
	public int getWeeksPregnant() {
		return weeksPregnant;
	}
	public double getWeight() {
		return weight;
	}
	public String getBloodPressure() {
		return bloodPressure;
	}
	public int getFetalHeartRate() {
		return fetalHeartRate;
	}
	public int getAmount() {
		return amount;
	}
	public boolean isLowLyingPlacenta() {
		return lowLyingPlacenta;
	}
	
	/** SETTERS **/
	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}
	public void setVisitId(long visitId) {
		this.visitId = visitId;
	}
	public void setVisitDate(LocalDateTime visitDate) {
		this.visitDate = visitDate;
	}
	public void setWeeksPregnant(int weeksPregnant) {
		this.weeksPregnant = weeksPregnant;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public void setBloodPressure(String bloodPressure) {
		this.bloodPressure = bloodPressure;
	}
	public void setFetalHeartRate(int fetalHeartRate) {
		this.fetalHeartRate = fetalHeartRate;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public void setLowLyingPlacenta(boolean lowLyingPlacenta) {
		this.lowLyingPlacenta = lowLyingPlacenta;
	}
	public boolean isRhFlag() {
		return rhFlag;
	}
	public void setRhFlag(boolean rhFlag) {
		this.rhFlag = rhFlag;
	}
	
}
