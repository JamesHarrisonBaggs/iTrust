package edu.ncsu.csc.itrust.unit.controller.healthtracker;

import static org.junit.Assert.*;

import java.time.LocalDate;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import edu.ncsu.csc.itrust.controller.healthtracker.HealthTrackerChartData;
import edu.ncsu.csc.itrust.controller.obgyn.ChildbirthController;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

public class HealthTrackerChartDataTest {
	
	HealthTrackerChartData controller;
	DataSource ds;
	TestDataGenerator gen;
	
	@Mock SessionUtils mockSessionUtils;
	
	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		Mockito.doReturn("51").when(mockSessionUtils).getRequestParameter(Mockito.anyString());
		Mockito.doReturn(2L).when(mockSessionUtils).getCurrentPatientMIDLong();
		controller = new HealthTrackerChartData(ds, mockSessionUtils);
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}

	@Test
	public void testHealthTrackerChartData() throws Exception {
		controller.setStartDate(LocalDate.of(2017, 3, 24));
		assertEquals(LocalDate.of(2017, 3, 24), controller.getStartDate());
		controller.setEndDate(LocalDate.of(2017, 5, 24));
		assertEquals(LocalDate.of(2017, 5, 24), controller.getEndDate());
		controller.refreshChartData();
		controller.getStepData();
//		controller.updateChartRange();
		controller.setChartData("data");
		assertEquals("data", controller.getChartData());
		controller.setDataList(null);
		assertNull(controller.getDataList());
	}

}
