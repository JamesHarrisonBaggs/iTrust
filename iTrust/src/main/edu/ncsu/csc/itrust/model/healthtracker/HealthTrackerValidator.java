package edu.ncsu.csc.itrust.model.healthtracker;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;
import edu.ncsu.csc.itrust.exception.ErrorList;

public class HealthTrackerValidator extends POJOValidator<HealthTrackerBean> {
	
	private static HealthTrackerValidator instance = null;

	/**
	 * The constructor for HealthTrackerValidator
	 */
	public HealthTrackerValidator() {

	}
	
	/**
	 * Returns the singleton instance of HealthTrackerValidator
	 * @return the singleton instance of HealthTrackerValidators
	 */
	public static HealthTrackerValidator getInstance() {
		if (instance == null) {
			instance = new HealthTrackerValidator();
		}
		return instance;
	}

	/**
	 * Validates the contents of a Health Tracker Bean
	 * 
	 * @param bean - the bean to validate
	 * @throws FormValidationException
	 */
	public void validate(HealthTrackerBean bean) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		if (bean.getPatientId() < 0)
			errorList.addIfNotNull("Patient ID cannot be negative");
		if (bean.getDate() == null)
			errorList.addIfNotNull("Date cannot be null");
		if (bean.getCalories() < 0)
			errorList.addIfNotNull("Calories cannot be negative");
		if (bean.getSteps() < 0)
			errorList.addIfNotNull("Steps cannot be negative");
		if (bean.getDistance() < 0.0)
			errorList.addIfNotNull("Distance cannot be negative");
		if (bean.getFloors() < 0)
			errorList.addIfNotNull("Floors cannot be negative");
		if (bean.getActivityCalories() < 0)
			errorList.addIfNotNull("Activity Calories cannot be negative");
		if (bean.getMinutesSedentary() < 0)
			errorList.addIfNotNull("Minutes Sedentary cannot be negative");
		if (bean.getMinutesLightlyActive() < 0)
			errorList.addIfNotNull("Minutes Lightly Active cannot be negative");
		if (bean.getMinutesFairlyActive() < 0)
			errorList.addIfNotNull("Minutes Fairly Active cannot be negative");
		if (bean.getMinutesVeryActive() < 0)
			errorList.addIfNotNull("Minutes Very Active cannot be negative");
		if (bean.getActiveHours() < 0)
			errorList.addIfNotNull("Active Hours cannot be negative");
		if (bean.getHeartrateLow() < 0)
			errorList.addIfNotNull("Heartrate Low cannot be negative");
		if (bean.getHeartrateHigh() < 0)
			errorList.addIfNotNull("Heartrate High cannot be negative");
		if (bean.getHeartrateAverage() < 0)
			errorList.addIfNotNull("Heartrate Average cannot be negative");
		if (bean.getUvExposure() < 0)
			errorList.addIfNotNull("UV Exposure cannot be negative");
		if (errorList.hasErrors()) {
			throw new FormValidationException(errorList);
		}
	}

}
