package edu.ncsu.csc.itrust.controller.obGyn;

import static org.junit.Assert.*;
import org.junit.Test;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ObstetricsInitBeanTest {

	ObstetricsInitBean ob;
	LocalDate date;
	
	@Test
	public void testObstetricsInitBean() {
		ob = new ObstetricsInitBean();

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
