package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsVisit;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsVisitValidator;

public class ObstetricsVisitValidatorTest {

	private ObstetricsVisitValidator validator;
	private ObstetricsVisit bean;
	
	@Before
	public void setUp() {
		validator = new ObstetricsVisitValidator();
	}
	
	/**
	 * Tests valid beans
	 * @throws Exception
	 */
	@Test
	public void testValid() throws Exception {
		bean = defaultBean();
		assertFalse(bean.isLowLyingPlacenta());
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			fail("Validation failed");
		}
	}
	
	/**
	 * Tests invalid beans
	 * @throws Exception
	 */
	@Test
	public void testInvalid() throws Exception {
		
		// null bean
		invalidate(null, "Bean cannot be null");
		
		// patient id < 0
		bean = defaultBean();
		bean.setPatientId(-1);
		invalidate(bean, "Patient ID cannot be negative");
		
		// visit id < 0
		bean = defaultBean();
		bean.setVisitId(-1);
		invalidate(bean, "Office visit ID cannot be negative");
		
		// visitDate = null
		bean = defaultBean();
		bean.setVisitDate(null);
		invalidate(bean, "Office visit date cannot be null");
		
		// weeksPregnant < 0
		bean = defaultBean();
		bean.setWeeksPregnant(-1);
		invalidate(bean, "Weeks pregnant cannot be negative");
		
		// weight = 0
		bean = defaultBean();
		bean.setWeight(0.0);
		invalidate(bean, "Weight cannot be zero or less");
		
		// weight < 0
		bean = defaultBean();
		bean.setWeight(-100.82);
		invalidate(bean, "Weight cannot be zero or less");
		
		// fetal heart rate < 0
		bean = defaultBean();
		bean.setFetalHeartRate(-1);
		invalidate(bean, "Fetal heart rate cannot be negative");
		
		// amount < 1
		bean = defaultBean();
		bean.setAmount(0);
		invalidate(bean, "Multiples cannot be less than 1");
		
		// amount < 0
		bean = defaultBean();
		bean.setAmount(-1);
		invalidate(bean, "Multiples cannot be less than 1");
		
	}
	
	/**
	 * Test invalid values for blood pressure
	 * @throws Exception
	 */
	@Test
	public void testBloodPressure() throws Exception {
		String errorMsg = "Blood Pressure: Up to 3-digit number / Up to 3-digit number";
		
		// blood pressure string
		bean = defaultBean();
		bean.setBloodPressure("asd");
		invalidate(bean, errorMsg);
	
		// blood pressure int
		bean = defaultBean();
		bean.setBloodPressure("4");
		invalidate(bean, errorMsg);
		
		// blood pressure large
		bean = defaultBean();
		bean.setBloodPressure("1000/1000");
		invalidate(bean, errorMsg);
		
		// blood pressure negative
		bean = defaultBean();
		bean.setBloodPressure("-1/-2");
		invalidate(bean, errorMsg);
	
	}

	/**
	 * @return a default ObstetricsVisit bean
	 */
	private ObstetricsVisit defaultBean() {
		ObstetricsVisit bean = new ObstetricsVisit();
		bean.setPatientId(1);
		bean.setVisitId(1);
		bean.setVisitDate(LocalDateTime.now());		
		bean.setBloodPressure("120/60"); // normal is (90-120)/(60-80)
		bean.setWeeksPregnant(20);
		bean.setWeight(125.7);
		bean.setFetalHeartRate(120);
		bean.setAmount(1);
		bean.setLowLyingPlacenta(false);
		return bean;
	}
	
	private void invalidate(ObstetricsVisit bean, String errorMsg) {
		try {
			validator.validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertTrue(e.getMessage().contains(errorMsg));
		}
	}
	
}
