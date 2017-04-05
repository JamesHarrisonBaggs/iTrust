package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.when;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsVisit;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsVisitMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ObstetricsVisitMySQLTest {
	
	TestDataGenerator gen;
	DataSource ds;
	@Mock
	DataSource mockDS;
	
	ObstetricsVisitMySQL sql;	
	List<ObstetricsVisit> list;
	ObstetricsVisit bean;
	
	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		mockDS = Mockito.mock(DataSource.class);
		sql = new ObstetricsVisitMySQL(ds);
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}
	
	/**
	 * Tests the constructor fails without a context
	 */
	@Test
	public void testConstructor() throws Exception {
		ObstetricsVisitMySQL ob = null;
		try {
			ob = new ObstetricsVisitMySQL();			
		} catch (DBException e) {
			assertNull(ob);
			assertTrue(e.getExtendedMessage().contains("Context Lookup Naming Exception"));
		}
	}

	@Test
	public void testCreate() throws Exception {

		// create bean
		bean = new ObstetricsVisit();
		bean.setPatientId(2);
		bean.setVisitId(420);
		LocalDateTime now = LocalDateTime.now();
		bean.setVisitDate(now);
		bean.setWeeksPregnant(25);
		bean.setWeight(113.76);
		bean.setBloodPressure("110/70");
		bean.setFetalHeartRate(118);
		bean.setAmount(2);
		bean.setLowLyingPlacenta(true);
		bean.setRhFlag(false);
		sql.update(bean);

		// check result
		list = sql.getByID(2);
		assertEquals(2, list.size());
		
		bean = sql.getByVisit(420);
		assertEquals("110/70", bean.getBloodPressure());
		assertEquals(2, bean.getAmount());
		
		list = sql.getByPatientVisit(2, 420);
		assertEquals(1, list.size());
		
		bean = list.get(0);
		assertEquals(2, bean.getPatientId());
		assertEquals(420, bean.getVisitId());
		assertEquals(now.toLocalDate(), bean.getVisitDate().toLocalDate());
		assertEquals(25, bean.getWeeksPregnant());
		assertEquals(113.76, bean.getWeight(), 0);
		assertEquals("110/70", bean.getBloodPressure());
		assertEquals(118, bean.getFetalHeartRate());
		assertEquals(2, bean.getAmount());
		assertTrue(bean.isLowLyingPlacenta());
		assertFalse(bean.isRhFlag());
		
	}
	
	@Test
	public void testUpdate() throws Exception {
		
		long visitId = -1;
		list = sql.getByID(2);
		for (ObstetricsVisit b : list) {
			if (b.getBloodPressure().equals("120/60") && b.getFetalHeartRate() == 120) {
				visitId = b.getVisitId();
			}
		}
	
		// update bean
		bean = new ObstetricsVisit();
		bean.setPatientId(2);
		bean.setVisitId(visitId);
		LocalDateTime now = LocalDateTime.now();
		bean.setVisitDate(now); // different
		bean.setWeeksPregnant(22); // different
		bean.setWeight(127.9); // different
		bean.setBloodPressure("120/60");
		bean.setFetalHeartRate(120);
		bean.setAmount(1);
		bean.setLowLyingPlacenta(false);
		bean.setRhFlag(true);
	
		// perform update
		sql.update(bean);
		// doesn't check the "proper" value was returned
		
		// check result
		list = sql.getByID(2);
		assertEquals(1, list.size());
		
		bean = list.get(0);
		assertEquals(2, bean.getPatientId());
		assertEquals(visitId, bean.getVisitId());
		assertEquals(now.toLocalDate(), bean.getVisitDate().toLocalDate());
		assertEquals(22, bean.getWeeksPregnant());
		assertEquals(127.9, bean.getWeight(), 0);
		assertEquals("120/60", bean.getBloodPressure());
		assertEquals(120, bean.getFetalHeartRate());
		assertEquals(1, bean.getAmount());
		assertFalse(bean.isLowLyingPlacenta());
		assertTrue(bean.isRhFlag());
		
	}

	@Test
	public void testUpdateException() throws Exception {
		
		// Invoke SQLException catch block via mocking
		sql = new ObstetricsVisitMySQL(mockDS);
		Mockito.doThrow(SQLException.class).when(mockDS).getConnection();
		try {
			sql.update(defaultBean());
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
		
	}
	
	@Test
	public void testGetExceptions() throws Exception {
		
		// Invoke SQLException catch block via mocking
		sql = new ObstetricsVisitMySQL(mockDS);
		Connection mockConn = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConn);
		when(mockConn.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			sql.getByID(1L);
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
		
		// Invoke SQLException catch block via mocking
		sql = new ObstetricsVisitMySQL(mockDS);
		mockConn = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConn);
		when(mockConn.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			sql.getByVisit(1L);
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
		
		// Invoke SQLException catch block via mocking
		sql = new ObstetricsVisitMySQL(mockDS);
		mockConn = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConn);
		when(mockConn.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			sql.getByPatientVisit(2L, 1L);
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
	}
	
	@Test
	public void testGetByNoVisit() throws Exception {
		bean = sql.getByVisit(18L);
		assertEquals("000/000", bean.getBloodPressure());
		assertNull(bean.getVisitDate());
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
