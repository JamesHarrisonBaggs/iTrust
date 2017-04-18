package edu.ncsu.csc.itrust.unit.controller.obgyn;

import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import edu.ncsu.csc.itrust.controller.obgyn.LaborReportController;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

public class LaborReportControllerTest {
	/**
	LaborReportController controller;
	DataSource ds;
	@Mock SessionUtils mockSessionUtils;
	
	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		Mockito.doReturn(2L).when(mockSessionUtils).getCurrentPatientMIDLong();
		controller = new LaborReportController(ds, mockSessionUtils, TestDAOFactory.getTestInstance());
	}

	@Test
	public void testLaborReportController() throws Exception {
		assertNotNull(controller.getCurrentInit());
		controller.getObInits();
		controller.reportAvailable();
		controller.getObVisits();
		controller.getPregnancies();
		controller.getAllergies();
		controller.getMostRecentEDD();
		controller.getBloodType();
		controller.getRelevantConditions();
//		controller.getPregnancyWarningFlagsForVisit(obvisit);
		controller.getAllPregnancyWarningFlags();
//		controller.getComplication(visit);
		controller.logReportView();
	}
**/
}
