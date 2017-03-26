package edu.ncsu.csc.itrust.model.obgyn;

import java.time.LocalDateTime;

import javax.faces.bean.ManagedBean;

@ManagedBean(name="ultrasound")
public class Ultrasound {
	
	/** The MID of the patient */
	private long patientId;
	/** ID of the associated office visit */
	private long visitId;
	/** Date of the associated office visit */
	private LocalDateTime visitDate;
	/** ID of the fetus */
	private int fetusId;
	/** Crown rump length (CRL) in mm */
	private int crownRumpLength;
	/** Biparietal diameter (BPD) in mm */
	private int biparietalDiameter;
	/** Head circumference (HC) in mm */
	private int headCircumference;
	/** Femur length (FL) in mm */
	private int femurLength;
	/** Occipitofrontal diameter (OFD) in mm */
	private int occipitofrontalDiameter;
	/** Abdominal circumference (AC) in mm */
	private int abdominalCircumference;
	/** Humerus length (HL) in mm */
	private int humerusLength;
	/** Estimated fetal weight (grams) */
	private int estimatedFetalWeight;
	
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
	public int getFetusId() {
		return fetusId;
	}
	public int getCrownRumpLength() {
		return crownRumpLength;
	}
	public int getBiparietalDiameter() {
		return biparietalDiameter;
	}
	public int getHeadCircumference() {
		return headCircumference;
	}
	public int getFemurLength() {
		return femurLength;
	}
	public int getOccipitofrontalDiameter() {
		return occipitofrontalDiameter;
	}
	public int getAbdominalCircumference() {
		return abdominalCircumference;
	}
	public int getHumerusLength() {
		return humerusLength;
	}
	public int getEstimatedFetalWeight() {
		return estimatedFetalWeight;
	}
	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}
	
	/** SETTERS **/
	public void setVisitId(long visitId) {
		this.visitId = visitId;
	}
	public void setVisitDate(LocalDateTime visitDate) {
		this.visitDate = visitDate;
	}
	public void setFetusId(int fetusId) {
		this.fetusId = fetusId;
	}
	public void setCrownRumpLength(int crownRumpLength) {
		this.crownRumpLength = crownRumpLength;
	}
	public void setBiparietalDiameter(int biparietalDiameter) {
		this.biparietalDiameter = biparietalDiameter;
	}
	public void setHeadCircumference(int headCircumference) {
		this.headCircumference = headCircumference;
	}
	public void setFemurLength(int femurLength) {
		this.femurLength = femurLength;
	}
	public void setOccipitofrontalDiameter(int occipitofrontalDiameter) {
		this.occipitofrontalDiameter = occipitofrontalDiameter;
	}
	public void setAbdominalCircumference(int abdominalCircumference) {
		this.abdominalCircumference = abdominalCircumference;
	}
	public void setHumerusLength(int humerusLength) {
		this.humerusLength = humerusLength;
	}
	public void setEstimatedFetalWeight(int estimateFetalWeight) {
		this.estimatedFetalWeight = estimateFetalWeight;
	}
	
}
