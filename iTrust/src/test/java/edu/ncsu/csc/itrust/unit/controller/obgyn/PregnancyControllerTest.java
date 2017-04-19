package edu.ncsu.csc.itrust.unit.controller.obgyn;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import edu.ncsu.csc.itrust.controller.obgyn.PregnancyController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.Pregnancy;
import edu.ncsu.csc.itrust.model.obgyn.PregnancyMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

public class PregnancyControllerTest {
	
	PregnancyController controller;
	DataSource ds;
	TestDataGenerator gen;
	
	PregnancyMySQL sql;
	List<Pregnancy> list;
	Pregnancy bean;
	
	@Mock SessionUtils mockSessionUtils;

	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		Mockito.doReturn(2L).when(mockSessionUtils).getCurrentPatientMIDLong();
		controller = new PregnancyController(ds, mockSessionUtils, TestDAOFactory.getTestInstance());
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}

	@Test
	public void testPregnancyController() throws Exception {
		sql = controller.getSql();
		assertNotNull(sql);
		
		list = controller.getPregnancies();
		assertEquals(5, list.size());
		
		list = controller.getPregnancyByDate(LocalDate.of(1991, 1, 1));
		assertEquals(5, list.size());
	}
	
	@Test
	public void testUpdate() throws Exception {
		bean = defaultBean();
		bean.setDateOfBirth(LocalDate.of(1973, 1, 1));
		bean.setYearOfConception(1972);
		controller.update(bean);

		list = controller.getPregnancyByDate(LocalDate.of(1973, 1, 1));
		assertEquals(3, list.size());
		assertEquals(1972, list.get(2).getYearOfConception());
		assertEquals(20, list.get(2).getDaysPregnant());
	}
	
	@Test
	public void testDiabolicals() throws Exception {
		sql = spy(new PregnancyMySQL(ds));
		controller.setSql(sql);
		Pregnancy bean = defaultBean();
		when(sql.update(bean)).thenThrow(new NullPointerException());
		controller.update(bean);
	}
	
	@Test
	public void testDiabolicals2() throws Exception {
		sql = spy(new PregnancyMySQL(ds));
		controller.setSql(sql);
		bean = defaultBean();
		when(sql.update(bean)).thenThrow(new DBException(null));
		controller.update(bean);
	}
	
	private Pregnancy defaultBean() {
		Pregnancy bean = new Pregnancy();
		bean.setPatientId(2);
		bean.setDateOfBirth(LocalDate.of(1996, 3, 24));
		bean.setYearOfConception(1995);
		bean.setDaysPregnant(20);
		bean.setHoursInLabor(7);
		bean.setWeightGain(8.62);
		bean.setDeliveryType("vaginal");
		bean.setAmount(1);
		return bean;
	}

}
