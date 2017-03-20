package edu.ncsu.csc.itrust.unit.model.healthtracker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import edu.ncsu.csc.itrust.model.healthtracker.HealthTrackerSQLLoader;
import junit.framework.TestCase;

/**
 * Based on OfficeVisitSQLLoaderTest
 * @author erein
 *
 */
public class HealthTrackerSQLLoaderTest extends TestCase {
	
	private static String MOCK_COLUMN_NAME = "mock_column";
	private static Integer MOCK_COLUMN_INDEX = 0;
	private static Integer MOCK_INT_VALUE = 1031;
	private static Float MOCK_FLOAT_VALUE = 1031.1f;
	
	private HealthTrackerSQLLoader htloader;
	
	@Mock
	private ResultSet mockIntValResultSet;
	@Mock
	private ResultSet mockIntNullResultSet;
	@Mock
	private ResultSet mockFloatValResultSet;
	@Mock
	private ResultSet mockFloatNullResultSet;
	@Mock
	private PreparedStatement mockPreparedStatement;
	
	@SuppressWarnings("null")
	@Override
	public void setUp() throws Exception {
		htloader = new HealthTrackerSQLLoader();
		
		// Mock a ResultSet containing int value
		mockIntValResultSet = Mockito.mock(ResultSet.class);
		Mockito.when(mockIntValResultSet.getInt(MOCK_COLUMN_NAME)).thenReturn(MOCK_INT_VALUE);
		Mockito.when(mockIntValResultSet.wasNull()).thenReturn(false);
		
		// Mock a ResultSet containing null int value
		mockIntNullResultSet = Mockito.mock(ResultSet.class);
		Mockito.when(mockIntNullResultSet.getInt(MOCK_COLUMN_NAME)).thenReturn(0);
		Mockito.when(mockIntNullResultSet.wasNull()).thenReturn(true);
		
		// Mock a ResultSet containing int value
		mockFloatValResultSet = Mockito.mock(ResultSet.class);
		Mockito.when(mockFloatValResultSet.getFloat(MOCK_COLUMN_NAME)).thenReturn(MOCK_FLOAT_VALUE);
		Mockito.when(mockFloatValResultSet.wasNull()).thenReturn(false);
		
		// Mock a ResultSet containing null int value
		mockFloatNullResultSet = Mockito.mock(ResultSet.class);
		Mockito.when(mockFloatNullResultSet.getFloat(MOCK_COLUMN_NAME)).thenReturn(0.0f);
		Mockito.when(mockFloatNullResultSet.wasNull()).thenReturn(true);
		
		// Mock a PreparedStatement with a placeholder
		mockPreparedStatement = Mockito.mock(PreparedStatement.class);
	}
	
	@Test
	public void testGetIntOrNull() throws Exception {
		Assert.assertEquals(MOCK_INT_VALUE, htloader.getIntOrNull(mockIntValResultSet, MOCK_COLUMN_NAME));
		Assert.assertEquals(null, htloader.getIntOrNull(mockIntNullResultSet, MOCK_COLUMN_NAME));
	}
	
	@Test
	public void testGetFloatOrNull() throws Exception {
		Assert.assertEquals(MOCK_FLOAT_VALUE, htloader.getFloatOrNull(mockFloatValResultSet, MOCK_COLUMN_NAME));
		Assert.assertEquals(null, htloader.getFloatOrNull(mockFloatNullResultSet, MOCK_COLUMN_NAME));
	}
	
	@Test
	public void testSetIntOrNull() throws Exception {
		try {
			htloader.setIntOrNull(mockPreparedStatement, MOCK_COLUMN_INDEX, MOCK_INT_VALUE);
		} catch (NullPointerException e) {
			fail();
		}
		
		try {
			htloader.setIntOrNull(mockPreparedStatement, MOCK_COLUMN_INDEX, null);
		} catch (NullPointerException e) {
			fail();
		}
	}
	
	@Test
	public void testSetFloatOrNull() throws Exception {
		try {
			htloader.setFloatOrNull(mockPreparedStatement, MOCK_COLUMN_INDEX, MOCK_FLOAT_VALUE);
		} catch (NullPointerException e) {
			fail();
		}
		
		try {
			htloader.setFloatOrNull(mockPreparedStatement, MOCK_COLUMN_INDEX, null);
		} catch (NullPointerException e) {
			fail();
		}
	}
}

