package edu.ncsu.csc.itrust.model.obgyn;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;
import edu.ncsu.csc.itrust.model.ValidationFormat;

public class PregnancyValidator extends POJOValidator<Pregnancy> {

	/**
	 * Validates the contents of a PregnancyBean
	 * @param bean - the bean to validate
	 * @throws FormValidationException
	 */
	public void validate(Pregnancy bean) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		if (bean == null) {
			errorList.addIfNotNull("Bean cannot be null");
			throw new FormValidationException(errorList);
		}
		if (bean.getPatientId() < 0)
			errorList.addIfNotNull("Patient id cannot be negative");		
		if (bean.getDateOfBirth() == null)
			errorList.addIfNotNull("Date of birth cannot be null");
		if (bean.getDateOfBirth() != null)
			if (bean.getDateOfBirth().getYear() < bean.getYearOfConception())
				errorList.addIfNotNull("Year of conception cannot be after year of birth");			
		if (bean.getDaysPregnant() < 0)
			errorList.addIfNotNull("Days pregnant cannot be negative");
		if (bean.getHoursInLabor() < 0)
			errorList.addIfNotNull("Hours in labor cannot be negative");
		errorList.addIfNotNull(
			checkFormat("Delivery Type", bean.getDeliveryType(), ValidationFormat.DELIVERY_TYPE, false));
		if (bean.getAmount() <= 0)
			errorList.addIfNotNull("Amount cannot be zero or less");
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}

}
