package edu.ncsu.csc.itrust.model.obgyn;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;

public class ObstetricsInitValidator extends POJOValidator<ObstetricsInit> {

	/**
	 * Validates the contents of a Obstetrics Bean
	 * @param bean - the bean to validate
	 * @throws FormValidationException
	 */
	public void validate(ObstetricsInit bean) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		if (bean == null) {
			errorList.addIfNotNull("Bean cannot be null");
			throw new FormValidationException(errorList);
		}
		if (bean.getPatientId() < 0)
			errorList.addIfNotNull("Patient id cannot be negative");
		if (bean.getInitDate() == null)
			errorList.addIfNotNull("Initialization date cannot be null");
		if (bean.getLastMenstrualPeriod() == null)
			errorList.addIfNotNull("Last menstrual period cannot be null");
		if (bean.getEstimatedDueDate() == null)
			errorList.addIfNotNull("Expected due date cannot be null");
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}
		
}
