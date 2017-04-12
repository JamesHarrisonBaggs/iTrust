package edu.ncsu.csc.itrust.unit.controller.obgyn;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import edu.ncsu.csc.itrust.controller.obgyn.ObstetricsVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsVisit;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsVisitMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

public class ObstetricsVisitControllerTest {

	ObstetricsVisitController controller;
	DataSource ds;
	TestDataGenerator gen;
	
	@Mock SessionUtils mockSessionUtils;
	
	ObstetricsVisitMySQL sql;
	List<ObstetricsVisit> list;
	ObstetricsVisit bean;
	
	@Before
	public void setUp() throws Exception {
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		Mockito.doReturn(51L).when(mockSessionUtils).getCurrentOfficeVisitId();
		Mockito.doReturn(2L).when(mockSessionUtils).getCurrentPatientMIDLong();
		
		ds = ConverterDAO.getDataSource();
		controller = new ObstetricsVisitController(ds, mockSessionUtils, TestDAOFactory.getTestInstance());
		sql = controller.getSql();
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();	
	}
	
	/**
	 * Tests the constructor fails without a context
	 */
	@Test
	public void testConstructor() throws Exception {
		ObstetricsVisitController c = null;
		try {
			c = new ObstetricsVisitController();
		} catch (DBException e) {
			assertNull(c);
			assertTrue(e.getExtendedMessage().contains("Context Lookup Naming Exception"));
		}
	}
	
	@Test
	public void testUpdate() throws Exception {
		// normal update
		controller.update(defaultBean());
		
		// form validation exception
		controller.update(new ObstetricsVisit());
	}
	
	@Test
	public void testDiabolicals() throws Exception {
		sql = spy(new ObstetricsVisitMySQL(ds));
		controller.setSql(sql);
		bean = defaultBean();
		
		when(sql.update(bean)).thenThrow(new NullPointerException());
		controller.update(bean);
		when(sql.getByVisit(Mockito.anyLong())).thenThrow(new NullPointerException());
		controller.getObstetricsVisit();
		
	}
	
	@Test
	public void testMisc() throws Exception {
		
//		controller.autoScheduleAppt();
		
		controller.getOfficeVisit();
		
		controller.setPatientId(2L);
		controller.setVisitDate(LocalDateTime.now());
		controller.setVisitId(51L);
		controller.submit();
		
//		controller.getWeeksPregnant();
		
		// booleans
		controller.isEligible();
		controller.isObgyn();
		controller.isAutoSchedule();
		
		controller.setAmount(0);
		assertEquals(0, controller.getAmount());
		controller.setAutoSchedule(false);
		controller.setBloodPressure("100/60");
		assertEquals("100/60", controller.getBloodPressure());
//		ovc.setEligible();
		controller.setFetalHeartRate(80);
		assertEquals(80, controller.getFetalHeartRate());
		controller.setLowLyingPlacenta(true);
		assertTrue(controller.isLowLyingPlacenta());
		controller.setNotice();
//		ovc.setObgyn();
//		ovc.setObstetricsList();
		controller.setPatientId(2);
		assertEquals(2, controller.getPatientId());
		controller.setRhFlag(true);
		assertTrue(controller.isRhFlag());
		controller.setSessionUtils(null);
		controller.setTransactionLogger(null);
		controller.setVisitDate(LocalDate.of(2015, 2, 28).atTime(0, 0));
		assertEquals(LocalDate.of(2015, 2, 28), controller.getVisitDate().toLocalDate());
		controller.setVisitId(2);
		assertEquals(2, controller.getVisitId());
		controller.setWeight(2.2);
		assertEquals(2.2, controller.getWeight(), 0.01);
	}
	
	/**
	 * Returns a default ObstetricsVisit bean
	 */
	private ObstetricsVisit defaultBean() {
		ObstetricsVisit bean = new ObstetricsVisit();
		bean.setPatientId(1);
		bean.setVisitId(1);
		bean.setVisitDate(LocalDateTime.now());		
		bean.setBloodPressure("120/60");
		bean.setWeeksPregnant(20);
		bean.setWeight(125.7);
		bean.setFetalHeartRate(120);
		bean.setAmount(1);
		bean.setLowLyingPlacenta(false);
		return bean;
	}

}
