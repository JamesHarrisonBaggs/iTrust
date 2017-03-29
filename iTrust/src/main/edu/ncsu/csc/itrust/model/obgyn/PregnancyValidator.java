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
		if (bean.getDateOfBirth() != null)
			if (bean.getDateOfBirth().getYear() < bean.getYearOfConception())
				errorList.addIfNotNull("Year of conception cannot be after year of birth");			
		if (bean.getDaysPregnant() < 0)
			errorList.addIfNotNull("Days pregnant cannot be negative");
		if (bean.getHoursInLabor() < 0)
			errorList.addIfNotNull("Hours in labor cannot be negative");
		// TODO validate weight gain
		if (!isValidDeliveryType(bean.getDeliveryType()))
			errorList.addIfNotNull("Delivery type is invalid");
		// TODO validate deliveryType
		if (bean.getAmount() <= 0)
			errorList.addIfNotNull("Amount cannot be zero or less");
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}
	
	/**
	 * Returns true if a specified delivery type is valid
	 */
	private boolean isValidDeliveryType(String deliveryType) {
		if (deliveryType == null) {
			return false;
		}
		switch (deliveryType) {
			case "vaginal":
				return true;
			case "vacuum":
				return true;
			case "forceps":
				return true;
			case "caesarean":
				return true;
			case "miscarriage":
				return true;
		}
		return false;
	}

}
