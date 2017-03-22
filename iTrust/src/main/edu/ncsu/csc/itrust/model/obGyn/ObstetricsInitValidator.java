package edu.ncsu.csc.itrust.model.obGyn;

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
	public void validate(ObstetricsInitBean bean) throws FormValidationException {
		ErrorList errors = new ErrorList();
		
		// TODO validate
		
		if (errors.hasErrors()) {
			throw new FormValidationException(errors);
		}
	}
		
}
