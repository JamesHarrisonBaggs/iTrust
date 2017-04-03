package edu.ncsu.csc.itrust.model.obgyn;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;
import edu.ncsu.csc.itrust.model.ValidationFormat;

public class ObstetricsVisitValidator extends POJOValidator<ObstetricsVisit> {

	/**
	 * Validates the contents of a Obstetrics Bean
	 * 
	 * @param bean
	 *            - the bean to validate
	 * @throws FormValidationException
	 */
	@Override
	public void validate(ObstetricsVisit bean) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		if (bean.getPatientId() < 0)
			errorList.addIfNotNull("Patient ID cannot be negative");
		if (bean.getVisitId() < 0)
			errorList.addIfNotNull("Office visit ID cannot be negative");
		if (bean.getVisitDate() == null)
			errorList.addIfNotNull("Office visit date cannot be null");
		if (bean.getWeeksPregnant() < 0)
			errorList.addIfNotNull("Weeks pregnant cannot be negative");
		if (bean.getWeight() <= 0)
			errorList.addIfNotNull("Weight cannot be zero or less");
		errorList.addIfNotNull(
				checkFormat("Blood Pressure", bean.getBloodPressure(), ValidationFormat.BLOOD_PRESSURE_OV, false));
		if (bean.getFetalHeartRate() < 0)
			errorList.addIfNotNull("Fetal heart rate cannot be negative");
		if (bean.getAmount() < 1)
			errorList.addIfNotNull("Multiples cannot be less than 1");
		// doesn't validate booleans
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}

}