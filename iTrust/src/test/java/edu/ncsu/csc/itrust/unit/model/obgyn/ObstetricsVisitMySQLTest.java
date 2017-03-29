package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsVisit;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsVisitMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class ObstetricsVisitMySQLTest extends TestCase {
	
	private ObstetricsVisitMySQL sql;	
	private TestDataGenerator gen;
	private DataSource ds;
	
	private List<ObstetricsVisit> list;
	private ObstetricsVisit bean;
	
	@Override
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		sql = new ObstetricsVisitMySQL(ds);
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
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
		assertEquals(113.76, bean.getWeight());
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
			if (b.getBloodPressure().equals("120/60")) {
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
		
		// check result
		list = sql.getByID(2);
		assertEquals(1, list.size());
		
		bean = list.get(0);
		assertEquals(2, bean.getPatientId());
		assertEquals(visitId, bean.getVisitId());
		assertEquals(now.toLocalDate(), bean.getVisitDate().toLocalDate());
		assertEquals(22, bean.getWeeksPregnant());
		assertEquals(127.9, bean.getWeight());
		assertEquals("120/60", bean.getBloodPressure());
		assertEquals(120, bean.getFetalHeartRate());
		assertEquals(1, bean.getAmount());
		assertFalse(bean.isLowLyingPlacenta());
		assertTrue(bean.isRhFlag());
		
	}

//	@Test
//	public void testGetByID() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetByVisit() {
//		fail("Not yet implemented");
//	}

}
