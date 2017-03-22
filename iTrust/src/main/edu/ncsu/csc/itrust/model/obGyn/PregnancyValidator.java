package edu.ncsu.csc.itrust.model.obGyn;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;

public class PregnancyValidator {

	private static PregnancyValidator instance = null;
	
	/**
	 * Constructor for PregnancyValidator
	 */
	public PregnancyValidator() {
		
	}
	
	/**
	 * @return the singleton instance of PregnancyValidator
	 */
	public static PregnancyValidator getInstance() {
		if (instance == null) instance = new PregnancyValidator();
		return instance;
	}
	
	/**
	 * Validates the contents of a PregnancyBean
	 * @param bean - the bean to validate
	 * @throws FormValidationException
	 */
	public void validate(PregnancyBean bean) throws FormValidationException {
		ErrorList errors = new ErrorList();
		
		// TODO validate
		
		if (errors.hasErrors()) {
			throw new FormValidationException(errors);
		}
	}
	
}
