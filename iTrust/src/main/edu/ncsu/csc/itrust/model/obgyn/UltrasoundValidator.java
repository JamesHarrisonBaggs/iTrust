package edu.ncsu.csc.itrust.model.obgyn;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;

public class UltrasoundValidator extends POJOValidator<Ultrasound> {

	/**
	 * Validates the contents of an Ultrasound Bean
	 */
	@Override
	public void validate(Ultrasound bean) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		if (bean == null) {
			errorList.addIfNotNull("Bean cannot be null");
			throw new FormValidationException(errorList);
		}
		if (bean.getPatientId() < 0)
			errorList.addIfNotNull("Patient id cannot be negative");
		if (bean.getVisitId() < 0)
			errorList.addIfNotNull("Office visit id cannot be negative");
		if (bean.getVisitDate() == null)
			errorList.addIfNotNull("Office visit date cannot be null");
		if (bean.getFetusId() < 0)
			errorList.addIfNotNull("Fetus id cannot be negative");
		if (bean.getCrownRumpLength() <= 0)
			errorList.addIfNotNull("Crown rump length cannot be zero or less");
		if (bean.getBiparietalDiameter() <= 0)
			errorList.addIfNotNull("Biparietal diameter cannot be zero or less");
		if (bean.getHeadCircumference() <= 0)
			errorList.addIfNotNull("Head circumference cannot be zero or less");
		if (bean.getFemurLength() <= 0)
			errorList.addIfNotNull("Femur length cannot be zero or less");
		if (bean.getOccipitofrontalDiameter() <= 0)
			errorList.addIfNotNull("Occipitofrontal diameter cannot be zero or less");
		if (bean.getAbdominalCircumference() <= 0)
			errorList.addIfNotNull("Abdominal circumference cannot be zero or less");
		if (bean.getHumerusLength() <= 0)
			errorList.addIfNotNull("Humerus length cannot be zero or less");
		if (bean.getEstimatedFetalWeight() <= 0.0)
			errorList.addIfNotNull("Estimated fetal weight cannot be zero or less");
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}

}
