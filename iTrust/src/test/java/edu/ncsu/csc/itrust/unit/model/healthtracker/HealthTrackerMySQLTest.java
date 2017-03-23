package edu.ncsu.csc.itrust.unit.model.healthtracker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.diagnosis.Diagnosis;
import edu.ncsu.csc.itrust.model.emergencyRecord.EmergencyRecord;
import edu.ncsu.csc.itrust.model.emergencyRecord.EmergencyRecordMySQL;
import edu.ncsu.csc.itrust.model.healthtracker.HealthTrackerBean;
import edu.ncsu.csc.itrust.model.healthtracker.HealthTrackerMySQL;
import edu.ncsu.csc.itrust.model.immunization.Immunization;
import edu.ncsu.csc.itrust.model.old.beans.AllergyBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AllergyDAO;
import edu.ncsu.csc.itrust.model.old.enums.Gender;
import edu.ncsu.csc.itrust.model.prescription.Prescription;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;



public class HealthTrackerMySQLTest extends TestCase {

	private DataSource ds;
	private TestDataGenerator gen;
	
	private HealthTrackerBean hb;
	private HealthTrackerMySQL htms;
	private DAOFactory factory;
	private ArrayList<HealthTrackerBean> alhtb;
	
	public void setUp() throws DBException, FileNotFoundException, SQLException, IOException {
		this.ds = ConverterDAO.getDataSource();
		this.gen = new TestDataGenerator();
		
		factory = DAOFactory.getProductionInstance();
		htms = new HealthTrackerMySQL(factory);
		
	}
	
	public void testGetAll(){
		HealthTrackerBean bean = new HealthTrackerBean();
		bean.setDateString("5/6/2016");
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
		
		try {
			htms.updateData(bean);
		} catch (DBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FormValidationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		List<HealthTrackerBean> beans;
		try {
			beans = (ArrayList<HealthTrackerBean>) htms.getAll();
			System.out.println(beans.get(0).getPatientId());
			System.out.println(beans.get(1).getPatientId());
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
