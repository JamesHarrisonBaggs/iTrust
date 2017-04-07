package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInitValidator;

public class ObstetricsInitValidatorTest {

	ObstetricsInitValidator validator;
	ObstetricsInit bean;
	
	@Before
	public void setUp() {
		validator = new ObstetricsInitValidator();
	}

	@Test
	public void testValidBeans() {

		// sample bean
		bean = new ObstetricsInit();
		bean.setPatientId(101);
		bean.setInitDate(LocalDate.of(2016, 3, 24));
		bean.setLastMenstrualPeriod(LocalDate.now().minusDays(9));
		
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testInvalidBeans() {
		
		// default bean
		bean = new ObstetricsInit();
		invalidate(bean, "Patient id cannot be negative");
		
		// null bean
		invalidate(null, "Bean cannot be null");
		
		// test id < 0
		bean = new ObstetricsInit();
		bean.setPatientId(-7);
		invalidate(bean, "Patient id cannot be negative");
		
		// test date == null
		bean = new ObstetricsInit();
		bean.setInitDate(null);
		invalidate(bean, "Initialization date cannot be null");
		
		// test LMP == null
		bean = new ObstetricsInit();
		bean.setLastMenstrualPeriod(null);
		invalidate(bean, "Last menstrual period cannot be null");
		invalidate(bean, "Expected due date cannot be null");
		
	}
	
	/**
	 * Assert that a given bean is invalid
	 */
	private void invalidate(ObstetricsInit bean, String errorMsg) {
		try {
			validator.validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertTrue(e.getMessage().contains(errorMsg));
		}
	}

}
