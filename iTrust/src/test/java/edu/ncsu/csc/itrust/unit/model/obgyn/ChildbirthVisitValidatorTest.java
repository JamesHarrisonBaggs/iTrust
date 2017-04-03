package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obgyn.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.obgyn.ChildbirthVisitValidator;

public class ChildbirthVisitValidatorTest {

	ChildbirthVisitValidator validator;
	ChildbirthVisit bean;
	
	@Before
	public void setUp() throws Exception {
		validator = new ChildbirthVisitValidator();
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
	public void testInvalidBeans() {
		// new bean
		bean = new ChildbirthVisit();
		invalidate(bean, "Patient ID cannot be negative");
		
		// test patient ID < 0
		bean = defaultBean();
		bean.setPatientID(-2);
		invalidate(bean, "Patient ID cannot be negative");		
		
		// test visit ID < 0
		bean = defaultBean();
		bean.setVisitID(-1);
		invalidate(bean, "Visit ID cannot be negative");
		
		// test null visit date
		bean = defaultBean();
		bean.setVisitDate(null);
		invalidate(bean, "Visit date cannot be null");
		
		// test invalid delivery type
		bean = defaultBean();
		bean.setDeliveryType("asd");
		invalidate(bean, "Delivery type is invalid");
		
		// test negative dosage
		bean = defaultBean();
		bean.setPitocin(-1);
		invalidate(bean, "Dosage of pitocin is invalid");
		
		bean = defaultBean();
		bean.setNitrousOxide(-1);
		invalidate(bean, "Dosage of nitrous oxide is invalid");
		
		bean = defaultBean();
		bean.setPethidine(-1);
		invalidate(bean, "Dosage of pethidine is invalid");
		
		bean = defaultBean();
		bean.setEpiduralAnaesthesia(-1);
		invalidate(bean, "Dosage of epidural anaesthesia is invalid");
		
		bean = defaultBean();		
		bean.setMagnesiumSO4(-1);		
		invalidate(bean, "Dosage of magnesium sulfate is invalid");
		
	}
	
	private ChildbirthVisit defaultBean() {
		ChildbirthVisit b = new ChildbirthVisit();
		b.setPatientID(2);
		b.setVisitID(51);
		b.setVisitDate(LocalDate.now());
		b.setPreSchedule(false);
		b.setDeliveryType("vaginal");
		b.setPitocin(0);
		b.setNitrousOxide(0);
		b.setPethidine(0);
		b.setEpiduralAnaesthesia(0);
		b.setMagnesiumSO4(0);
		return b;
	}
	
	private void invalidate(ChildbirthVisit bean, String errorMsg) {
		try {
			validator.validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertTrue(e.getMessage().contains(errorMsg));
		}
	}

}
