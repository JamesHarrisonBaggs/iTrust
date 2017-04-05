package edu.ncsu.csc.itrust.model.obgyn;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;
import edu.ncsu.csc.itrust.model.ValidationFormat;

public class ChildbirthVisitValidator extends POJOValidator<ChildbirthVisit> {

	@Override
	public void validate(ChildbirthVisit bean) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		if (bean == null) {
			errorList.addIfNotNull("Bean cannot be null");
			throw new FormValidationException(errorList);
		}
		if (bean.getPatientID() < 0)
			errorList.addIfNotNull("Patient ID cannot be negative");
		if (bean.getVisitID() < 0)
			errorList.addIfNotNull("Visit ID cannot be negative");
		if (bean.getVisitDate() == null)
			errorList.addIfNotNull("Visit date cannot be null");
		errorList.addIfNotNull(
				checkFormat("Delivery Type", bean.getDeliveryType(), ValidationFormat.DELIVERY_TYPE, false));
		if (bean.getPitocin() < 0)
			errorList.addIfNotNull("Dosage of pitocin cannot be negative");
		if (bean.getNitrousOxide() < 0)
			errorList.addIfNotNull("Dosage of nitrous oxide cannot be negative");
		if (bean.getPethidine() < 0)
			errorList.addIfNotNull("Dosage of pethidine cannot be negative");
		if (bean.getEpiduralAnaesthesia() < 0)
			errorList.addIfNotNull("Dosage of epidural anaesthesia cannot be negative");
		if (bean.getMagnesiumSO4() < 0)
			errorList.addIfNotNull("Dosage of magnesium sulfate cannot be negative");
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}

}
