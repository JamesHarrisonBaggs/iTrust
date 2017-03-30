package edu.ncsu.csc.itrust.unit.model.healthtracker;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.when;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.mysql.jdbc.Connection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.healthtracker.HealthTrackerBean;
import edu.ncsu.csc.itrust.model.healthtracker.HealthTrackerMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class HealthTrackerMySQLTest {

	TestDataGenerator gen;
	DataSource ds;
	@Mock
	DataSource mockDS;
	
	HealthTrackerMySQL sql;
	List<HealthTrackerBean> list;
	HealthTrackerBean bean;
	
	@Before
	public void setUp() throws DBException, FileNotFoundException, SQLException, IOException {
		ds = ConverterDAO.getDataSource();
		mockDS = Mockito.mock(DataSource.class);
		sql = new HealthTrackerMySQL(ds);
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}
	
	/**
	 * Tests the constructor fails without a context
	 */
	@Test
	public void testConstructor() throws Exception {
		HealthTrackerMySQL obj = null;
		try {
			obj = new HealthTrackerMySQL();			
		} catch (DBException e) {
			assertNull(obj);
			assertTrue(e.getExtendedMessage().contains("Context Lookup Naming Exception"));
		}
	}
	
	@Test
	public void testUpdateException() throws Exception {
		// Invoke SQLException catch block via mocking
		sql = new HealthTrackerMySQL(mockDS);
		Mockito.doThrow(SQLException.class).when(mockDS).getConnection();
		bean = new HealthTrackerBean(0, LocalDate.of(2016, 3, 24), 5, 5, 5.0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5);

		try {
			sql.updateData(bean);
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
	}
	
	@Test
	public void testGetExceptions() throws Exception {
		// Invoke SQLException catch block via mocking
		sql = new HealthTrackerMySQL(mockDS);
		Connection mockConn = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConn);
		when(mockConn.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			sql.getDataOnDay(1L, Timestamp.valueOf(LocalDateTime.now()));
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
		
		// Invoke SQLException catch block via mocking
		sql = new HealthTrackerMySQL(mockDS);
		mockConn = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConn);
		when(mockConn.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			Timestamp t1 = Timestamp.valueOf(LocalDateTime.now().minusDays(2));
			Timestamp t2 = Timestamp.valueOf(LocalDateTime.now().minusDays(2));
			sql.getDataInRange(1L, t1, t2);
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
		
		// Invoke SQLException catch block via mocking
		sql = new HealthTrackerMySQL(mockDS);
		mockConn = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConn);
		when(mockConn.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			sql.getAll();
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
		
		// Invoke SQLException catch block via mocking
		sql = new HealthTrackerMySQL(mockDS);
		mockConn = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConn);
		when(mockConn.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			sql.getByID(1L);
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
	}
	
	@Test
	public void testGet() throws Exception {
		
		Timestamp oct1 = Timestamp.valueOf(LocalDate.of(2016, 10, 1).atTime(0, 0));
		Timestamp oct2 = Timestamp.valueOf(LocalDate.of(2016, 10, 2).atTime(0, 0));

		list = sql.getAll();
		assertEquals(4, list.size());

		list = sql.getByID(2);
		assertEquals(3, list.size());
		
		list = sql.getDataInRange(2, oct1, oct2);
		assertEquals(2, list.size());
		
		list = sql.getDataOnDay(2, oct1);
		assertEquals(1, list.size());
		
	}
	
	@Test
	public void testAdd() throws Exception {
		
		// create bean
		HealthTrackerBean bean = new HealthTrackerBean();
		bean.setPatientId(2);
		bean.setDate(LocalDate.of(2016, 10, 4));
		bean.setCalories(2010);
		bean.setSteps(11000);
		bean.setDistance(4.42);
		bean.setFloors(3);
		bean.setActivityCalories(250);
		bean.setMinutesSedentary(10);
		bean.setMinutesLightlyActive(7);
		bean.setMinutesFairlyActive(5);
		bean.setMinutesVeryActive(25);
		bean.setActiveHours(5);
		bean.setHeartrateLow(42);
		bean.setHeartrateHigh(106);
		bean.setHeartrateAverage(71);
		bean.setUvExposure(7);
		
		// add to DB
		int result = sql.updateData(bean);
		assertEquals(1, result);
		
		// check is added
		Timestamp ts = Timestamp.valueOf(LocalDate.of(2016, 10, 4).atTime(0, 0));
		list = sql.getDataOnDay(2, ts);
		assertEquals(1, list.size());
		
		// check assertions
		bean = list.get(0);
		assertEquals(250, bean.getActivityCalories());
		assertEquals(4.42, bean.getDistance(), 0.01);
		assertEquals(71, bean.getHeartrateAverage());
		
		
	}

	
}
