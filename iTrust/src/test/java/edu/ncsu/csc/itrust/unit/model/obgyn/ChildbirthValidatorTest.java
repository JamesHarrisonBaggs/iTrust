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
	public void testValidBeans() {
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
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			fail("Validation failed unexpectedly");
		}

		bean = defaultBean();
		bean.setGender("Female");
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			fail("Validation failed unexpectedly");
		}

		bean = defaultBean();
		bean.setGender("Not Specified");
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			fail("Validation failed unexpectedly");
		}
		
	}
	
	@Test
	public void testInvalidBeans() {
		// new bean
		bean = new Childbirth();
		invalidate(bean, "Patient ID cannot be negative");
		
		// test parent ID < 0
		bean = defaultBean();
		bean.setParentID(-1);
		invalidate(bean, "Patient ID cannot be negative");
		
		// test visit ID < 0
		bean = defaultBean();
		bean.setVisitID(-1);
		
		// test birth ID < 0
		bean = defaultBean();
		bean.setBirthID(-1);
		
		// test null birth date
		bean = defaultBean();
		bean.setBirthdate(null);
		
		// test invalid gender
		bean = defaultBean();
		bean.setGender("Attack Helicopter");
		
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
	
	private void invalidate(Childbirth bean, String errorMsg) {
		try {
			validator.validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertTrue(e.getMessage().contains(errorMsg));
		}	
	}

}
