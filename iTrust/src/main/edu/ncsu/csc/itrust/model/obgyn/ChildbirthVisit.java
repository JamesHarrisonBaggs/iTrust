package edu.ncsu.csc.itrust.model.obgyn;

import java.time.LocalDate;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "childbirth_visit")
public class ChildbirthVisit {

	/** MID of the patient */
	private long patientID;
	/** ID of the associated office visit */
	private long visitID;
	/** The date of the office visit */
	private LocalDate visitDate;
	/** True if the visit was pre-scheduled */
	private boolean preSchedule;
	/** The type of delivery */
	private String deliveryType;
	/** The dosage of pitocin */
	private int pitocin;
	/** The dosage of nitrous oxide (N20) */
	private int nitrousOxide;
	/** The dosage of pethidine */
	private int pethidine;
	/** The dosage of epidural anaesthesia */
	private int epiduralAnaesthesia;
	/** The dosage of magnesium sulfate */
	private int magnesiumSO4;
	
	public ChildbirthVisit() {
		this.patientID = -1;
		this.visitID = -1;
	}

	public long getPatientID() {
		return patientID;
	}

	public long getVisitID() {
		return visitID;
	}

	public LocalDate getVisitDate() {
		return visitDate;
	}

	public boolean isPreSchedule() {
		return preSchedule;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public int getPitocin() {
		return pitocin;
	}

	public int getNitrousOxide() {
		return nitrousOxide;
	}

	public int getPethidine() {
		return pethidine;
	}

	public int getEpiduralAnaesthesia() {
		return epiduralAnaesthesia;
	}

	public int getMagnesiumSO4() {
		return magnesiumSO4;
	}

	public void setPatientID(long patientID) {
		this.patientID = patientID;
	}

	public void setVisitID(long visitID) {
		this.visitID = visitID;
	}

	public void setVisitDate(LocalDate visitDate) {
		this.visitDate = visitDate;
	}

	public void setPreSchedule(boolean preSchedule) {
		this.preSchedule = preSchedule;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public void setPitocin(int pitocin) {
		this.pitocin = pitocin;
	}

	public void setNitrousOxide(int nitrousOxide) {
		this.nitrousOxide = nitrousOxide;
	}

	public void setPethidine(int pethidine) {
		this.pethidine = pethidine;
	}

	public void setEpiduralAnaesthesia(int epiduralAnaesthesia) {
		this.epiduralAnaesthesia = epiduralAnaesthesia;
	}

	public void setMagnesiumSO4(int magnesiumSO4) {
		this.magnesiumSO4 = magnesiumSO4;
	}
	
}
