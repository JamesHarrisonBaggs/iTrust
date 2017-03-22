package edu.ncsu.csc.itrust.unit.model.obGyn;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obGyn.PregnancyBean;
import edu.ncsu.csc.itrust.model.obGyn.PregnancyValidator;

public class PregnancyValidatorTest {
	
	PregnancyValidator validator;
	PregnancyBean bean;

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

		LocalDate init = LocalDate.of(2016, 3, 24);
		LocalDate lmp = LocalDate.now().minusDays(9);
		
		bean = new PregnancyBean();
		bean.setPatientId(101);
//		bean.setInitDate(init);
//		bean.setLastMenstrualPeriod(lmp);
//		bean.setCurrent(true);
		
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			fail(e.getMessage());
		}
		
	}
	
	@Test
	public void invalidYear() {
		LocalDate d = LocalDate.of(-47, 3, 2);
		System.out.println(d);
	}
	
	@Test
	public void testInvalidBeans() {
		
		bean = new PregnancyBean();
		// set stuff
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// test negative id
		
		// test null date
		
		// test invalid year
		
		// 
		
	}
	
}