package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Mockito.when;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.Childbirth;
import edu.ncsu.csc.itrust.model.obgyn.ChildbirthMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ChildbirthMySQLTest {

	TestDataGenerator gen;
	DataSource ds;
	@Mock
	DataSource mockDS;
	
	ChildbirthMySQL sql;
	List<Childbirth> list;
	Childbirth bean;
	
	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		mockDS = Mockito.mock(DataSource.class);
		sql = new ChildbirthMySQL(ds);
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}
	
	/**
	 * Tests the constructor fails without a context
	 */
	@Test
	public void testConstructor() throws Exception {
		ChildbirthMySQL ob = null;
		try {
			ob = new ChildbirthMySQL();			
		} catch (DBException e) {
			assertNull(ob);
			assertTrue(e.getExtendedMessage().contains("Context Lookup Naming Exception"));
		}
	}

	/**
	 * Tests creating a Childbirth
	 */
	@Test
	public void testCreate() throws Exception {
		// starting assumption
		list = sql.getByParentID(2L);
		assertEquals(1, list.size());
		LocalDateTime date = LocalDateTime.now();
		
		// new bean
		bean = updateBean(1090L, -1, date);
		bean.setAdded(true);
		sql.update(bean);
		list = sql.getByParentID(2L);
		assertEquals(2, list.size());

		// check values
		bean = list.get(1);
		assertEquals(2L, bean.getParentID());
		assertEquals(1090L, bean.getVisitID());
		assertDateTimeEquals(date, bean.getBirthdate());
		assertEquals("Male", bean.getGender());
		assertFalse(bean.isEstimated());
		assertTrue(bean.isAdded());
	}
	
	@Test
	public void testUpdate() throws Exception {
		// starting assumption
		list = sql.getByParentID(2L);
		assertEquals(1, list.size());
		long visitID = list.get(0).getVisitID();
		int birthID = list.get(0).getBirthID();
		LocalDateTime date = LocalDateTime.now();
		
		// update and check
		bean = updateBean(visitID, birthID, date);
		sql.update(bean);
		list = sql.getByVisitID(visitID);
		assertEquals(1, list.size());
		
		// check values
		bean = list.get(0);
		assertEquals(2L, bean.getParentID());
		assertEquals(visitID, bean.getVisitID());
		assertDateTimeEquals(date, bean.getBirthdate());
		assertEquals("Male", bean.getGender());
		assertFalse(bean.isEstimated());
		assertFalse(bean.isAdded());
	}
	
	@Test
	public void testGetByBirthID() throws Exception {
		// get data
		list = sql.getByParentID(7);
		long visitID = list.get(0).getVisitID();
		int birthID = list.get(0).getBirthID();
		bean = sql.getByBirthID(visitID, birthID);
		
		// check assertions
		assertEquals(7, bean.getParentID());
		assertEquals(visitID, bean.getVisitID());
		assertEquals(birthID, bean.getBirthID());
		assertDateTimeEquals(LocalDateTime.of(2017, 4, 3, 8, 30), bean.getBirthdate());
		assertEquals("Male", bean.getGender());
		assertEquals(bean.isEstimated(), true);
		assertEquals(bean.isAdded(), false);

	}
	
	@Test
	public void testRemove() throws Exception {
		// get data to remove
		list = sql.getByParentID(7);
		assertEquals(1, list.size());
		long visitID = list.get(0).getVisitID();
		int birthID = list.get(0).getBirthID();
		
		// remove data
		sql.remove(visitID, birthID);
		
		// ensure empty
		bean = sql.getByBirthID(visitID, birthID);
		assertEquals(-1, bean.getParentID());
		assertEquals(-1, bean.getVisitID());
		assertEquals(-1, bean.getBirthID());
		list = sql.getByParentID(7);
		assertEquals(0, list.size());
	}
	
	private void assertDateTimeEquals(LocalDateTime t1, LocalDateTime t2) {
		LocalDateTime a = t1.truncatedTo(ChronoUnit.MINUTES);		
		LocalDateTime b = t2.truncatedTo(ChronoUnit.MINUTES);
		assertEquals(a, b);
	}

	private Childbirth updateBean(long visitID, int birthID, LocalDateTime date) {
		Childbirth b = new Childbirth();
		b.setParentID(2L);
		b.setVisitID(visitID);
		b.setBirthID(birthID);
		b.setBirthdate(date);
		b.setGender("Male");
		b.setEstimated(false);
		b.setAdded(false);
		return b;
	}
	
	@Test
	public void testParentIDException() throws Exception {
		// Invoke SQLException catch block via mocking
		sql = new ChildbirthMySQL(mockDS);
		Connection mockConn = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConn);
		when(mockConn.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			sql.getByParentID(2L);
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
	}

	@Test
	public void testBirthIDException() throws Exception {
		// Invoke SQLException catch block via mocking
		sql = new ChildbirthMySQL(mockDS);
		Connection mockConn = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConn);
		when(mockConn.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			sql.getByBirthID(1L, 2);
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
	}
	
	@Test
	public void testVisitIDException() throws Exception {
		// Invoke SQLException catch block via mocking
		sql = new ChildbirthMySQL(mockDS);
		Connection mockConn = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConn);
		when(mockConn.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			sql.getByVisitID(2L);
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
	}
	
	@Test
	public void testCreateException() throws Exception {
		// Invoke SQLException catch block via mocking
		sql = new ChildbirthMySQL(mockDS);
		Mockito.doThrow(SQLException.class).when(mockDS).getConnection();
		try {
			sql.update(updateBean(51L, -1, LocalDateTime.now()));
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
	}

	@Test
	public void testUpdateException() throws Exception {
		// Invoke SQLException catch block via mocking
		sql = new ChildbirthMySQL(mockDS);
		Mockito.doThrow(SQLException.class).when(mockDS).getConnection();
		try {
			sql.update(updateBean(51L, 2, LocalDateTime.now()));
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
	}
	
	@Test
	public void testRemoveException() throws Exception {
		// Invoke SQLException catch block via mocking
		sql = new ChildbirthMySQL(mockDS);
		Mockito.doThrow(SQLException.class).when(mockDS).getConnection();
		try {
			sql.remove(51L, 1);
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
	}
	
}
