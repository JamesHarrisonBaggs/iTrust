package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.Ultrasound;
import edu.ncsu.csc.itrust.model.obgyn.UltrasoundMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class UltrasoundMySQLTest {
	
	private UltrasoundMySQL sql;
	private TestDataGenerator gen;
	private DataSource ds;
	
	private List<Ultrasound> list;
	private Ultrasound bean;

	@Before
	public void testUltrasoundMySQL() throws Exception {
		ds = ConverterDAO.getDataSource();
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
		list = sql.getByPatientId(2);
		for (Ultrasound b : list) {
			if (b.getBiparietalDiameter() == 40 && b.getVisitDate().toLocalDate().equals(LocalDate.of(2016, 07, 28))) {
				visitId = b.getVisitId();
			}
		}
		
		// create an update bean with the same patient and visit ID
		bean = new Ultrasound();
		bean.setPatientId(2);
		bean.setVisitId(visitId);
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

		// check that an update was performed
		int results = sql.update(bean);
		assertEquals(2, results);
		
		// check assertions
		list = sql.getByPatientIdVisitId(2, visitId);
		assertEquals(2, list.size());
		
		bean = sql.getByVisitFetus(visitId, 1);
		assertEquals(2, bean.getPatientId());
		assertEquals(visitId, bean.getVisitId());
		assertEquals(now.toLocalDate(), bean.getVisitDate().toLocalDate());
		assertEquals(1, bean.getFetusId());
		assertEquals(110, bean.getAbdominalCircumference());

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

}
