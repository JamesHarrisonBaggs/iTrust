package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class ObstetricsInitMySQLTest {

	private ObstetricsInitMySQL db;
	private DAOFactory dao;
	private TestDataGenerator gen;
	
	private List<ObstetricsInit> list;
	private ObstetricsInit bean;
	
	@Before
	public void setUp() throws Exception {
		// get database
		dao = TestDAOFactory.getTestInstance();
		db = new ObstetricsInitMySQL(dao);
		
		// generate UC93
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}

	@Test
	public void testGetByID() throws Exception {
		
		try {
			list = db.getByID(2);
		} catch (DBException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		assertEquals(3, list.size());
		assertEquals(true, list.get(2).isCurrent());
		assertEquals(false, list.get(1).isCurrent());
		assertEquals(false, list.get(00).isCurrent());

	}

	@Test
	public void testGetByDate() throws Exception {
		try {
			list = db.getByDate(2, Timestamp.valueOf("1972-01-01 00:00:01"));
		} catch (DBException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		assertEquals(1, list.size());
		ObstetricsInit b = list.get(0);		
		assertEquals(true, b.isCurrent());
		assertEquals(2, b.getPatientId());
		assertEquals(LocalDate.of(1972, 01, 01), b.getInitDate());
	}

	@Test
	public void testUpdate() throws Exception {
		
		// create bean
		bean = new ObstetricsInit();
		bean.setPatientId(3);
		bean.setInitTimestamp(Timestamp.valueOf("2017-03-24 00:00:00"));
		bean.setLMPTimestamp(Timestamp.valueOf("2017-02-04 00:00:00"));
		bean.setCurrent(false);
		
		// add bean
		try {
			int res = db.update(bean);
			assertEquals(1, res);
		} catch (DBException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		// get bean by date
		try {
			list = db.getByDate(3, Timestamp.valueOf("2017-03-24 00:00:00"));
		} catch (DBException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
				
		// check assertions
		assertEquals(1, list.size());
		ObstetricsInit b = list.get(0);
		assertEquals(3, b.getPatientId());
		assertEquals(LocalDate.of(2017, 03, 24), b.getInitDate());
		assertEquals(LocalDate.of(2017, 02, 04), b.getLastMenstrualPeriod());
		assertEquals(false, b.isCurrent());

	}

	@SuppressWarnings("unused")
	private void printList(List<ObstetricsInit> list) {
		System.err.println(list.size());
		for (ObstetricsInit b : list) {
			System.err.println(b.getPatientId() + ", " + b.getInitDate());
		}
	} 
	
}
