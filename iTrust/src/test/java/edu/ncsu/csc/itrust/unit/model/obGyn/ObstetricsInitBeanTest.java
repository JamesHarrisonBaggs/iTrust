package edu.ncsu.csc.itrust.unit.model.obGyn;

import static org.junit.Assert.*;
import org.junit.Test;

import edu.ncsu.csc.itrust.model.obGyn.ObstetricsInitBean;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ObstetricsInitBeanTest {

	private ObstetricsInitBean ob;
	private LocalDate date;
	
	@Test
	public void testObstetricsInitBean() {
		ob = new ObstetricsInitBean(true);
		
		LocalDate init = LocalDate.of(2016, 3, 24);
		LocalDate lmp = LocalDate.now().minusDays(9);
		
		ob.setPatientId(101);
		ob.setInitDate(init);
		ob.setLastMenstrualPeriod(lmp);
		ob.setCurrent(true);
		
		assertEquals(101, ob.getPatientId());
		assertEquals(init, ob.getInitDate());
		assertEquals(lmp, ob.getLastMenstrualPeriod());
//		assertEquals(ob.getEstimatedDueDate());
//		assertEquals(ob.getDaysPregnant());
//		assertEquals(ob.getTimePregnant());
		assertEquals(true, ob.isCurrent());

	}
	
	@Test
	public void testSetTimestamps() {
		ob = new ObstetricsInitBean();

		ob.setInitTimestamp(Timestamp.valueOf("2016-03-24 10:10:10.0"));
		ob.setLMPTimestamp(Timestamp.valueOf("2016-02-20 10:10:10.0"));
		
		assertEquals(LocalDate.of(2016, 3, 24), ob.getInitDate());
		assertEquals(LocalDate.of(2016, 02, 20), ob.getLastMenstrualPeriod());
				
	}
	
	@Test
	public void testGetTimestamps() {
		ob = new ObstetricsInitBean();

		ob.setInitDate(LocalDate.of(2016, 3, 24));
		ob.setLastMenstrualPeriod(LocalDate.of(2016, 02, 20));
		
		assertEquals(Timestamp.valueOf("2016-03-24 10:10:10.0"), ob.getInitTimestamp());
		assertEquals(Timestamp.valueOf("2016-02-20 10:10:10.0"), ob.getLMPTimestamp());

	}
	
	@Test
	public void testSetLastMenstrualPeriod() {
		ob = new ObstetricsInitBean(false);
		
		// set LMP
		date = LocalDate.of(2017, 2, 21);
		ob.setLastMenstrualPeriod(date);
		
		// check EDD
		LocalDate expEdd = LocalDate.of(2017, 11, 28);
		assertTrue(expEdd.equals(ob.getEstimatedDueDate()));
		
		// check days pregnant
		long expDays = ChronoUnit.DAYS.between(date, LocalDate.now().atStartOfDay());
		long actDays = ob.getDaysPregnant();		
		assertEquals(expDays, actDays);
	}
	
	@Test
	public void testPregnantString() {		
		ob = new ObstetricsInitBean();

		// 0 week
		ob.setLastMenstrualPeriod(LocalDate.now().minusDays(1));
		assertEquals("0 weeks 1 day", ob.getTimePregnant());
		
		// 1 week
		ob.setLastMenstrualPeriod(LocalDate.now().minusDays(10));
		assertEquals("1 week 3 days", ob.getTimePregnant());
		
		// 0 days
		ob.setLastMenstrualPeriod(LocalDate.now().minusDays(14));
		assertEquals("2 weeks 0 days", ob.getTimePregnant());

		// 1 day
		ob.setLastMenstrualPeriod(LocalDate.now().minusDays(99));
		assertEquals("14 weeks 1 day", ob.getTimePregnant());
	}
}
