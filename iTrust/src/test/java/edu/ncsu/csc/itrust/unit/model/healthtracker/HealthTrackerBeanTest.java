package edu.ncsu.csc.itrust.unit.model.healthtracker;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import static org.junit.Assert.*;
import org.junit.Test;
import edu.ncsu.csc.itrust.model.healthtracker.HealthTrackerBean;

public class HealthTrackerBeanTest {

	/**
	 * Tests getters and setters
	 */
	@Test
	public void testHealthTrackerBean() {
		HealthTrackerBean bean = new HealthTrackerBean();
		bean.setActiveHours(5);
		bean.setActivityCalories(250);
		bean.setCalories(2010);
		bean.setDistance(4.4);
		bean.setFloors(3);
		bean.setHeartrateAverage(71);
		bean.setHeartrateHigh(106);
		bean.setHeartrateLow(42);
		bean.setMinutesFairlyActive(5);
		bean.setMinutesLightlyActive(7);
		bean.setMinutesSedentary(10);
		bean.setMinutesVeryActive(25);
		bean.setPatientId(2);
		bean.setSteps(11000);
		bean.setUvExposure(7);
		assertEquals(5, bean.getActiveHours());
		assertEquals(250, bean.getActivityCalories());
		assertEquals(2010, bean.getCalories());
		assertEquals(4.4, bean.getDistance(), 0);
		assertEquals(3, bean.getFloors());
		assertEquals(71, bean.getHeartrateAverage());
		assertEquals(106, bean.getHeartrateHigh());
		assertEquals(42, bean.getHeartrateLow());
		assertEquals(5, bean.getMinutesFairlyActive());
		assertEquals(7, bean.getMinutesLightlyActive());
		assertEquals(10, bean.getMinutesSedentary());
		assertEquals(25, bean.getMinutesVeryActive());
		assertEquals(2, bean.getPatientId());
		assertEquals(11000, bean.getSteps());
		assertEquals(7, bean.getUvExposure());
	}
	
	@Test
	public void testReplace() {
		String rep = "\\testing-files\\sample_healthtracker\\fitbit-test-data.csv";
		rep = rep.replace("\\", "/");
		assertEquals("/testing-files/sample_healthtracker/fitbit-test-data.csv", rep);
	}
	
	@Test
	public void testGetTimestamp() {
		HealthTrackerBean bean = new HealthTrackerBean();
		LocalDate date = LocalDate.of(2017, 3, 24);
		bean.setDate(date);
		Timestamp ts = bean.getTimestamp();
		assertEquals(ts.toLocalDateTime().toLocalDate(), date);
	}
	
	@Test
	public void testSetTimestamp() {
		HealthTrackerBean bean = new HealthTrackerBean();
		Timestamp ts = Timestamp.valueOf("2017-07-17 10:10:10.0");
		bean.setTimestamp(ts);
		assertEquals(LocalDate.of(2017, 07, 17), bean.getDate());
	}
	
	@Test
	public void testGetDateStandard() {
		HealthTrackerBean bean = new HealthTrackerBean();
		LocalDate locDate = LocalDate.of(2017, 3, 24);
		bean.setDate(locDate);
		Date date = bean.getDateStandard();
		assertEquals(locDate, date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
	}
	
//	@Test
//	public void testSetDateStandard() {
//		HealthTrackerBean bean = new HealthTrackerBean();
//	}

	@Test
	public void testGetDateString() {
		HealthTrackerBean bean = new HealthTrackerBean();
		LocalDate date = LocalDate.of(2017, 3, 24);
		bean.setDate(date);
		
		String dateString = bean.getDateString();
		assertEquals("3/24/2017", dateString);
	}
	
	@Test
	public void testSetDateString() {
		HealthTrackerBean bean = new HealthTrackerBean();
		bean.setDateString("5/6/2016");
		
		LocalDate date = bean.getDate();
		assertEquals(LocalDate.of(2016, 5, 6), date);
		
		String dateStr = bean.getDateString();
		assertEquals("5/6/2016", dateStr);
	}

	@Test
	public void testEquals() {
		HealthTrackerBean bean1 = new HealthTrackerBean();
		bean1.setPatientId(20);
		bean1.setHeartrateHigh(90);
		bean1.setActiveHours(4);
		
		HealthTrackerBean bean2 = new HealthTrackerBean();
		bean2.setPatientId(20);
		bean2.setHeartrateHigh(90);
		bean2.setActiveHours(4);

		HealthTrackerBean bean3 = new HealthTrackerBean();
		bean3.setPatientId(21);
		bean3.setHeartrateAverage(96);
		bean3.setActiveHours(4);
		
		assertEquals(bean1, bean2);
		assertEquals(bean2, bean2);
		assertNotEquals(bean1, bean3);
		assertNotEquals(bean2, bean3);
		
	}

}
