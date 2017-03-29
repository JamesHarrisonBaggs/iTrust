package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.Pregnancy;
import edu.ncsu.csc.itrust.model.obgyn.PregnancyMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class PregnancyMySQLTest {
	
	private PregnancyMySQL sql;
	private TestDataGenerator gen;
	private DataSource ds;
	
	private List<Pregnancy> list;
	private Pregnancy bean;

	@Before
	public void testUltrasoundMySQL() throws Exception {
		ds = ConverterDAO.getDataSource();
		sql = new PregnancyMySQL(ds);
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}
	
	/**
	 * Tests the constructor fails without a context
	 */
	@Test
	public void testConstructor() throws Exception {
		PregnancyMySQL ob = null;
		try {
			ob = new PregnancyMySQL();			
		} catch (DBException e) {
			assertNull(ob);
			assertTrue(e.getExtendedMessage().contains("Context Lookup Naming Exception"));
		}
	}
	
	@Test
	public void testCreate() throws Exception {
		
		// create a new bean
		bean = new Pregnancy();
		bean.setPatientId(7);
		bean.setDateOfBirth(LocalDate.of(1996, 3, 24));
		bean.setYearOfConception(1995);
		bean.setDaysPregnant(280);
		bean.setHoursInLabor(9);
		bean.setWeightGain(18.72);
		bean.setDeliveryType("vacuum");
		bean.setAmount(3);
		
		// see if it was created
		int results = sql.update(bean);
		assertEquals(1, results);
		
		// validate it is there
		list = sql.getByID(7);
		assertEquals(1, list.size());
		assertEquals(7, list.get(0).getPatientId());
		assertEquals(1995, list.get(0).getYearOfConception());
		assertEquals(18.72, list.get(0).getWeightGain(), 0.01);
		assertEquals("vacuum", list.get(0).getDeliveryType());
		assertEquals(3, list.get(0).getAmount());
		
	}
	
	@Test
	public void testUpdate() throws Exception {
		// create an update bean
		bean = new Pregnancy();
		bean.setPatientId(2);
		bean.setDateOfBirth(LocalDate.of(1991, 1, 1));
		bean.setYearOfConception(1990);
		bean.setDaysPregnant(288);
		bean.setHoursInLabor(6);
		bean.setWeightGain(19.4);
		bean.setDeliveryType("caesarean");
		bean.setAmount(2);
		
		// see if it was updated
		int results = sql.update(bean);
		assertEquals(2, results);
		
		// validate that it was
		list = sql.getByDate(2, Timestamp.valueOf(LocalDate.of(1991, 1, 1).atTime(0, 0)));
		assertEquals(5, list.size());
		bean = list.get(4);
		assertEquals(2, bean.getPatientId());
		assertEquals(1990, bean.getYearOfConception());
		assertEquals(288, bean.getDaysPregnant());
		assertEquals(6, bean.getHoursInLabor());
		assertEquals(19.4, bean.getWeightGain(), 0.01);
		assertEquals("caesarean", bean.getDeliveryType());
		assertEquals(2, bean.getAmount());
		
	}
	
	@Test
	public void testGetByID() throws Exception {
		list = sql.getByID(2);
		assertEquals(5, list.size());
	}

	@Test
	public void testGetByDate() throws Exception {
		list = sql.getByDate(2, Timestamp.valueOf(LocalDate.of(1973, 1, 1).atTime(0, 0)));		
		assertEquals(3, list.size());
		bean = list.get(2);
		assertEquals(2, bean.getPatientId());
		assertEquals(1973, bean.getYearOfConception());
		assertEquals(290, bean.getDaysPregnant());
		assertEquals(1, bean.getHoursInLabor());
		assertEquals(41.1, bean.getWeightGain(), 0.01);
		assertEquals("forceps", bean.getDeliveryType());
	}

}
