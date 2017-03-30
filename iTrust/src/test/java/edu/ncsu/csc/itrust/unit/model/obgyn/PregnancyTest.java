package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import edu.ncsu.csc.itrust.model.obgyn.Pregnancy;

public class PregnancyTest {

	private Pregnancy bean;
	
	@Test
	public void testPregnancyBean() {
		
		bean = new Pregnancy();
		bean.setPatientId(10);
		bean.setDateOfBirth(LocalDate.of(2016, 3, 24));
		bean.setYearOfConception(2015);
		bean.setDaysPregnant(30);
		bean.setHoursInLabor(7);
		bean.setWeightGain(4.5);
		bean.setDeliveryType("Vaginal");
		bean.setAmount(1);
		
		assertEquals(10, bean.getPatientId());
		assertEquals(LocalDate.of(2016, 3, 24), bean.getDateOfBirth());
		assertEquals(2015, bean.getYearOfConception());
		assertEquals(30, bean.getDaysPregnant());
		assertEquals(7, bean.getHoursInLabor());
		assertEquals(4.5, bean.getWeightGain(), 0);
		assertEquals("Vaginal", bean.getDeliveryType());
		assertEquals(1, bean.getAmount());
		
		// TODO test timestamp
		
	}

}
