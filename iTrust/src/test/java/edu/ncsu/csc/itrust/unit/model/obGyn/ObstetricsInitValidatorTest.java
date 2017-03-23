package edu.ncsu.csc.itrust.unit.model.obGyn;

import static org.junit.Assert.*;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obGyn.ObstetricsInitBean;
import edu.ncsu.csc.itrust.model.obGyn.ObstetricsInitValidator;
import edu.ncsu.csc.itrust.model.obGyn.PregnancyBean;

public class ObstetricsInitValidatorTest {

	ObstetricsInitValidator validator;
	ObstetricsInitBean bean;
	
	@Before
	public void setUp() {
		validator = new ObstetricsInitValidator();
	}
	
	@Test
	public void testGetInstance() {
		ObstetricsInitValidator val = ObstetricsInitValidator.getInstance();
		assertNotNull(val);
	}

	@Test
	public void testValidBeans() {

		// sample bean
		bean = new ObstetricsInitBean();
		bean.setPatientId(101);
		bean.setInitDate(LocalDate.of(2016, 3, 24));
		bean.setLastMenstrualPeriod(LocalDate.now().minusDays(9));
		bean.setCurrent(true);
		
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testInvalidBeans() {
		
		// default bean
		bean = new ObstetricsInitBean();
		try {
			validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// test id < 0
		bean = new ObstetricsInitBean();
		bean.setPatientId(-7);
		try {
			validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertTrue(e.getMessage().contains("Patient id cannot be negative"));
		}
		
		// test date == null
		bean = new ObstetricsInitBean();
		bean.setInitDate(null);
		try {
			validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertTrue(e.getMessage().contains("Initialization date cannot be null"));
		}
		
		// test LMP == null
		bean = new ObstetricsInitBean();
		bean.setLastMenstrualPeriod(null);
		try {
			validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertTrue(e.getMessage().contains("Last menstrual period cannot be null"));
		}
		
		// test EDD == null
		bean = new ObstetricsInitBean();
		bean.setEstimatedDueDate(null);
		try {
			validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertTrue(e.getMessage().contains("Expected due date cannot be null"));
		}
		
		// test days pregnant < 0
		bean = new ObstetricsInitBean();
		bean.setDaysPregnant(-5);
		try {
			validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertTrue(e.getMessage().contains("Days pregnant cannot be negative"));
		}
		
	}
	
	/**
	 * Validates a specified bean. Throws exception if validation fails.
	 * @param bean
	 * @throws FormValidationException
	 */
	private void validate(ObstetricsInitBean bean) throws FormValidationException {
		validator.validate(bean);
	}
	
}
