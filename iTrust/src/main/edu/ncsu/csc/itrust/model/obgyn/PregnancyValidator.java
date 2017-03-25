package edu.ncsu.csc.itrust.model.obgyn;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;

public class PregnancyValidator extends POJOValidator<PregnancyBean> {

	private static PregnancyValidator instance = null;
	
	/**
	 * @return the singleton instance of PregnancyValidator
	 */
	public static PregnancyValidator getInstance() {
		if (instance == null) instance = new PregnancyValidator();
		return instance;
	}
	
	/**
	 * Validates the contents of a PregnancyBean
	 * @param obj - the bean to validate
	 * @throws FormValidationException
	 */
	public void validate(PregnancyBean obj) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		
		if (obj.getPatientId() < 0)
			errorList.addIfNotNull("Patient id cannot be negative");		
		if (obj.getDateOfBirth() == null)
			errorList.addIfNotNull("Date of birth cannot be null");
		if (obj.getDaysPregnant() < 0)
			errorList.addIfNotNull("Days pregnant cannot be negative");
		if (obj.getHoursInLabor() < 0)
			errorList.addIfNotNull("Hours in labor cannot be negative");
		if (obj.getAmount() < 0)
			errorList.addIfNotNull("Amount cannot be negative");
		if (obj.getAmount() == 0)
			errorList.addIfNotNull("Amount cannot be zero");
		// TODO validate more stuff
		
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
		
	}

}
