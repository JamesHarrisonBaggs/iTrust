package edu.ncsu.csc.itrust.model.obgyn;

import java.time.LocalDateTime;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "childbirth_bean")
public class Childbirth {
	
	/** MID of the parent */
	private long parentID;
	/** ID of the associated office visit */
	private long visitID;
	/** ID of the child */
	private int birthID;
	/** Date and time of birth */
	private LocalDateTime birthdate;
	/** True if date and time is estimated */
	private boolean estimated;
	/** Gender of the child */
	private String gender;
	
	public Childbirth() {
		this.parentID = -1;
		this.visitID = -1;
		this.birthID = -1;
	}

	public long getParentID() {
		return parentID;
	}

	public long getVisitID() {
		return visitID;
	}

	public int getBirthID() {
		return birthID;
	}

	public LocalDateTime getBirthdate() {
		return birthdate;
	}

	public boolean isEstimated() {
		return estimated;
	}

	public String getGender() {
		return gender;
	}

	public void setParentID(long parentID) {
		this.parentID = parentID;
	}

	public void setVisitID(long visitID) {
		this.visitID = visitID;
	}

	public void setBirthID(int birthID) {
		this.birthID = birthID;
	}

	public void setBirthdate(LocalDateTime birthdate) {
		this.birthdate = birthdate;
	}

	public void setEstimated(boolean estimated) {
		this.estimated = estimated;
	}

	public void setGender(String gender) {
		if (gender.equalsIgnoreCase("m")) gender = "Male";
		if (gender.equalsIgnoreCase("male")) gender = "Male";
		if (gender.equalsIgnoreCase("f")) gender = "Female";
		if (gender.equalsIgnoreCase("female")) gender = "Female";
		this.gender = gender;
	}
	
}
