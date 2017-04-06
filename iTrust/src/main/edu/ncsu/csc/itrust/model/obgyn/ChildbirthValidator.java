package edu.ncsu.csc.itrust.model.obgyn;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;
import edu.ncsu.csc.itrust.model.ValidationFormat;

public class ChildbirthValidator extends POJOValidator<Childbirth> {

	@Override
	public void validate(Childbirth bean) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		if (bean == null) {
			errorList.addIfNotNull("Bean cannot be null");
			throw new FormValidationException(errorList);
		}
		if (bean.getParentID() < 0)
			errorList.addIfNotNull("Parent ID cannot be negative");
		if (bean.getVisitID() < 0)
			errorList.addIfNotNull("Visit ID cannot be negative");
		if (bean.getBirthdate() == null)
			errorList.addIfNotNull("Birth date cannot be null");
		errorList.addIfNotNull(
				checkFormat("Gender", bean.getGender(), ValidationFormat.GENDERCOD, false));
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);		
	}

}
