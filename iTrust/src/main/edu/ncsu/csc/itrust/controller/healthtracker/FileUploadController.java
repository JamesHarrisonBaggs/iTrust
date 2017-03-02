package edu.ncsu.csc.itrust.controller.healthtracker;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.Part;

import edu.ncsu.csc.itrust.model.healthtracker.HealthTrackerBean;
import edu.ncsu.csc.itrust.controller.healthtracker.HealthTrackerController;
import edu.ncsu.csc.itrust.CSVParser;
import edu.ncsu.csc.itrust.exception.CSVFormatException;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;

/**
 * Allows the upload of a CSV file of FitBit data to iTrust
 * CURRENTLY DOES NOT SUPPORT MSBAND DATA
 * 
 * @author erein
 *
 */
@ManagedBean(name = "fileUploadBean")
@RequestScoped
public class FileUploadController {
	
	private Part uploadedFile;
	private String fileText;
	
	private ArrayList<String> csvHeader;
	private ArrayList<ArrayList<String>> csvData;
	private ErrorList csvErrors;

	private ArrayList<HealthTrackerBean> beanList;
	private HealthTrackerController htcontrol;
		
	/**
	 * Constructs a File Upload Bean
	 */
	public FileUploadController() throws DBException {
		htcontrol = new HealthTrackerController();
	}
	
	/**
	 * Main flow to upload a file
	 */
	public void upload() {		
		// exit if file was not uploaded
		if (uploadedFile != null) {
			try {
				getFileText();
				parseData();
				// System.out.println("HEADER: " + csvHeader.toString());
				// System.out.println("DATA: " + csvData.toString());
				// System.out.println("ERRORS: " + csvErrors.toString());
				createBeans();
				htcontrol.uploadData(beanList);
			} catch (IOException | DBException | FormValidationException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}			
		}
	}
	
	/**
	 * Retrieves the plaintext of the uploaded file
	 * Assumes uploadedFile is not null
	 * @throw IOException if unable to scan plaintext
	 */
	private void getFileText() throws IOException {
		try (InputStream in = uploadedFile.getInputStream(); Scanner txt = new Scanner(in)){
			fileText = txt.useDelimiter("\\A").next();
		} catch (IOException e) {
			throw e;
		}
	}
	
	/**
	 * Parses the data from the file text
	 * Assumes fileText is not null
	 * @throw IOException if error occurs parsing text
	 */
	private void parseData() throws IOException {
		try (Scanner scan = new Scanner(fileText)) {
			CSVParser parser = new CSVParser(scan);
			csvData = parser.getData();
			csvHeader = parser.getHeader();
			csvErrors = parser.getErrors();
		} catch (CSVFormatException e) {
			throw new IOException(e.getMessage());
		}
		if (csvErrors != null && csvErrors.hasErrors()) {
			throw new IOException(csvErrors.toString());
		}
	}
	
	/**
	 * Assembles a beanList from the parsed data
	 * @param type - specifies the format of the data
	 */
	private void createBeans() {
		// determine file format
		if (csvHeader.get(0).equals("Activities")) {
			// System.out.println("Parsing as FitBit data:");
			createBeansFitBit();
		} else {
			// System.out.println("Parsing as MSBand data:");
			createBeansMSBand();
		}
	}
		
