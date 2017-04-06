package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
	public void testDefault() {
		// default bean		
		bean = defaultBean();
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			fail("Validation failed unexpectedly");
		}		
	}
	
	@Test
	public void testGetDosages() {
		bean = defaultBean();
		bean.setMagnesiumSO4(5);
		bean.setPitocin(1);
		bean.setNitrousOxide(2);
		bean.setEpiduralAnaesthesia(4);
		bean.setPethidine(3);
		int exp[] = { 1, 2, 3, 4, 5 };
		int act[] = bean.getDosages();
		assertArrayEquals(exp, act);	
	}
	
	@Test
	public void testSetDosages() {
		// set dosages
		bean = defaultBean();
		int dosages1[] = {5, 6, 7, 8, 9};
		bean.setDosages(dosages1);
		assertEquals(5, bean.getPitocin());
		assertEquals(6, bean.getNitrousOxide());
		assertEquals(7, bean.getPethidine());
		assertEquals(8, bean.getEpiduralAnaesthesia());
		assertEquals(9, bean.getMagnesiumSO4());
		
		// set invalid dosages
		bean = defaultBean();
		int dosages2[] = {1, 2, 3};
		bean.setDosages(dosages2);
		int exp[] = {0, 0, 0, 0, 0};
		assertArrayEquals(exp, bean.getDosages());
	}
	
	@Test
	public void testEstimated() {
		bean = defaultBean();
		bean.setPreSchedule(true);
		assertTrue(bean.isPreSchedule());
		
		bean = defaultBean();
		bean.setPreSchedule(false);
		assertFalse(bean.isPreSchedule());
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
		bean = new ChildbirthVisit();
		invalidate(bean, "Patient ID cannot be negative");
		
		// null bean
		invalidate(null, "Bean cannot be null");
		
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
		invalidate(bean, "Delivery Type: must be one of {vaginal, vacuum, forceps, caesarean, miscarriage}");
		
		// test negative dosage
		bean = defaultBean();
		bean.setPitocin(-1);
		invalidate(bean, "Dosage of pitocin cannot be negative");
		
		bean = defaultBean();
		bean.setNitrousOxide(-1);
		invalidate(bean, "Dosage of nitrous oxide cannot be negative");
		
		bean = defaultBean();
		bean.setPethidine(-1);
		invalidate(bean, "Dosage of pethidine cannot be negative");
		
		bean = defaultBean();
		bean.setEpiduralAnaesthesia(-1);
		invalidate(bean, "Dosage of epidural anaesthesia cannot be negative");
		
		bean = defaultBean();		
		bean.setMagnesiumSO4(-1);		
		invalidate(bean, "Dosage of magnesium sulfate cannot be negative");
		
	}
	
	private ChildbirthVisit defaultBean() {
		ChildbirthVisit b = new ChildbirthVisit();
		b.setPatientID(2);
		b.setVisitID(51);
		b.setVisitDate(LocalDateTime.now());
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
