package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.sql.Timestamp;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ObstetricsInitMySQLTest {

	private ObstetricsInitMySQL db;
	private DataSource ds;
	private TestDataGenerator gen;
	
	private List<ObstetricsInit> list;
	private ObstetricsInit bean;
		
	@Before
	public void setUp() throws Exception {
		// get database
		ds = ConverterDAO.getDataSource();
		db = new ObstetricsInitMySQL(ds);
		
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
		list = db.getByID(2);
		assertEquals(3, list.size());

//		(2,'2002-05-01 00:00:01','2016-01-01 00:00:01', 1),
//		(2,'1996-05-03 00:00:01','1990-01-01 00:00:01', 0),
//		(2, '1992-05-02 00:00:01', '1992-01-01 00:00:01', 0),

		assertEquals(list.get(0).getInitDate(), LocalDate.of(2002, 05, 01));
		assertEquals(list.get(1).getInitDate(), LocalDate.of(1996, 05, 03));
		assertEquals(list.get(2).getInitDate(), LocalDate.of(1992, 05, 02));
		
		assertEquals(list.get(0).getLastMenstrualPeriod(), LocalDate.of(2016, 1, 1));
		assertEquals(list.get(1).getLastMenstrualPeriod(), LocalDate.of(1990, 1, 1));
		assertEquals(list.get(2).getLastMenstrualPeriod(), LocalDate.of(1992, 1, 1));
		
	}

	@Test
	public void testGetByDate() throws Exception {
		list = db.getByDate(2, Timestamp.valueOf("1992-05-02 00:00:00"));
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

}
