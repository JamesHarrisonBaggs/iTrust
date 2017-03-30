package edu.ncsu.csc.itrust.unit.controller.obgyn;

import static org.junit.Assert.*;

import java.time.LocalDate;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.controller.obgyn.ObstetricsVisitController;
import edu.ncsu.csc.itrust.model.ConverterDAO;

public class ObstetricsVisitControllerTest {

	ObstetricsVisitController ovc;
	DataSource ds;
	
	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		ovc = new ObstetricsVisitController(ds);
	}

	@Test
	public void test() throws Exception {
		ovc.setAmount(0);
		assertEquals(0, ovc.getAmount());
		ovc.setAutoSchedule(false);
		ovc.setBloodPressure("100/60");
		assertEquals("100/60", ovc.getBloodPressure());
//		ovc.setEligible();
		ovc.setFetalHeartRate(80);
		assertEquals(80, ovc.getFetalHeartRate());
		ovc.setLowLyingPlacenta(true);
		assertTrue(ovc.isLowLyingPlacenta());
		ovc.setNotice();
//		ovc.setObgyn();
//		ovc.setObstetricsList();
		ovc.setPatientId(2);
		assertEquals(2, ovc.getPatientId());
		ovc.setRhFlag(true);
		assertTrue(ovc.isRhFlag());
		ovc.setSessionUtils(null);
		ovc.setTransactionLogger(null);
		ovc.setVisitDate(LocalDate.of(2015, 2, 28).atTime(0, 0));
		assertEquals(LocalDate.of(2015, 2, 28), ovc.getVisitDate().toLocalDate());
		ovc.setVisitId(2);
		assertEquals(2, ovc.getVisitId());
		ovc.setWeight(2.2);
		assertEquals(2.2, ovc.getWeight(), 0.01);
	}

}
