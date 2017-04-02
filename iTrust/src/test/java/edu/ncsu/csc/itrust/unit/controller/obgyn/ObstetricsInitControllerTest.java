package edu.ncsu.csc.itrust.unit.controller.obgyn;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.ncsu.csc.itrust.controller.obgyn.ObstetricsInitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInit;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ObstetricsInitControllerTest {
	
	ObstetricsInitController controller;
	DataSource ds;
	TestDataGenerator gen;
	
	List<ObstetricsInit> list;
	ObstetricsInit bean;
//	@Mock SessionUtils mockSessionUtils;	

	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		controller = new ObstetricsInitController(ds);
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		
//		mockSessionUtils = Mockito.mock(SessionUtils.class);
//		controller = Mockito.spy(new ObstetricsInitController(ds));
//		Mockito.doNothing().when(controller).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Matchers.anyString(),
//				Matchers.anyString(), Matchers.anyString());
//		Mockito.doNothing().when(controller).logTransaction(null, Mockito.anyString());
	}
	
	/**
	 * Tests the constructor fails without a context
	 */
	@Test
	public void testConstructor() throws Exception {
		ObstetricsInitController ob = null;
		try {
			ob = new ObstetricsInitController();			
		} catch (DBException e) {
			assertNull(ob);
			assertTrue(e.getExtendedMessage().contains("Context Lookup Naming Exception"));
		}
	}

	/**
	 * Tests getting and setting a patient MID for test purposes
	 */
	@Test
	public void testGetSetMid() throws Exception {
		controller.setMid(2L);
		assertEquals(2L, controller.getMid());
	}
	
	/**
	 * Tests getting obstetrics initialization records
	 */
	@Test
	public void testGetInits() throws Exception {
		controller.setMid(4L);
		list = controller.getObstetricsRecords();
		assertEquals(2, list.size());
		assertEquals(list.get(0).getInitDate(), LocalDate.of(2016, 5, 7));
		assertEquals(list.get(1).getInitDate(), LocalDate.of(2014, 5, 1));
		assertEquals(list.get(0).getLastMenstrualPeriod(), LocalDate.of(1990, 1, 1));
		assertEquals(list.get(1).getLastMenstrualPeriod(), LocalDate.of(1990, 1, 1));
	}
	
	/**
	 * Tests getting obstetrics initialization records by date
	 */
	@Test
	public void testGetByDate() throws Exception {
		controller.setMid(4L);
		list = controller.getObstetricsRecordByDate(LocalDate.of(2016, 5, 7));
		assertEquals(1, list.size());
		bean = list.get(0);
		assertEquals(4, bean.getPatientId());
		assertEquals(bean.getInitDate(), LocalDate.of(2016, 5, 7));
		assertEquals(bean.getLastMenstrualPeriod(), LocalDate.of(1990, 1, 1));
	}

	@Test
	public void testUpdate() throws Exception {
		bean = new ObstetricsInit();
		bean.setPatientId(2);
		bean.setInitDate(LocalDate.of(1992, 5, 2));
		bean.setLastMenstrualPeriod(LocalDate.of(1992, 2, 7));
		
		controller.setMid(2L);		
		controller.update(bean);
	}
	
	@Test
	public void testInit() throws Exception {
		// submit new init
		bean = controller.getNewInit();
		controller.setNewInit(new ObstetricsInit());
		controller.getNewInit().setPatientId(3L);
		controller.getNewInit().setLastMenstrualPeriod(LocalDate.of(2013, 8, 11));
		controller.setMid(3L);
		controller.submitInit();

		// get beans from patient
		controller.setMid(3L);
		list = controller.getObstetricsRecords();
		assertEquals(1, list.size());
		bean = list.get(0);
		
		// assert initialize one is present
		assertEquals(3L, bean.getPatientId());
		assertEquals(LocalDate.now(), bean.getInitDate());
		assertEquals(LocalDate.of(2013, 8, 11), bean.getLastMenstrualPeriod());
	}
	
	@Test
	public void testEligibility() throws Exception {
		controller.setMid(4L);
		controller.setEligible(true);
		assertTrue(controller.isEligible());
//		controller.setObGyn();
//		assertTrue(controller.getObGyn());
//		assertTrue(controller.isBothObGynEligible());		
//		controller.submitEligible();
//		assertTrue(controller.isEligible());
//		controller.submitEligible();
//		assertFalse(controller.isEligible());
	}

}
