package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;
import org.junit.Test;

import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInit;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ObstetricsInitTest {

	private ObstetricsInit ob;
	private LocalDate date;
	
	@Test
	public void testInitialization() {
		LocalDate init = LocalDate.now().minusDays(30);
		LocalDate lmp = LocalDate.now().minusDays(230);
		LocalDate edd = LocalDate.now().plusDays(50);
		
		ob = new ObstetricsInit();		
		ob.setPatientId(101);
		ob.setInitDate(init);
		ob.setLastMenstrualPeriod(lmp);
		
		assertEquals(101, ob.getPatientId());
		assertEquals(init, ob.getInitDate());
		assertEquals(lmp, ob.getLastMenstrualPeriod());
		assertEquals(edd, ob.getEstimatedDueDate());
		assertEquals(200, ob.getDaysPregnant());
		assertEquals("28 weeks 4 days", ob.getTimePregnant());
	}
	
	@Test
	public void testEmptyInit() {
		ob = new ObstetricsInit();
		assertEquals(LocalDate.now(), ob.getInitDate());
		assertEquals(LocalDate.now(), ob.getLastMenstrualPeriod());
		assertEquals(LocalDate.now().plusDays(280), ob.getEstimatedDueDate());
		assertEquals(0, ob.getDaysPregnant());
		
		ob.setInitDate(null);
		ob.setLastMenstrualPeriod(null);
		assertNull(ob.getInitDate());
		assertNull(ob.getLastMenstrualPeriod());
		assertNull(ob.getEstimatedDueDate());
		assertEquals(-1, ob.getDaysPregnant());
	}
	
	@Test
	public void testSetLastMenstrualPeriod() {
		// set LMP
		ob = new ObstetricsInit();
		date = LocalDate.of(2017, 2, 21);
		ob.setLastMenstrualPeriod(date);
		
		// check EDD
		LocalDate expEdd = LocalDate.of(2017, 11, 28);
		assertTrue(expEdd.equals(ob.getEstimatedDueDate()));
		
		// check days pregnant
		long expDays = ChronoUnit.DAYS.between(date, LocalDate.now().atStartOfDay());
		assertEquals(expDays, ob.getDaysPregnant());
	}
	
	@Test
	public void testPregnantString() {		
		ob = new ObstetricsInit();

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
