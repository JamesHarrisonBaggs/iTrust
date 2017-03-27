package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obgyn.Ultrasound;
import edu.ncsu.csc.itrust.model.obgyn.UltrasoundValidator;

public class UltrasoundValidatorTest {

	private UltrasoundValidator validator;
	private Ultrasound bean;
	
	@Before
	public void setUp() {
		validator = new UltrasoundValidator();
	}
	
	@Test
	public void testValid() {
		bean = defaultBean();
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			fail("Validation failed");
		}
	}
	
	@Test
	public void testInvalid() {
		// true default bean
		bean = new Ultrasound();
		invalidate(bean, "This form has not been validated correctly");
		
		// patient id < 0
		bean = defaultBean();
		bean.setPatientId(-1);
		invalidate(bean, "Patient id cannot be negative");
		
		// visit id < 0
		bean = defaultBean();
		bean.setVisitId(-1);
		invalidate(bean, "Office visit id cannot be negative");
		
		// visitDate = null
		bean = defaultBean();
		bean.setVisitDate(null);
		invalidate(bean, "Office visit date cannot be null");
		
		// fetusId < 0
		bean = defaultBean();
		bean.setFetusId(-2);
		invalidate(bean, "Fetus id cannot be negative");
		
		// CRL <= 0
		bean = defaultBean();
		bean.setCrownRumpLength(0);
		invalidate(bean, "Crown rump length cannot be zero or less");
		
		bean.setCrownRumpLength(-4);
		invalidate(bean, "Crown rump length cannot be zero or less");
		
		// BPD <= 0
		bean = defaultBean();
		bean.setBiparietalDiameter(0);
		invalidate(bean, "Biparietal diameter cannot be zero or less");
		
		bean = defaultBean();
		bean.setBiparietalDiameter(-1);
		invalidate(bean, "Biparietal diameter cannot be zero or less");
		
		// HC <= 0
		bean = defaultBean();
		bean.setHeadCircumference(0);
		invalidate(bean, "Head circumference cannot be zero or less");

		bean = defaultBean();
		bean.setHeadCircumference(-3);
		invalidate(bean, "Head circumference cannot be zero or less");
		
		// FL <= 0
		bean = defaultBean();
		bean.setFemurLength(0);
		invalidate(bean, "Femur length cannot be zero or less");

		bean = defaultBean();
		bean.setFemurLength(-2);
		invalidate(bean, "Femur length cannot be zero or less");
		
		// OFD <= 0
		bean = defaultBean();
		bean.setOccipitofrontalDiameter(0);
		invalidate(bean, "Occipitofrontal diameter cannot be zero or less");
		
		bean = defaultBean();
		bean.setOccipitofrontalDiameter(-1);
		invalidate(bean, "Occipitofrontal diameter cannot be zero or less");
		
		// AC <= 0
		bean = defaultBean();
		bean.setAbdominalCircumference(0);
		invalidate(bean, "Abdominal circumference cannot be zero or less");
		
		bean = defaultBean();
		bean.setAbdominalCircumference(-2);
		invalidate(bean, "Abdominal circumference cannot be zero or less");

		// HL <= 0
		bean = defaultBean();
		bean.setHumerusLength(0);
		invalidate(bean, "Humerus length cannot be zero or less");

		bean = defaultBean();
		bean.setHumerusLength(-7);
		invalidate(bean, "Humerus length cannot be zero or less");
		
		// EFW <= 0
		bean = defaultBean();
		bean.setEstimatedFetalWeight(0.0);
		invalidate(bean, "Estimated fetal weight cannot be zero or less");

		bean = defaultBean();
		bean.setEstimatedFetalWeight(-180.2);
		invalidate(bean, "Estimated fetal weight cannot be zero or less");
		
	}
	
	/**
	 * Assert that a given bean is invalid
	 */
	private void invalidate(Ultrasound bean, String errorMsg) {
		try {
			validator.validate(bean);
			fail("Validation passed unexpectedly");
		} catch (FormValidationException e) {
			assertTrue(e.getMessage().contains(errorMsg));
		}
	}
	
	/**
	 * Return a default Ultrasound bean
	 */
	private Ultrasound defaultBean() {
		Ultrasound bean = new Ultrasound();
		bean.setPatientId(0);
		bean.setVisitId(1);
		bean.setVisitDate(LocalDateTime.now());
		bean.setFetusId(1);
		bean.setCrownRumpLength(80); 			// ???
		bean.setBiparietalDiameter(40); 		// 40mm is typical for 18 weeks
		bean.setHeadCircumference(130); 		// 130mm is typical for 17 weeks
		bean.setFemurLength(25); 				// 25mm is typical for 17 weeks
		bean.setOccipitofrontalDiameter(40); 	// ???
		bean.setAbdominalCircumference(110); 	// 110mm is typical for 17 weeks
		bean.setHumerusLength(30); 				// ???
		bean.setEstimatedFetalWeight(201.55); 	// 201.55 grams for BPD/AC
		return bean;
	}

}
