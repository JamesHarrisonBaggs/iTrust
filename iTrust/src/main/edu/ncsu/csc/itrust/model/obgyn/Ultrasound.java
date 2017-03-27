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
	private double estimatedFetalWeight;
	
	public Ultrasound() {
		this.patientId = -1;
		this.visitId = -1;
		this.fetusId = -1;
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
	public double getEstimatedFetalWeight() {
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
	public void setCrownRumpLength(int crl) {
		this.crownRumpLength = crl;
	}
	public void setBiparietalDiameter(int bpd) {
		this.biparietalDiameter = bpd;
	}
	public void setHeadCircumference(int hc) {
		this.headCircumference = hc;
	}
	public void setFemurLength(int fl) {
		this.femurLength = fl;
	}
	public void setOccipitofrontalDiameter(int ofd) {
		this.occipitofrontalDiameter = ofd;
	}
	public void setAbdominalCircumference(int ac) {
		this.abdominalCircumference = ac;
	}
	public void setHumerusLength(int hl) {
		this.humerusLength = hl;
	}
	public void setEstimatedFetalWeight(double efw) {
		this.estimatedFetalWeight = efw;
	}
	
}
