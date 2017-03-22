package edu.ncsu.csc.itrust.unit.model.obGyn;

import static org.junit.Assert.*;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obGyn.ObstetricsInitBean;
import edu.ncsu.csc.itrust.model.obGyn.ObstetricsInitValidator;

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

		LocalDate init = LocalDate.of(2016, 3, 24);
		LocalDate lmp = LocalDate.now().minusDays(9);
		
		bean = new ObstetricsInitBean();
		bean.setPatientId(101);
		bean.setInitDate(init);
		bean.setLastMenstrualPeriod(lmp);
		bean.setCurrent(true);
		
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testInvalidBeans() {
		
		// test id
		bean = new ObstetricsInitBean();
		bean.setPatientId(-7);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// test null init date
		bean = new ObstetricsInitBean();
		bean.setInitDate(null);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// test null LMP
		bean = new ObstetricsInitBean();
		bean.setLastMenstrualPeriod(null);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// test null EDD
		bean = new ObstetricsInitBean();
		bean.setEstimatedDueDate(null);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// test negative DP
		bean = new ObstetricsInitBean();
		bean.setDaysPregnant(-5);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
	}
	
}
