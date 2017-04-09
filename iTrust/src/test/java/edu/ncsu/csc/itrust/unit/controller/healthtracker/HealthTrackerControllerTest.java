package edu.ncsu.csc.itrust.unit.controller.healthtracker;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.healthtracker.HealthTrackerController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.healthtracker.HealthTrackerBean;
import edu.ncsu.csc.itrust.model.healthtracker.HealthTrackerMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.JUnitiTrustUtils;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

public class HealthTrackerControllerTest {
	
	HealthTrackerController controller;
	DataSource ds;
	TestDataGenerator gen;

	List<HealthTrackerBean> list;
	HealthTrackerBean bean;
	
	@Mock HttpServletRequest mockHttpServletRequest;
	@Mock HttpSession mockHttpSession;
	@Mock SessionUtils mockSessionUtils;
	
	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		controller = new HealthTrackerController(ds);
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}
	
	@Test
	public void testGetData() throws Exception {
		// mock session utils
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		Mockito.doReturn(51L).when(mockSessionUtils).getCurrentPatientMIDLong();
		controller.setSessionUtils(mockSessionUtils);
		// set search date
		controller.setSearchDate(LocalDate.of(2017, 4, 9));
		assertEquals(LocalDate.of(2017, 4, 9), controller.getSearchDate());
		// various search methods
		controller.searchForData();
		list = controller.getDataList();
		list = controller.getDataInRange(LocalDate.now().minusDays(10), LocalDate.now());
		list = controller.getAllData();
	}

	@Test
	public void testUpdateData() throws Exception {
		// mock session utils
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		Mockito.doReturn(2L).when(mockSessionUtils).getCurrentPatientMIDLong();
		controller.setSessionUtils(mockSessionUtils);
		
		// update bean
		bean = new HealthTrackerBean(0, LocalDate.of(2016, 10, 02), 52, 5, 5.0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5);
		controller.updateData(bean);
		
		// test get
		list = controller.getDataInDay(LocalDate.of(2016, 10, 02));
		assertEquals(52, list.get(0).getCalories());
		
		// test data list
		controller.setDataList(null);
		assertNull(controller.getDataList());

	}
	
	@Test
	public void testUploadData() throws Exception {
		HealthTrackerMySQL sql = controller.getSql();
		assertNotNull(sql);
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		controller.setSessionUtils(mockSessionUtils);
		
		HealthTrackerBean bean = new HealthTrackerBean();
		bean.setDate(LocalDate.now());
		HealthTrackerBean bean2 = new HealthTrackerBean();
		bean2.setDate(LocalDate.now());
		ArrayList<HealthTrackerBean> list = new ArrayList<HealthTrackerBean>();
		list.add(bean);
		list.add(bean2);
		
		controller.uploadData(list);
//		JUnitiTrustUtils.assertLogged(TransactionType.UPLOAD_HEALTHTRACKER_DATA, 0, 0, "");
	}

	@Test
	public void testDiabolicals() throws Exception {
		HealthTrackerMySQL sql = spy(new HealthTrackerMySQL(ds));
		controller.setSql(sql);
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		controller.setSessionUtils(mockSessionUtils);
		
		bean = new HealthTrackerBean();
		bean.setDate(LocalDate.now());
		when(sql.updateData(bean)).thenThrow(new DBException(null));
		controller.updateData(bean);
	}

}
