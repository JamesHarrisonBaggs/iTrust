package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.when;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.mysql.jdbc.Connection;

import java.time.LocalDate;
import java.util.List;
import java.sql.SQLException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ObstetricsInitMySQLTest {

	DataSource ds;
	TestDataGenerator gen;
	@Mock
	DataSource mockDS;

	ObstetricsInitMySQL sql;
	List<ObstetricsInit> list;
	ObstetricsInit bean;
		
	@Before
	public void setUp() throws Exception {
		// get database
		ds = ConverterDAO.getDataSource();
		mockDS = Mockito.mock(DataSource.class);
		sql = new ObstetricsInitMySQL(ds);
		
		// standard data
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}
	
	/**
	 * Tests the constructor fails without a context
	 */
	@Test
	public void testConstructor() throws Exception {
		ObstetricsInitMySQL ob = null;
		try {
			ob = new ObstetricsInitMySQL();			
		} catch (DBException e) {
			assertNull(ob);
			assertTrue(e.getExtendedMessage().contains("Context Lookup Naming Exception"));
		}
	}

	/**
	 * Tests that getID returns the proper set of data 
	 */
	@Test
	public void testGetByID() throws Exception {
		list = sql.getByID(2);
		assertEquals(3, list.size());
		assertEquals(LocalDate.of(2016, 01, 01), list.get(0).getInitDate());
		assertEquals(LocalDate.of(1996, 05, 03), list.get(1).getInitDate());
		assertEquals(LocalDate.of(1992, 05, 02), list.get(2).getInitDate());
		assertEquals(LocalDate.of(2016, 1, 1), list.get(0).getLastMenstrualPeriod());
		assertEquals(LocalDate.of(1990, 1, 1), list.get(1).getLastMenstrualPeriod());
		assertEquals(LocalDate.of(1992, 1, 1), list.get(2).getLastMenstrualPeriod());
	}

	@Test
	public void testGetByDate() throws Exception {
		list = sql.getByDate(2, LocalDate.of(1992, 5, 2));
		assertEquals(1, list.size());
		assertEquals(LocalDate.of(1992, 5, 2), list.get(0).getInitDate());
		assertEquals(LocalDate.of(1992, 1, 1), list.get(0).getLastMenstrualPeriod());
		assertEquals(2, list.get(0).getPatientId());
		assertFalse(list.get(0).isCurrent());
	}

	@Test
	public void testUpdate() throws Exception {
		
		// create bean
		bean = new ObstetricsInit();
		bean.setPatientId(4);
		bean.setInitDate(LocalDate.of(2014, 5, 1));
		bean.setLastMenstrualPeriod(LocalDate.of(2014, 2, 7));
		
		// add bean
		int res = sql.update(bean);
		assertEquals(2, res);
		
		// get bean by date
		list = sql.getByDate(4, LocalDate.of(2014, 5, 1));
		assertEquals(1, list.size());
		bean = list.get(0);
		assertEquals(4, bean.getPatientId());
		assertEquals(LocalDate.of(2014, 5, 1), bean.getInitDate());
		assertEquals(LocalDate.of(2014, 2, 7), bean.getLastMenstrualPeriod());
	}
	
	@Test
	public void testGetExceptions() throws Exception {
		// Invoke SQLException catch block via mocking
		sql = new ObstetricsInitMySQL(mockDS);
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
		sql = new ObstetricsInitMySQL(mockDS);
		mockConn = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConn);
		when(mockConn.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			sql.getByDate(1L, LocalDate.now());
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
	}

	@Test
	public void testUpdateException() throws Exception {
		// Invoke SQLException catch block via mocking
		sql = new ObstetricsInitMySQL(mockDS);
		Mockito.doThrow(SQLException.class).when(mockDS).getConnection();
		try {
			sql.update(defaultBean());
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
	}
	
	private ObstetricsInit defaultBean() {
		bean = new ObstetricsInit();
		bean.setPatientId(101);
		bean.setInitDate(LocalDate.of(2016, 3, 24));
		bean.setLastMenstrualPeriod(LocalDate.now().minusDays(9));
		bean.setCurrent(true);
		return bean;
	}

}