	/**
	 * Assembles beans from the FitBit CSV format
	 */
	private void createBeansFitBit() {
		beanList = new ArrayList<HealthTrackerBean>();

		// for each line of data, create a new bean
		// start i at 1 since row 0 is headers
		for (int i = 1; i < csvData.size(); i++) {
			ArrayList<String> line = csvData.get(i);
			HealthTrackerBean bean = new HealthTrackerBean();
			int j = 0;
			
			// get date
			SimpleDateFormat format = new SimpleDateFormat("M/dd/yy");
			try {
				Date date = format.parse(line.get(j++));
				LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				bean.setDate(localDate);
			} catch (Exception e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			
			// get calories
			try {
				bean.setCalories(NumberFormat.getNumberInstance(Locale.US).parse(line.get(j++)).intValue());
			} catch (java.text.ParseException e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			// get steps
			try {
				bean.setSteps(NumberFormat.getNumberInstance(Locale.US).parse(line.get(j++)).intValue());
			} catch (java.text.ParseException e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			// get distance
			try {
				bean.setDistance(NumberFormat.getNumberInstance(Locale.US).parse(line.get(j++)).doubleValue());
			} catch (java.text.ParseException e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			// get floors
			try {
				bean.setFloors(NumberFormat.getNumberInstance(Locale.US).parse(line.get(j++)).intValue());
			} catch (java.text.ParseException e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			// get minutes sedentary
			try {
				bean.setMinutesSedentary(NumberFormat.getNumberInstance(Locale.US).parse(line.get(j++)).intValue());
			} catch (java.text.ParseException e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			// get minutes lightly active
			try {
				bean.setMinutesLightlyActive(NumberFormat.getNumberInstance(Locale.US).parse(line.get(j++)).intValue());
			} catch (java.text.ParseException e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			// get minutes fairly active
			try {
				bean.setMinutesFairlyActive(NumberFormat.getNumberInstance(Locale.US).parse(line.get(j++)).intValue());
			} catch (java.text.ParseException e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			// get minutes very active
			try {
				bean.setMinutesVeryActive(NumberFormat.getNumberInstance(Locale.US).parse(line.get(j++)).intValue());
			} catch (java.text.ParseException e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			// get activity calories
			try {
				bean.setActivityCalories(NumberFormat.getNumberInstance(Locale.US).parse(line.get(j++)).intValue());
			} catch (java.text.ParseException e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			
			// add it to the list
			beanList.add(bean);

		}
	}
	
	/**
	 * Assembles beans from the MS Band CSV format
	 */
	private void createBeansMSBand() {
		beanList = new ArrayList<HealthTrackerBean>();

		// for each line of data, create a new bean
		for (int i = 0; i < csvData.size(); i++) {
			ArrayList<String> line = csvData.get(i);
			HealthTrackerBean bean = new HealthTrackerBean();
			int j = 0;
			
			// get Date
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
			try {
				Date date = format.parse(line.get(j++));
				LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				bean.setDate(localDate);
			} catch (Exception e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			// get Steps
			try {
				bean.setSteps(NumberFormat.getNumberInstance(Locale.US).parse(line.get(j++)).intValue());
			} catch (ParseException e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			// get Calories
			try {
				bean.setCalories(NumberFormat.getNumberInstance(Locale.US).parse(line.get(j++)).intValue());
			} catch (ParseException e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			// get HR_Lowest
			try {
				bean.setHeartrateLow(NumberFormat.getNumberInstance(Locale.US).parse(line.get(j++)).intValue());
			} catch (ParseException e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			// get HR_Highest
			try {
				bean.setHeartrateHigh(NumberFormat.getNumberInstance(Locale.US).parse(line.get(j++)).intValue());
			} catch (ParseException e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			// get HR_Average
			try {
				bean.setHeartrateAverage(NumberFormat.getNumberInstance(Locale.US).parse(line.get(j++)).intValue());
			} catch (ParseException e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			// get Total_Miles_Moved
			try {
				bean.setDistance(NumberFormat.getNumberInstance(Locale.US).parse(line.get(j++)).doubleValue());
			} catch (ParseException e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			// get Active_Hours
			try {
				bean.setSteps(NumberFormat.getNumberInstance(Locale.US).parse(line.get(j++)).intValue());
			} catch (ParseException e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			// get Floors_Climbed
			try {
				bean.setFloors(NumberFormat.getNumberInstance(Locale.US).parse(line.get(j++)).intValue());
			} catch (ParseException e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			// get UV_Exposure_Minutes
			try {
				bean.setUvExposure(NumberFormat.getNumberInstance(Locale.US).parse(line.get(j++)).intValue());
			} catch (ParseException e) {
				System.err.println("Parsing: " + e.getMessage());
			}
			
			// add bean to list
			beanList.add(bean);
		}
	}

	/** Getters and Setters */
	
	public Part getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(Part uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	
	public String getText() {
		return fileText;
	}
	
	public void setText(String text) {
		this.fileText = text;
	}
	
}