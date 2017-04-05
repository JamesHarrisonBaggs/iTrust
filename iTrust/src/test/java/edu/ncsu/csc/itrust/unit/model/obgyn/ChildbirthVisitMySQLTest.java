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
import edu.ncsu.csc.itrust.model.obgyn.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.obgyn.ChildbirthVisitMySQL;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsVisitMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

import org.junit.Before;
import org.junit.Test;

public class ChildbirthVisitMySQLTest {
	
	TestDataGenerator gen;
	DataSource ds;
	@Mock
	DataSource mockDS;
	
	ChildbirthVisitMySQL sql;
	List<ChildbirthVisit> list;
	ChildbirthVisit bean;

	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		mockDS = Mockito.mock(DataSource.class);
		sql = new ChildbirthVisitMySQL(ds);
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}

	/**
	 * Tests the constructor fails without a context
	 */
	@Test
	public void testConstructor() throws Exception {
		sql = null;
		try {
			sql = new ChildbirthVisitMySQL();			
		} catch (DBException e) {
			assertNull(sql);
			assertTrue(e.getExtendedMessage().contains("Context Lookup Naming Exception"));
		}
	}
	
	@Test
	public void testCreate() throws Exception {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdate() throws Exception {
		// required values
		long visitID = getVisitID();
		LocalDateTime date = LocalDateTime.of(2017, 4, 5, 11, 30);
		int drugs[] = {5, 6, 7, 8, 9};
		
		// update and check
		bean = updateBean(visitID, date);
		sql.update(bean);
		// doesn't check the "proper" value was returned
		
		list = sql.getByPatientID(2);
		assertEquals(1, list.size());
		bean = list.get(0);
		assertEquals(2, bean.getPatientID());
		assertEquals(visitID, bean.getVisitID());
		assertEquals(date, bean.getVisitDate());
		assertFalse(bean.isPreSchedule());
		assertEquals("miscarriage", bean.getDeliveryType());
		assertArrayEquals(drugs, bean.getDosages());
		
	}
	
	/**
	 * A very specific method for returning the visitID of patient 2
	 */
	private long getVisitID() throws Exception {
		LocalDateTime date = LocalDateTime.of(2017, 4, 5, 11, 30);
		list = sql.getByPatientID(2);
		for (ChildbirthVisit b : list) {			
			if (date.equals(b.getVisitDate()) && b.isPreSchedule()) {
				return b.getVisitID();
			}
		}
		return -1;
	}

	@Test
	public void testGetByVisitId() throws Exception {
		// required values
		LocalDateTime date = LocalDateTime.of(2017, 4, 5, 11, 30);
		long visitID = getVisitID();
		int drugs[] = { 1, 0, 3, 0, 4 };
		
		// get and check
		bean = sql.getByVisitId(visitID);
		assertEquals(2, bean.getPatientID());
		assertEquals(date, bean.getVisitDate());
		assertTrue(bean.isPreSchedule());
		assertEquals("vaginal", bean.getDeliveryType());
		assertArrayEquals(drugs, bean.getDosages());
	}
	
	@Test
	public void testGetByPatientIDException() throws Exception {
		// Invoke SQLException catch block via mocking
		sql = new ChildbirthVisitMySQL(mockDS);
		Connection mockConn = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConn);
		when(mockConn.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			sql.getByPatientID(2L);
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
	}
	
	@Test
	public void testByVisitIDException() throws Exception {
		// Invoke SQLException catch block via mocking
		sql = new ChildbirthVisitMySQL(mockDS);
		Connection mockConn = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConn);
		when(mockConn.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			sql.getByVisitId(51L);
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
	}
	
	@Test
	public void testUpdateException() throws Exception {
		// Invoke SQLException catch block via mocking
		sql = new ChildbirthVisitMySQL(mockDS);
		Mockito.doThrow(SQLException.class).when(mockDS).getConnection();
		try {
			sql.update(updateBean(51, LocalDateTime.now()));
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
	}
	
	private ChildbirthVisit updateBean(long visitID, LocalDateTime date) {
		ChildbirthVisit b = new ChildbirthVisit();
		b.setPatientID(2);
		b.setVisitID(visitID);
		b.setVisitDate(date);
		b.setPreSchedule(false);
		b.setDeliveryType("miscarriage");
		b.setPitocin(5);
		b.setNitrousOxide(6);
		b.setPethidine(7);
		b.setEpiduralAnaesthesia(8);
		b.setMagnesiumSO4(9);
		return b;
	}

}
