package edu.ncsu.csc.itrust.unit.model.healthtracker;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.healthtracker.HealthTrackerBean;
import edu.ncsu.csc.itrust.model.healthtracker.HealthTrackerValidator;
import junit.framework.Assert;

public class HealthTrackerValidatorTest {

	HealthTrackerValidator validator;
	LocalDate date = LocalDate.of(2011, 11, 20);
	
	@Before
	public void setUp() {
		validator = new HealthTrackerValidator();
	}
	
	@Test
	public void testGetInstance() {
		HealthTrackerValidator hv = HealthTrackerValidator.getInstance();
		assertNotNull(hv);
	}
	
	@Test
	public void testValidate() {
		// tests valid bean
		HealthTrackerBean bean = new HealthTrackerBean(0, date, 5, 5, 5.0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			fail();
		}
		
		// tests invalid date
		bean = new HealthTrackerBean(0, null, 5, 5, 5.0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// tests invalid patient id
		bean = new HealthTrackerBean(-1, date, 5, 5, 5.0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// tests invalid calories
		bean = new HealthTrackerBean(0, date, -5, 5, 5.0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// tests invalid steps
		bean = new HealthTrackerBean(0, date, 5, -5, 5.0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// tests invalid distance
		bean = new HealthTrackerBean(0, date, 5, 5, -5.0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// tests invalid floors
		bean = new HealthTrackerBean(0, date, 5, 5, 5.0, -5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// tests invalid activity calories
		bean = new HealthTrackerBean(0, date, 5, 5, 5.0, 5, -5, 5, 5, 5, 5, 5, 5, 5, 5, 5);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// tests invalid minutes s
		bean = new HealthTrackerBean(0, date, 5, 5, 5.0, 5, 5, -5, 5, 5, 5, 5, 5, 5, 5, 5);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// tests invalid minutes l
		bean = new HealthTrackerBean(0, date, 5, 5, 5.0, 5, 5, 5, -5, 5, 5, 5, 5, 5, 5, 5);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// tests invalid minutes f
		bean = new HealthTrackerBean(0, date, 5, 5, 5.0, 5, 5, 5, 5, -5, 5, 5, 5, 5, 5, 5);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// tests invalid minutes v
		bean = new HealthTrackerBean(0, date, 5, 5, 5.0, 5, 5, 5, 5, 5, -5, 5, 5, 5, 5, 5);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// tests invalid active hours
		bean = new HealthTrackerBean(0, date, 5, 5, 5.0, 5, 5, 5, 5, 5, 5, -5, 5, 5, 5, 5);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// tests invalid heartrate low
		bean = new HealthTrackerBean(0, date, 5, 5, 5.0, 5, 5, 5, 5, 5, 5, 5, -5, 5, 5, 5);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// tests invalid heartrate high
		bean = new HealthTrackerBean(0, date, 5, 5, 5.0, 5, 5, 5, 5, 5, 5, 5, 5, -5, 5, 5);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// tests invalid heartrate aveage
		bean = new HealthTrackerBean(0, date, 5, 5, 5.0, 5, 5, 5, 5, 5, 5, 5, 5, 5, -5, 5);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
		// tests invalid UV exposure
		bean = new HealthTrackerBean(0, date, 5, 5, 5.0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, -5);
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			assertNotNull(e.getMessage());
		}
		
	}

}
