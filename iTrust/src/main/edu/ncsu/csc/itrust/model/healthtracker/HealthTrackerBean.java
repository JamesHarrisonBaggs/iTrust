package edu.ncsu.csc.itrust.model.healthtracker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.sql.Timestamp;
import javax.faces.bean.ManagedBean;

/**
 * A class representing a set of health tracker data for a
 * specified patient on a specified date
 * 
 * @author ereinst
 *
 */
@ManagedBean(name = "htBean")
public class HealthTrackerBean {
	
	/** MID of the patient */
	private long patientId;
	/** Date of the data */
	private LocalDate date;
	/** Calories burned */
	private int calories;
	/** Steps covered */
	private int steps;
	/** Distance covered (miles) */
	private double distance;
	/** Floors covered */
	private int floors;
	/** Activity calories */
	private int activityCalories;
	/** Minutes sedentary */
	private int minutesSedentary;
	/** Minutes lightly active */
	private int minutesLightlyActive;
	/** Minutes fairly active */
	private int minutesFairlyActive;
	/** Minutes very active */
	private int minutesVeryActive;
	/** Hours active */
	private int activeHours;
	/** Lowest heart rate (bpm) */
	private int heartrateLow;
	/** Highest heart rate (bpm) */
	private int heartrateHigh;
	/** Average heart rate (bpm) */
	private int heartrateAverage;
	/** UV exposure */
	private int uvExposure;
	
	/**
	 * Constructs a default HealthTrackerBean
	 */
	public HealthTrackerBean() {
		this.patientId = 0;
		this.date = null;
		this.calories = 0;
		this.steps = 0;
		this.distance = 0.0;
		this.floors = 0;
		this.activityCalories = 0;
		this.minutesSedentary = 0;
		this.minutesLightlyActive = 0;
		this.minutesFairlyActive = 0;
		this.minutesVeryActive = 0;
		this.activeHours = 0;
		this.heartrateLow = 0;
		this.heartrateHigh = 0;
		this.heartrateAverage = 0;
		this.uvExposure = 0;
	}
	
	public HealthTrackerBean(long patientId, LocalDate date, int calories, int steps, double distance, int floors,
			int activityCalories, int minutesSedentary, int minutesLightlyActive, int minutesFairlyActive,
			int minutesVeryActive, int activeHours, int heartrateLow, int heartrateHigh, int heartrateAverage,
			int uvExposure) {
		this.patientId = patientId;
		this.date = date;
		this.calories = calories;
		this.steps = steps;
		this.distance = distance;
		this.floors = floors;
		this.activityCalories = activityCalories;
		this.minutesSedentary = minutesSedentary;
		this.minutesLightlyActive = minutesLightlyActive;
		this.minutesFairlyActive = minutesFairlyActive;
		this.minutesVeryActive = minutesVeryActive;
		this.activeHours = activeHours;
		this.heartrateLow = heartrateLow;
		this.heartrateHigh = heartrateHigh;
		this.heartrateAverage = heartrateAverage;
		this.uvExposure = uvExposure;
	}

	/****** Date/Time Conversions ******/
	
	/**
	 * @return java.time.LocalDate to java.sql.Timestamp
	 */
	public Timestamp getTimestamp() {
		return Timestamp.valueOf(date.atStartOfDay());
	}
	
	/**
	 * @param java.sql.Timestamp to java.time.LocalDate
	 */
	public void setTimestamp(Timestamp timestamp) {
		this.date = timestamp.toLocalDateTime().toLocalDate();
	}
	
