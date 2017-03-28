package edu.ncsu.csc.itrust.model.obgyn;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;

public class PregnancyValidator extends POJOValidator<Pregnancy> {

	/**
	 * Validates the contents of a PregnancyBean
	 * @param bean - the bean to validate
	 * @throws FormValidationException
	 */
	public void validate(Pregnancy bean) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		
		if (bean.getPatientId() < 0)
			errorList.addIfNotNull("Patient id cannot be negative");		
		if (bean.getDateOfBirth() == null)
			errorList.addIfNotNull("Date of birth cannot be null");
		if (bean.getDaysPregnant() < 0)
			errorList.addIfNotNull("Days pregnant cannot be negative");
		if (bean.getHoursInLabor() < 0)
			errorList.addIfNotNull("Hours in labor cannot be negative");
		if (bean.getAmount() <= 0)
			errorList.addIfNotNull("Amount cannot be zero or less");
		// TODO validate more stuff
		
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
		
	}

}
