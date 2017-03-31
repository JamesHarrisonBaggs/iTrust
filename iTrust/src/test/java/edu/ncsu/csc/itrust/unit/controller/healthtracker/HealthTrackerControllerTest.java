package edu.ncsu.csc.itrust.unit.controller.healthtracker;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;

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
	
	DataSource ds;
	HealthTrackerController controller;
	TestDataGenerator gen;

	@Mock private HttpServletRequest mockHttpServletRequest;
	@Mock private HttpSession mockHttpSession;
	@Mock private SessionUtils mockSessionUtils;
	
	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}
	
	@Ignore
	@Test
	public void testDiabolicals() throws Exception {
		controller = new HealthTrackerController(ds);
		HealthTrackerMySQL sql = spy(new HealthTrackerMySQL(ds));
		controller.setSql(sql);
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		controller.setSessionUtils(mockSessionUtils);
		HealthTrackerBean bean = new HealthTrackerBean();
		bean.setDate(LocalDate.now());
		when(sql.updateData(bean)).thenThrow(new DBException(null));
		controller.updateData(bean);
	}

	@Ignore
	@Test
	public void testUploadData() throws Exception {
		controller = new HealthTrackerController(ds);
		HealthTrackerMySQL sql = spy(new HealthTrackerMySQL(ds));
		controller.setSql(sql);
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
		JUnitiTrustUtils.assertLogged(TransactionType.UPLOAD_HEALTHTRACKER_DATA, 0, 0, "");
	}

}
