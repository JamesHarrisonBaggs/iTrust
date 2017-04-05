package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.mysql.jdbc.Connection;

import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.Ultrasound;
import edu.ncsu.csc.itrust.model.obgyn.UltrasoundMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class UltrasoundMySQLTest {
	
	private TestDataGenerator gen;
	private DataSource ds;
	@Mock
	DataSource mockDS;
	
	private UltrasoundMySQL sql;
	private List<Ultrasound> list;
	private Ultrasound bean;

	@Before
	public void testUltrasoundMySQL() throws Exception {
		ds = ConverterDAO.getDataSource();
		mockDS = Mockito.mock(DataSource.class);
		sql = new UltrasoundMySQL(ds);
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}
	
	/**
	 * Tests the constructor fails without a context
	 */
	@Test
	public void testConstructor() throws Exception {
		UltrasoundMySQL ob = null;
		try {
			ob = new UltrasoundMySQL();			
		} catch (DBException e) {
			assertNull(ob);
			assertTrue(e.getExtendedMessage().contains("Context Lookup Naming Exception"));
		}
	}

	@Test
	public void testCreate() throws Exception {
		
		bean = new Ultrasound();
		bean.setPatientId(3);
		bean.setVisitId(420);
		LocalDateTime now = LocalDateTime.now();
		bean.setVisitDate(now);
		bean.setFetusId(1);
		bean.setCrownRumpLength(80);
		bean.setBiparietalDiameter(40);
		bean.setHeadCircumference(130);
		bean.setFemurLength(25); 
		bean.setOccipitofrontalDiameter(40);
		bean.setAbdominalCircumference(110);
		bean.setHumerusLength(30);
		bean.setEstimatedFetalWeight(201.55);

		int results = sql.update(bean);
		assertEquals(1, results);
		
		list = sql.getByPatientIdVisitId(3, 420);
		assertEquals(1, list.size());
		assertEquals(3, list.get(0).getPatientId());
		assertEquals(420, list.get(0).getVisitId());
		assertEquals(now.toLocalDate(), list.get(0).getVisitDate().toLocalDate());
		
	}
	
	@Test
	public void testUpdate() throws Exception {
		
		// quick and ugly way to grab existing visit id
		long visitId = -1;
		int fetusId = -1;
		list = sql.getByPatientId(2);
		for (Ultrasound b : list) {
			if (b.getBiparietalDiameter() == 40 && b.getVisitDate().toLocalDate().equals(LocalDate.of(2016, 07, 28))) {
				visitId = b.getVisitId();
				fetusId = b.getFetusId();
			}
		}
		
		// create an update bean with the same patient/visit/fetus ID
		// updates local date, ofd, and hl
		bean = new Ultrasound();
		bean.setPatientId(2);
		bean.setVisitId(visitId);
		LocalDateTime now = LocalDateTime.now();
		bean.setVisitDate(now);
		bean.setFetusId(fetusId);
		bean.setCrownRumpLength(80);
		bean.setBiparietalDiameter(40);
		bean.setHeadCircumference(130);
		bean.setFemurLength(25); 
		bean.setOccipitofrontalDiameter(45);
		bean.setAbdominalCircumference(110);
		bean.setHumerusLength(35);
		bean.setEstimatedFetalWeight(201.55);

		// check that an update was performed
		sql.update(bean);
		list = sql.getByPatientId(2);
		// doesn't check that the "proper" number was returned
		
		// check assertions
		list = sql.getByPatientIdVisitId(2, visitId);
		assertEquals(2, list.size());
		
		bean = sql.getByVisitFetus(visitId, 1);
		assertEquals(2, bean.getPatientId());
		assertEquals(visitId, bean.getVisitId());
		assertEquals(now.toLocalDate(), bean.getVisitDate().toLocalDate());
		assertEquals(1, bean.getFetusId());
		assertEquals(80, bean.getCrownRumpLength());
		assertEquals(40, bean.getBiparietalDiameter());
		assertEquals(130, bean.getHeadCircumference());
		assertEquals(25, bean.getFemurLength());
		assertEquals(45, bean.getOccipitofrontalDiameter());		
		assertEquals(110, bean.getAbdominalCircumference());
		assertEquals(35, bean.getHumerusLength());
		assertEquals(201.55, bean.getEstimatedFetalWeight(), 0);
		
	}

	@Test
	public void testGetByPatientId() throws Exception {		
		list = sql.getByPatientId(2);
		assertEquals(2, list.size());
	}

	@Test
	public void testGetByPatientIdVisitId() throws Exception {
		list = sql.getByPatientId(7);
		long visitId = list.get(0).getVisitId();
		
		list = sql.getByPatientIdVisitId(7, visitId);
		assertEquals(7, list.get(0).getPatientId());
		assertEquals(visitId, list.get(0).getVisitId());
		assertEquals(LocalDate.of(2016, 1, 16), list.get(0).getVisitDate().toLocalDate());
		assertEquals(1, list.get(0).getFetusId());
		assertEquals(206.23, list.get(0).getEstimatedFetalWeight(), 0.01);
	}
	
	@Test
	public void testGetByVisitFetus() throws Exception {
		list = sql.getByPatientId(2);
		long visitId = list.get(0).getVisitId();
		
		bean = sql.getByVisitFetus(visitId, 2);
		assertEquals(2, bean.getPatientId());
		assertEquals(2, bean.getFetusId());
		assertEquals(81, bean.getCrownRumpLength());
		
		bean = sql.getByVisitFetus(visitId, 18);
		assertEquals(-1, bean.getPatientId());
		assertEquals(-1, bean.getVisitId());
		assertEquals(-1, bean.getFetusId());
		assertNull(bean.getVisitDate());
		assertEquals(0.0, bean.getEstimatedFetalWeight(), 0.01);
	}
	
	@Test
	public void testDeletes() throws Exception {
		list = sql.getByPatientId(2);
		long visitId = list.get(0).getVisitId();

		list = sql.getByPatientIdVisitId(2, visitId);
		assertEquals(2, list.size());
		
		assertTrue(sql.removeUltrasound(visitId, 2));
		
		list = sql.getByPatientIdVisitId(2, visitId);
		assertEquals(1, list.size());
		
		sql.removeUltrasoundImage(2, 1);
	}

	@Test
	public void testUpdateException() throws Exception {
		// Invoke SQLException catch block via mocking
		sql = new UltrasoundMySQL(mockDS);
		Mockito.doThrow(SQLException.class).when(mockDS).getConnection();
		try {
			sql.update(defaultBean());
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
	}
	
	@Test
	public void testDeleteExceptions() throws Exception {
		// Invoke SQLException catch block via mocking
		sql = new UltrasoundMySQL(mockDS);
		Mockito.doThrow(SQLException.class).when(mockDS).getConnection();
		try {
			sql.removeUltrasound(1L, 2);
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
		
		// Invoke SQLException catch block via mocking
		sql = new UltrasoundMySQL(mockDS);
		Mockito.doThrow(SQLException.class).when(mockDS).getConnection();
		try {
			sql.removeUltrasoundImage(1L, 2);
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
	}
	
	@Test
	public void testGetExceptions() throws Exception {
		// Invoke SQLException catch block via mocking
		sql = new UltrasoundMySQL(mockDS);
		Connection mockConn = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConn);
		when(mockConn.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			sql.getByVisitFetus(1L, 1);
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
		
		// Invoke SQLException catch block via mocking
		sql = new UltrasoundMySQL(mockDS);
		mockConn = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConn);
		when(mockConn.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			sql.getByPatientId(1L);
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
		
		// Invoke SQLException catch block via mocking
		sql = new UltrasoundMySQL(mockDS);
		mockConn = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConn);
		when(mockConn.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			sql.getByPatientIdVisitId(2L, 1L);
			fail("Exception should be thrown");
		} catch (DBException e) {
			assertNotNull(e.getMessage());
		}
	}

	private Ultrasound defaultBean() {
		Ultrasound bean = new Ultrasound();
		bean.setPatientId(0);
		bean.setVisitId(1);
		bean.setVisitDate(LocalDateTime.now());
		bean.setFetusId(1);
		bean.setCrownRumpLength(80);
		bean.setBiparietalDiameter(40);
		bean.setHeadCircumference(130);
		bean.setFemurLength(25);
		bean.setOccipitofrontalDiameter(40);
		bean.setAbdominalCircumference(110);
		bean.setHumerusLength(30);
		bean.setEstimatedFetalWeight(201.55);
		return bean;
	}
	
}
