package edu.ncsu.csc.itrust.unit.controller.obgyn;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import edu.ncsu.csc.itrust.controller.obgyn.ObstetricsInitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInit;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

public class ObstetricsInitControllerTest {
	
	ObstetricsInitController controller;
	DataSource ds;
	TestDataGenerator gen;
	
	List<ObstetricsInit> list;
	ObstetricsInit bean;
	
	@Mock SessionUtils mockUtils;	
	DAOFactory factory;
	
	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		factory = TestDAOFactory.getTestInstance();
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		
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
	 * Tests getting obstetrics initialization records
	 */
	@Test
	public void testGetInits() throws Exception {
		
		mockUtils = Mockito.mock(SessionUtils.class);
		Mockito.doReturn(4L).when(mockUtils).getCurrentPatientMIDLong();
		controller = new ObstetricsInitController(ds, mockUtils, factory);
		
		list = controller.getObstetricsRecords();
		assertEquals(2, list.size());
		assertEquals(list.get(0).getInitDate(), LocalDate.of(2016, 5, 7));
		assertEquals(list.get(1).getInitDate(), LocalDate.of(2014, 5, 1));
		assertEquals(list.get(0).getLastMenstrualPeriod(), LocalDate.of(1990, 1, 1));
		assertEquals(list.get(1).getLastMenstrualPeriod(), LocalDate.of(1990, 1, 1));
		
		bean = controller.getCurrentInitialization();
		assertEquals(bean.getInitDate(), LocalDate.of(2016, 5, 7));
		assertEquals(bean.getLastMenstrualPeriod(), LocalDate.of(1990, 1, 1));	
	}
	
	/**
	 * Tests getting obstetrics initialization records by date
	 */
	@Test
	public void testGetByDate() throws Exception {
		
		mockUtils = Mockito.mock(SessionUtils.class);
		Mockito.doReturn(4L).when(mockUtils).getCurrentPatientMIDLong();
		Mockito.doReturn(50L).when(mockUtils).getSessionLoggedInMIDLong();
		controller = new ObstetricsInitController(ds, mockUtils, factory);

		list = controller.getObstetricsRecordByDate(LocalDate.of(2016, 5, 7));
		assertEquals(1, list.size());
		bean = list.get(0);
		assertEquals(4, bean.getPatientId());
		assertEquals(bean.getInitDate(), LocalDate.of(2016, 5, 7));
		assertEquals(bean.getLastMenstrualPeriod(), LocalDate.of(1990, 1, 1));
	}
	
	@Test
	public void testMisc() throws Exception {
		mockUtils = Mockito.mock(SessionUtils.class);
		Mockito.doReturn(2L).when(mockUtils).getCurrentPatientMIDLong();
		Mockito.doReturn(50L).when(mockUtils).getSessionLoggedInMIDLong();
		controller = new ObstetricsInitController(ds, mockUtils, factory);

		controller.setAdded(true);
		assertTrue(controller.isAdded());
		controller.getObGyn();
		controller.isBothObGynEligible();
	}

	@Test
	public void testUpdate() throws Exception {
		bean = new ObstetricsInit();
		bean.setPatientId(2);
		bean.setInitDate(LocalDate.of(1992, 5, 2));
		bean.setLastMenstrualPeriod(LocalDate.of(1992, 2, 7));
		
		mockUtils = Mockito.mock(SessionUtils.class);
		Mockito.doReturn(2L).when(mockUtils).getCurrentPatientMIDLong();
		Mockito.doReturn(50L).when(mockUtils).getSessionLoggedInMIDLong();
		controller = new ObstetricsInitController(ds, mockUtils, factory);
		controller.update(bean);
		
		// test form validation exception
		bean = new ObstetricsInit();
		controller.update(bean);
	}
	
	@Test
	public void testInit() throws Exception {
		
		// new controller
		mockUtils = Mockito.mock(SessionUtils.class);
		Mockito.doReturn(3L).when(mockUtils).getCurrentPatientMIDLong();
		Mockito.doReturn(50L).when(mockUtils).getSessionLoggedInMIDLong();
		controller = new ObstetricsInitController(ds, mockUtils, factory);
		controller.setNewInit(null);
		
		// submit new init
		bean = controller.getNewInit();
		controller.setNewInit(new ObstetricsInit());
		controller.getNewInit().setPatientId(3L);
		controller.getNewInit().setLastMenstrualPeriod(LocalDate.of(2013, 8, 11));
		controller.submitInit();

		// get beans from patient
		list = controller.getInitializations(3L);
		assertEquals(1, list.size());
		bean = list.get(0);
		
		// assert initialize one is present
		assertEquals(3L, bean.getPatientId());
		assertEquals(LocalDate.now(), bean.getInitDate());
		assertEquals(LocalDate.of(2013, 8, 11), bean.getLastMenstrualPeriod());
	}
	
	@Test
	public void testEligibility() throws Exception {
		
		mockUtils = Mockito.mock(SessionUtils.class);
		Mockito.doReturn(4L).when(mockUtils).getCurrentPatientMIDLong();
		Mockito.doReturn(50L).when(mockUtils).getSessionLoggedInMIDLong();
		controller = new ObstetricsInitController(ds, mockUtils, factory);
		
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
