package edu.ncsu.csc.itrust.unit.controller.healthtracker;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sql.DataSource;

import edu.ncsu.csc.itrust.CSVParser;
import edu.ncsu.csc.itrust.controller.healthtracker.HealthTrackerFileUpload;
import edu.ncsu.csc.itrust.exception.CSVFormatException;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.healthtracker.HealthTrackerBean;

public class HealthTrackerFileUploadTest {

	HealthTrackerFileUpload uploader;
	DataSource ds;
	
	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		uploader = new HealthTrackerFileUpload(ds);
	}
	
	@Test
	public void testFitBitUpload() {
		try {
			// get test file to parse
			String path = "/testing-files/sample_healthtracker/fitbit-test-data.csv";
			File file = new File(System.getProperty("user.dir") + path);
			Scanner scan = new Scanner(file);
			
			// parse file and create beans
			CSVParser parser = new CSVParser(scan);			
			uploader.setCsvData(parser.getData());
			uploader.createBeansFitBit();
			
			// check a few beans
			ArrayList<HealthTrackerBean> list = uploader.getBeanList();
			
			HealthTrackerBean b7 = list.get(7);
			assertEquals(2293, b7.getCalories());
			assertEquals(8246, b7.getSteps());
			assertEquals(3.66, b7.getDistance(), 0);
			assertEquals(22, b7.getFloors());
			assertEquals(1015, b7.getMinutesSedentary());
			assertEquals(425, b7.getMinutesLightlyActive());
			assertEquals(0, b7.getMinutesFairlyActive());
			assertEquals(0, b7.getMinutesVeryActive());
			assertEquals(1234, b7.getActivityCalories());
			
			HealthTrackerBean b30 = list.get(30);
			assertEquals(7137, b30.getSteps());
			assertEquals(16, b30.getFloors());
			assertEquals(3.17, b30.getDistance(), 0);
			
			assertEquals(6833, list.get(2).getSteps());
			assertEquals(1.72, list.get(6).getDistance(), 0);
			assertEquals(9, list.get(16).getMinutesFairlyActive());
			
		} catch (FileNotFoundException | CSVFormatException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testMSBandUpload() {
		try {			
			// get test file to parse
			String path = "/testing-files/sample_healthtracker/msband-test-data.csv";
			File file = new File(System.getProperty("user.dir") + path);
			Scanner scan = new Scanner(file);
			
			// parse file and create beans
			CSVParser parser = new CSVParser(scan);			
			uploader.setCsvData(parser.getData());
			uploader.createBeansMSBand();
			
			// check a few beans
			ArrayList<HealthTrackerBean> list = uploader.getBeanList();	
			
			HealthTrackerBean b2 = list.get(2);
			assertEquals(7030, b2.getSteps());
			assertEquals(2309, b2.getCalories());
			assertEquals(47, b2.getHeartrateLow());
			assertEquals(116, b2.getHeartrateHigh());
			assertEquals(71, b2.getHeartrateAverage());
			assertEquals(3.47, b2.getDistance(), 0.01);
			assertEquals(4, b2.getActiveHours());
			assertEquals(8, b2.getFloors());
			assertEquals(0, b2.getUvExposure());
						
			HealthTrackerBean b4 = list.get(4);
			assertEquals(2198, b4.getCalories());
			assertEquals(0, b4.getHeartrateLow());
			assertEquals(0, b4.getHeartrateAverage());
			assertEquals(0, b4.getHeartrateHigh());
			assertEquals(0, b4.getDistance(), 0);
			
			assertEquals(11967, list.get(0).getSteps());
			assertEquals(0, list.get(0).getMinutesFairlyActive());			
			assertEquals(1048, list.get(6).getSteps());
			assertEquals(0.515, list.get(6).getDistance(), 0.01);
			
		} catch (FileNotFoundException | CSVFormatException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testText() {
		uploader.setText("hello there");
		assertEquals("hello there", uploader.getText());
	}

}
