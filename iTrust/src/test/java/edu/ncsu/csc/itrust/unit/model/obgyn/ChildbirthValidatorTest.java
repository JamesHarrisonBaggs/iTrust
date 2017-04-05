package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obgyn.Childbirth;
import edu.ncsu.csc.itrust.model.obgyn.ChildbirthValidator;

public class ChildbirthValidatorTest {

	ChildbirthValidator validator;
	Childbirth bean;
	
	@Before
	public void setUp() throws Exception {
		validator = new ChildbirthValidator();
	}

	@Test
	public void testDefault() {
		// default bean
		bean = defaultBean();
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			fail("Validation failed unexpectedly");
		}
	}
	
	@Test
	public void testGenders() {
		// test valid genders
		bean = defaultBean();
		bean.setGender("Male");
		validate(bean);

		bean = defaultBean();
		bean.setGender("Female");
		validate(bean);

		bean = defaultBean();
		bean.setGender("Not Specified");
		validate(bean);
		
		// test converted genders
		bean = defaultBean();
		bean.setGender("M");
		validate(bean);
		
		bean = defaultBean();
		bean.setGender("male");
		validate(bean);
		
		bean = defaultBean();
		bean.setGender("F");
		validate(bean);
		
		bean = defaultBean();
		bean.setGender("female");
		validate(bean);
		
		// test invalid genders
		bean = defaultBean();
		bean.setGender("Attack Helicopter");
		invalidate(bean, "Gender: Only Male, Female, or All Patients");

		bean = defaultBean();
		bean.setGender("Other");
		invalidate(bean, "Gender: Only Male, Female, or All Patients");

	}
	
	@Test
	public void testEstimated() {
		bean = defaultBean();
		bean.setEstimated(true);
		assertTrue(bean.isEstimated());
		
		bean = defaultBean();
		bean.setEstimated(false);
		assertFalse(bean.isEstimated());
	}
	
	@Test
	public void testInvalid() {
		
		// new bean
		bean = new Childbirth();
		invalidate(bean, "Parent ID cannot be negative");
		
		// test parent ID < 0
		bean = defaultBean();
		bean.setParentID(-1);
		invalidate(bean, "Parent ID cannot be negative");
		
		// test visit ID < 0
		bean = defaultBean();
		bean.setVisitID(-1);
		invalidate(bean, "Visit ID cannot be negative");
		
		// test birth ID < 0
		// actually this is fine because it doesn't matter
		// SQL ignores the birthID here and generates its own
		
		// test null birth date
		bean = defaultBean();
		bean.setBirthdate(null);
		invalidate(bean, "Birth date cannot be null");
				
	}
	
	/**
	 * @return a default childbirth
	 */
	private Childbirth defaultBean() {
		Childbirth b = new Childbirth();
		b.setParentID(2);
		b.setVisitID(51);
		b.setBirthID(1);
		b.setBirthdate(LocalDateTime.now());
		b.setEstimated(false);
		b.setGender("Male");
		return b;
	}
	
	private void validate(Childbirth bean) {
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			fail("Validation failed unexpectedly");
		}
	}
	
	private void invalidate(Childbirth bean, String errorMsg) {
		try {
			validator.validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertTrue(e.getMessage().contains(errorMsg));
		}	
	}

}
