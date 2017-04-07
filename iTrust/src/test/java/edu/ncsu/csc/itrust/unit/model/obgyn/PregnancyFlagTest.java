package edu.ncsu.csc.itrust.unit.model.obgyn;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.model.obgyn.PregnancyFlag;

public class PregnancyFlagTest {

	private PregnancyFlag flag;
	
	@Test
	public void testFlagConstructor() {
		flag = new PregnancyFlag();
		assertFalse(flag.isFlag());
		assertEquals("empty flag", flag.getName());
		assertEquals("", flag.getDescription());
	}

	@Test
	public void testFlagGettersSetters() {
		flag = new PregnancyFlag(true, "Genetic", "");
		assertTrue(flag.isFlag());
		assertEquals("Genetic", flag.getName());
		assertEquals("", flag.getDescription());
		
		flag.setFlag(false);
		flag.setName("empty flag");
		flag.setDescription("hi");
		
		assertFalse(flag.isFlag());
		assertEquals("empty flag", flag.getName());
		assertEquals("hi", flag.getDescription());
	}

}
