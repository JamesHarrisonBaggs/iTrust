package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obgyn.Pregnancy;
import edu.ncsu.csc.itrust.model.obgyn.PregnancyValidator;

public class PregnancyValidatorTest {
	
	private PregnancyValidator validator;
	private Pregnancy bean;

	@Before
	public void setUp() {
		validator = new PregnancyValidator();
	}

	@Test
	public void testValidBeans() {
		
		// default bean
		bean = defaultBean();
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			fail(e.getMessage());
		}
		
		// valid bean
		bean = new Pregnancy();
		bean.setPatientId(10);
		bean.setDateOfBirth(LocalDate.now().minusDays(20));
		bean.setYearOfConception(2016);
		bean.setDaysPregnant(273);
		bean.setHoursInLabor(7);
		bean.setWeightGain(7.2);
		bean.setDeliveryType("vaginal");
		bean.setAmount(2);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			fail(e.getMessage());
		}
				
	}
	
	@Test
	public void testValidDeliveryType() {
		try {
			bean = defaultBean();
			bean.setDeliveryType("vaginal");
			validator.validate(bean);
			bean.setDeliveryType("forceps");
			validator.validate(bean);
			bean.setDeliveryType("vacuum");
			validator.validate(bean);
			bean.setDeliveryType("caesarean");
			validator.validate(bean);
			bean.setDeliveryType("miscarriage");
			validator.validate(bean);
		} catch (FormValidationException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testInvalidBeans() {
		
		// new bean
		bean = new Pregnancy();
		invalidate(bean, "");
				
		// test id < 0
		bean = defaultBean();
		bean.setPatientId(-1);
		invalidate(bean, "Patient id cannot be negative");
				
		// test date == null
		bean = defaultBean();
		bean.setDateOfBirth(null);
		invalidate(bean, "Date of birth cannot be null");
				
		// year of conception > year of birth
		bean = defaultBean();
		bean.setDateOfBirth(LocalDate.of(2010, 3, 24));
		bean.setYearOfConception(2015);
		invalidate(bean, "Year of conception cannot be after year of birth");
		
		// test days pregnant < 0
		bean = defaultBean();
		bean.setDaysPregnant(-5);
		invalidate(bean, "Days pregnant cannot be negative");
		
		// test hours in labor < 0
		bean = defaultBean();
		bean.setHoursInLabor(-5);
		invalidate(bean, "Hours in labor cannot be negative");
		
		// TODO negative weight gain might be OK
		
		bean = defaultBean();
		bean.setDeliveryType("meh");
		try {
			validator.validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertTrue(e.getMessage().contains("Delivery type is invalid"));
		}
		
		// test amount < 1
		bean = defaultBean();
		bean.setAmount(0);
		invalidate(bean, "Amount cannot be zero or less");
		
		// test amount < 0
		bean = defaultBean();
		bean.setAmount(-2);
		invalidate(bean, "Amount cannot be zero or less");
		
	}
	
	/**
	 * Return a default Ultrasound bean
	 */
	private Pregnancy defaultBean() {
		Pregnancy bean = new Pregnancy();
		bean.setPatientId(2);
		bean.setDateOfBirth(LocalDate.of(1996, 3, 24));
		bean.setYearOfConception(1995);
		bean.setDaysPregnant(20);
		bean.setHoursInLabor(7);
		bean.setWeightGain(8.62);
		bean.setDeliveryType("vaginal");
		bean.setAmount(1);
		return bean;
	}
	
	/**
	 * Assert that a given bean is invalid
	 */
	private void invalidate(Pregnancy bean, String errorMsg) {
		try {
			validator.validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertTrue(e.getMessage().contains(errorMsg));
		}
	}
	
}