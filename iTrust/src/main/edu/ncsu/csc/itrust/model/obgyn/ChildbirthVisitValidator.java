package edu.ncsu.csc.itrust.model.obgyn;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;

public class ChildbirthVisitValidator extends POJOValidator<ChildbirthVisit> {

	@Override
	public void validate(ChildbirthVisit bean) throws FormValidationException {
		// TODO Auto-generated method stub
//		errorList.addIfNotNull(
//				checkFormat("Gender", bean.getGender(), ValidationFormat.GENDERCOD, false))
	}

}
