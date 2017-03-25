package edu.ncsu.csc.itrust.model.obgyn;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;

public class ObstetricsInitValidator {

	private static ObstetricsInitValidator instance = null;

	/**
	 * Constructor for ObstetricsValidator
	 */
	public ObstetricsInitValidator() {
		
	}
	
	/**
	 * Returns the singleton instance of ObstetricsInitValidator
	 * @return the singleton instance of ObstetricsInitValidator
	 */
	public static ObstetricsInitValidator getInstance() {
		if (instance == null) instance = new ObstetricsInitValidator();
		return instance;
	}
	
	/**
	 * Validates the contents of a Obstetrics Bean
	 * @param bean - the bean to validate
	 * @throws FormValidationException
	 */
	public void validate(ObstetricsInit bean) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		if (bean.getPatientId() < 0)
			errorList.addIfNotNull("Patient id cannot be negative");
		if (bean.getInitDate() == null)
			errorList.addIfNotNull("Initialization date cannot be null");
		if (bean.getLastMenstrualPeriod() == null)
			errorList.addIfNotNull("Last menstrual period cannot be null");
		if (bean.getEstimatedDueDate() == null)
			errorList.addIfNotNull("Expected due date cannot be null");
		if (bean.getDaysPregnant() < 0)
			errorList.addIfNotNull("Days pregnant cannot be negative");
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}
		
}
