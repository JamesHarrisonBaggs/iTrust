package edu.ncsu.csc.itrust.unit.controller.obgyn;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import edu.ncsu.csc.itrust.controller.obgyn.ChildbirthController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.Childbirth;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

public class ChildbirthControllerTest {

	ChildbirthController controller;
	DataSource ds;
	TestDataGenerator gen;

	@Mock
	SessionUtils mockSessionUtils;

	List<Childbirth> list;
	Childbirth bean;

	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		Mockito.doReturn("51").when(mockSessionUtils).getRequestParameter(Mockito.anyString());
		Mockito.doReturn(2L).when(mockSessionUtils).getCurrentPatientMIDLong();
		controller = new ChildbirthController(ds, mockSessionUtils);
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}

	@Test
	public void testGettersSetters() {
		LocalDateTime date = LocalDateTime.of(2016, 03, 24, 1, 30);
		controller.setBirthDate(date);
		assertEquals(date, controller.getBirthDate());
		controller.setBirthID(0);
		controller.getBirthID();
		controller.setDateOfBirth(date);
		controller.getDateOfBirth();
		controller.setDeliveryType("Vaginal Delivery");
		controller.getDeliveryType();
		controller.setEpiduralAnaesthesia(0);
		controller.getEpiduralAnaesthesia();
		controller.setGender("Male");
		controller.getGender();
		controller.setMagnesiumSO4(0);
		controller.getMagnesiumSO4();
		controller.setMid(1);
		controller.getMid();
		controller.setName("Eric");
		controller.getName();
		controller.setNitrousOxide(10);
		controller.getNitrousOxide();
		controller.setPethidine(10);
		controller.getPethidine();
		controller.setPid(1);
		controller.getPid();
		controller.setPitocin(10);
		controller.getPitocin();
		controller.setTimeOfBirth(LocalTime.now());
		controller.getTimeOfBirth();
		controller.setVisitDate(date);
		controller.getVisitDate();
		controller.setVisitId(20);
		controller.getVisitId();
	}

	@Test
	public void testBooleans() {
		controller.setEditBaby(true);
		controller.isEditBaby();
		controller.setAdded(true);
		controller.isAdded();
		controller.setEstimated(true);
		controller.isEstimated();
		controller.setNewBaby(false);
		controller.isNewBaby();
		controller.setPreEstimateFlag(true);
		controller.isPreEstimateFlag();
		controller.setPreScheduled(true);
		controller.isPreScheduled();
	}

	@Test
	public void testChildbirthController() {
		try {
			controller.submitNew();
			controller.getChildbirthVisitID();
			controller.getChildbirthVisitVisitID();
		} catch (DBException e) {
			System.out.println(e.getExtendedMessage());
		}
	}

}
