package edu.ncsu.csc.itrust.model.obgyn;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;

public class ObstetricsInitValidator extends POJOValidator<ObstetricsInit> {

	/**
	 * Validates the contents of a Obstetrics Bean
	 * @param obj - the bean to validate
	 * @throws FormValidationException
	 */
	public void validate(ObstetricsInit obj) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		if (obj.getPatientId() < 0)
			errorList.addIfNotNull("Patient id cannot be negative");
		if (obj.getInitDate() == null)
			errorList.addIfNotNull("Initialization date cannot be null");
		if (obj.getLastMenstrualPeriod() == null)
			errorList.addIfNotNull("Last menstrual period cannot be null");
		if (obj.getEstimatedDueDate() == null)
			errorList.addIfNotNull("Expected due date cannot be null");
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}
		
}