	/**
	 * @return java.time.LocalDate to java.util.Date
	 */
	public Date getDateStandard() {
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	/**
	 * @param java.util.Date to java.time.LocalDate
	 */
	public void setDateStandard(Date date) {
		this.date = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public String getDateString() {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("M/d/yyyy");
		return date.format(fmt);
	}
	
	public void setDateString(String date) {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("M/d/yyyy");
		this.date = LocalDate.parse(date, fmt);
	}
	
	/******** GETTERS ********/	
	
	/**
	 * @return the patientId
	 */
	public long getPatientId() {
		return patientId;
	}

	/**
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * @return the calories
	 */
	public int getCalories() {
		return calories;
	}

	/**
	 * @return the steps
	 */
	public int getSteps() {
		return steps;
	}

	/**
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * @return the floors
	 */
	public int getFloors() {
		return floors;
	}

	/**
	 * @return the activityCalories
	 */
	public int getActivityCalories() {
		return activityCalories;
	}

	/**
	 * @return the minutesSedentary
	 */
	public int getMinutesSedentary() {
		return minutesSedentary;
	}

	/**
	 * @return the minutesLightlyActive
	 */
	public int getMinutesLightlyActive() {
		return minutesLightlyActive;
	}

	/**
	 * @return the minutesFairlyActive
	 */
	public int getMinutesFairlyActive() {
		return minutesFairlyActive;
	}

	/**
	 * @return the minutesVeryActive
	 */
	public int getMinutesVeryActive() {
		return minutesVeryActive;
	}

	/**
	 * @return the activeHours
	 */
	public int getActiveHours() {
		return activeHours;
	}

	/**
	 * @return the heartRateLow
	 */
	public int getHeartrateLow() {
		return heartrateLow;
	}

	/**
	 * @return the heartrateHigh
	 */
	public int getHeartrateHigh() {
		return heartrateHigh;
	}

	/**
	 * @return the average heart rate
	 */
	public int getHeartrateAverage() {
		return heartrateAverage;
	}

	/**
	 * @return the uvExposure
	 */
	public int getUvExposure() {
		return uvExposure;
	}
	
	/******** SETTERS ********/

	/**
	 * @param patientId the patientId to set
	 */
	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * @param calories the calories to set
	 */
	public void setCalories(int calories) {
		this.calories = calories;
	}

	/**
	 * @param steps the steps to set
	 */
	public void setSteps(int steps) {
		this.steps = steps;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * @param floors the floors to set
	 */
	public void setFloors(int floors) {
		this.floors = floors;
	}

	/**
	 * @param activityCalories the activityCalories to set
	 */
	public void setActivityCalories(int activityCalories) {
		this.activityCalories = activityCalories;
	}

	/**
	 * @param minutesSedentary the minutesSedentary to set
	 */
	public void setMinutesSedentary(int minutesSedentary) {
		this.minutesSedentary = minutesSedentary;
	}

	/**
	 * @param minutesLightlyActive the minutesLightlyActive to set
	 */
	public void setMinutesLightlyActive(int minutesLightlyActive) {
		this.minutesLightlyActive = minutesLightlyActive;
	}

	/**
	 * @param minutesFairlyActive the minutesFairlyActive to set
	 */
	public void setMinutesFairlyActive(int minutesFairlyActive) {
		this.minutesFairlyActive = minutesFairlyActive;
	}

	/**
	 * @param minutesVeryActive the minutesVeryActive to set
	 */
	public void setMinutesVeryActive(int minutesVeryActive) {
		this.minutesVeryActive = minutesVeryActive;
	}

	/**
	 * @param activeHours the activeHours to set
	 */
	public void setActiveHours(int activeHours) {
		this.activeHours = activeHours;
	}

	/**
	 * @param heartRateLow the heartRateLow to set
	 */
	public void setHeartrateLow(int heartRateLow) {
		this.heartrateLow = heartRateLow;
	}

	/**
	 * @param heartrateHigh the heartrateHigh to set
	 */
	public void setHeartrateHigh(int heartrateHigh) {
		this.heartrateHigh = heartrateHigh;
	}

	/**
	 * @param heartRateAvg the heartRateAvg to set
	 */
	public void setHeartrateAverage(int heartRateAvg) {
		this.heartrateAverage = heartRateAvg;
	}

	/**
	 * @param uvExposure the uvExposure to set
	 */
	public void setUvExposure(int uvExposure) {
		this.uvExposure = uvExposure;
	}
		
	/**
	 * toString() method for a HealthTrackerBean
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HealthTrackerBean [patientId=" + patientId + ", date=" + date + ", calories=" + calories + ", steps="
				+ steps + ", distance=" + distance + ", floors=" + floors + ", activityCalories=" + activityCalories
				+ ", minutesSedentary=" + minutesSedentary + ", minutesLightlyActive=" + minutesLightlyActive
				+ ", minutesFairlyActive=" + minutesFairlyActive + ", minutesVeryActive=" + minutesVeryActive
				+ ", activeHours=" + activeHours + ", heartrateLow=" + heartrateLow + ", heartrateHigh=" + heartrateHigh
				+ ", heartrateAverage=" + heartrateAverage + ", uvExposure=" + uvExposure + "]";
	}

	/**
	 * equals() method for a HealthTrackerBean
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HealthTrackerBean other = (HealthTrackerBean) obj;
		if (activeHours != other.activeHours)
			return false;
		if (activityCalories != other.activityCalories)
			return false;
		if (calories != other.calories)
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (Double.doubleToLongBits(distance) != Double.doubleToLongBits(other.distance))
			return false;
		if (floors != other.floors)
			return false;
		if (heartrateAverage != other.heartrateAverage)
			return false;
		if (heartrateHigh != other.heartrateHigh)
			return false;
		if (heartrateLow != other.heartrateLow)
			return false;
		if (minutesFairlyActive != other.minutesFairlyActive)
			return false;
		if (minutesLightlyActive != other.minutesLightlyActive)
			return false;
		if (minutesSedentary != other.minutesSedentary)
			return false;
		if (minutesVeryActive != other.minutesVeryActive)
			return false;
		if (patientId != other.patientId)
			return false;
		if (steps != other.steps)
			return false;
		if (uvExposure != other.uvExposure)
			return false;
		return true;
	}
	
}
