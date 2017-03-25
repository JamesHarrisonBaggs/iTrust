package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obgyn.PregnancyBean;
import edu.ncsu.csc.itrust.model.obgyn.PregnancyValidator;

public class PregnancyValidatorTest {
	
	private PregnancyValidator validator;
	private PregnancyBean bean;

	@Before
	public void setUp() {
		validator = new PregnancyValidator();
	}
	
	@Test
	public void testGetInstance() {
		PregnancyValidator val = PregnancyValidator.getInstance();
		assertNotNull(val);
	}

	@Test
	public void testValidBeans() {
		
		// valid bean
		bean = new PregnancyBean();
		bean.setPatientId(10);
		bean.setDateOfBirth(LocalDate.now().minusDays(20));
		bean.setYearOfConception(2016);
		bean.setDaysPregnant(273);
		bean.setHoursInLabor(7);
		bean.setWeightGain(7.2);
		bean.setDeliveryType("Vaginal delivery");
		bean.setAmount(2);		
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testInvalidBeans() {
		
		// default bean
		bean = new PregnancyBean();
		try {
			validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
				
		// test id < 0
		bean = new PregnancyBean();
		bean.setPatientId(-1);
		try {
			validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertTrue(e.getMessage().contains("Patient id cannot be negative"));
		}
				
		// test date == null
		bean = new PregnancyBean();
		bean.setDateOfBirth(null);
		try {
			validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertTrue(e.getMessage().contains("Date of birth cannot be null"));
		}
		
		// not sure how to validate year
		
		// test days pregnant < 0
		bean = new PregnancyBean();
		bean.setDaysPregnant(-5);
		try {
			validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertTrue(e.getMessage().contains("Days pregnant cannot be negative"));
		}
		
		// test hours in labor < 0
		bean = new PregnancyBean();
		bean.setHoursInLabor(-5);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertTrue(e.getMessage().contains("Hours in labor cannot be negative"));
		}
		
		// TODO negative weight gain might be OK
		
		// TODO test invalid delivery type
//		bean = new PregnancyBean();
//		bean.setDeliveryType("meh");
//		try {
//			validate(bean);
//			fail("Validation passed unexpectedly");
//		} catch (FormValidationException e) {
//			assertTrue(e.getMessage().contains("Delivery type is invalid"));
//		}
		
		// test amount < 1
		bean = new PregnancyBean();
		bean.setAmount(0);
		try {
			validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertTrue(e.getMessage().contains("Amount cannot be zero"));
		}
		
		// test amount < 0
		bean = new PregnancyBean();
		bean.setAmount(-2);
		try {
			validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertTrue(e.getMessage().contains("Amount cannot be negative"));
		}
		
	}

	/**
	 * Validates a specified bean. Throws exception if validation fails.
	 * @param bean
	 * @throws FormValidationException
	 */
	private void validate(PregnancyBean bean) throws FormValidationException {
		validator.validate(bean);
	}
	
}