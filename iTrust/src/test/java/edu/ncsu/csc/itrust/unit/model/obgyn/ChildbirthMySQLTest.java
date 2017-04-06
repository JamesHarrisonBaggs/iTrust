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
import java.time.LocalDateTime;
import java.util.List;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.Childbirth;
import edu.ncsu.csc.itrust.model.obgyn.ChildbirthMySQL;
import edu.ncsu.csc.itrust.model.obgyn.ChildbirthVisitMySQL;
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

	@Test
	public void testCreate() {
		// fail("Not yet implemented");
	}
	
	@Test
	public void testUpdate() {
		// fail("Not yet implemented");
	}
	
	@Test
	public void testGetByVisitID() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetByBirthID() {
		// fail("Not yet implemented");
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
	public void testUpdateException() throws Exception {
		// Invoke SQLException catch block via mocking
		sql = new ChildbirthMySQL(mockDS);
		Mockito.doThrow(SQLException.class).when(mockDS).getConnection();
		try {
			sql.update(updateBean());
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
	}
	
	private Childbirth updateBean() {
		Childbirth b = new Childbirth();
		b.setParentID(2);
		b.setVisitID(51);
		b.setBirthID(1);
		b.setBirthdate(LocalDateTime.now());
		b.setEstimated(false);
		b.setGender("Male");
		return b;
	}

}
