package edu.ncsu.csc.itrust.controller.obgyn;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.diagnosis.Diagnosis;
import edu.ncsu.csc.itrust.model.diagnosis.DiagnosisMySQL;
import edu.ncsu.csc.itrust.model.icdcode.ICDCode;
import edu.ncsu.csc.itrust.model.icdcode.ICDCodeMySQL;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsVisit;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsVisitMySQL;
import edu.ncsu.csc.itrust.model.obgyn.Pregnancy;
import edu.ncsu.csc.itrust.model.obgyn.PregnancyFlag;
import edu.ncsu.csc.itrust.model.obgyn.PregnancyMySQL;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;
import edu.ncsu.csc.itrust.model.old.beans.AllergyBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AllergyDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name="report_controller")
public class LaborReportController extends iTrustController {

	/** OBGYN databases */
	private ObstetricsInitMySQL initsDB;
	private PregnancyMySQL pregnancyDB;
	private ObstetricsVisitMySQL obVisitsDB;
	private DiagnosisMySQL diagnosisDB;
	private ICDCodeMySQL icdcodeDB;
	private OfficeVisitMySQL officevisitDB;
	
	/** Patient databases */
	private PatientDAO patientDB;
	private AllergyDAO allergyDB;
	private DAOFactory factory;
	
	/** Other data */
	private long patientID;
	private SessionUtils sessionUtils;
	
	/**
	 * Constructs a LaborReportController
	 */
	public LaborReportController() throws DBException {
		super();
		sessionUtils = getSessionUtils();
		factory = DAOFactory.getProductionInstance();
		
		initsDB = new ObstetricsInitMySQL();
		pregnancyDB = new PregnancyMySQL();
		obVisitsDB = new ObstetricsVisitMySQL();
		diagnosisDB = new DiagnosisMySQL();
		icdcodeDB = new ICDCodeMySQL();
		officevisitDB = new OfficeVisitMySQL();
		
		this.setPatientData();
	}
	
	/**
	 * Constructs a LaborReportController with a Data Source
	 */
	public LaborReportController(DataSource ds, SessionUtils utils) throws DBException {
		super();
		sessionUtils = utils;
		factory = TestDAOFactory.getTestInstance();
		
		initsDB = new ObstetricsInitMySQL(ds);
		pregnancyDB = new PregnancyMySQL(ds);
		obVisitsDB = new ObstetricsVisitMySQL(ds);
		diagnosisDB = new DiagnosisMySQL(ds);
		icdcodeDB = new ICDCodeMySQL(ds);
		officevisitDB = new OfficeVisitMySQL(ds);
		
		this.setPatientData();
	}
	
	private void setPatientData() {
		patientID = sessionUtils.getCurrentPatientMIDLong();
		patientDB = factory.getPatientDAO();
		allergyDB = factory.getAllergyDAO();
	}
		
	// TODO handle and not just throw DBExceptions
	
	/**
	 * Returns data to display
	 */	
	public List<ObstetricsInit> getObInits() throws DBException {
		return initsDB.getByID(patientID);
	}
	
	public boolean reportAvailable() throws DBException {
		boolean isAvailable = true;
		PatientBean patient = patientDB.getPatient(patientID);
		if(!patient.isObgynEligible()){
			isAvailable = false;
		} else if (initsDB.getByID(patientID).isEmpty()){
			isAvailable = false;
		}
		return isAvailable;
	}
	
	public ObstetricsInit getCurrentInit() throws DBException {
		return initsDB.getByID(patientID).get(0);
	}
	
	public List<ObstetricsVisit> getObVisits() throws DBException {
		return obVisitsDB.getByID(patientID);
	}	
	
	public List<Pregnancy> getPregnancies() throws DBException {
		return pregnancyDB.getByID(patientID);
	}
	
	public List<AllergyBean> getAllergies() throws DBException {
		return allergyDB.getPregnancyRelatedAllergies(patientID);
	}
	
	public LocalDate getMostRecentEDD() throws DBException{
		ObstetricsInit recent = getCurrentInit();
		return recent.getEstimatedDueDate();
	}
	
	/**
	 * Returns the patient's blood type
	 */
	public String getBloodType() throws DBException {
		PatientBean patient = patientDB.getPatient(patientID);
		return patient.getBloodType().toString();
	}
	
	public List<ICDCode> getRelevantConditions() throws DBException {	
		List<OfficeVisit> visits = officevisitDB.getVisitsForPatient(patientID);
		List<ICDCode> diagnosiscodes = new ArrayList<ICDCode>(); 
		for(OfficeVisit v : visits){
			List<Diagnosis> diags = diagnosisDB.getAllDiagnosisByOfficeVisit(v.getVisitID());
			for (Diagnosis d : diags){
				ICDCode icd = null;
				try {
					icd = icdcodeDB.getByCode(d.getCode());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (icd.isChronic() || icd.isPregnancyRelated()){
					diagnosiscodes.add(icd);
				}
			}
			
		}
		
		return diagnosiscodes;
		
		//diagnosisDB
		//icdcodeDB
		//officevisitDB
	}
	
	public List<String> getPregnancyWarningFlagsForVisit(ObstetricsVisit obvisit) {
		List<String> flags = new ArrayList<String>();
		String vid = Long.toString(obvisit.getVisitId());
		if (obvisit.isRhFlag()){
			flags.add("Visit " + vid + " has RH Flag set!");
		}
		String bp = obvisit.getBloodPressure();
		String parts[] = bp.split("/");			
		if (Integer.parseInt(parts[0]) >= 140 || Integer.parseInt(parts[1]) >= 90){
			flags.add("Visit " + vid + " has High Blood Pressure");
		}
		if (obvisit.isLowLyingPlacenta()){
			flags.add("Visit " + vid + " has Low Lying Placenta");
		}
		if (obvisit.getFetalHeartRate() > 160 || obvisit.getFetalHeartRate() < 120){
			flags.add("Visit " + vid + " has Abnormal Fetal Heartrate");
		}
		if (obvisit.getAmount() > 1){
			flags.add("Visit " + vid + " has Multiples in pregnancy");
		}
		
		return flags;
	}
	
	public List<String> getAllPregnancyWarningFlags() throws DBException{
		List<ObstetricsInit> obInits = getObInits();
		PatientBean patient = patientDB.getPatient(patientID);
		List<ObstetricsVisit> obVisits = getObVisits();
		List<String> flags = new ArrayList<String>();
		
		double maxweight = 0;
		double minweight = 0;
		for(ObstetricsVisit v : obVisits){
			List<String> hold = getPregnancyWarningFlagsForVisit(v);
			for(String s: hold){
				flags.add(s);
			}
			double weight = v.getWeight();
			if(weight > maxweight){
				maxweight = weight;
			} 
			if (weight < minweight || minweight <= 0){
				minweight = weight;
			}
		}
		double weightdifference = maxweight - minweight;
		
		Instant dateOfBirth = patient.getDateOfBirth().toInstant();
		Instant expDueDate = obInits.get(0).getEstimatedDueDate().atStartOfDay().toInstant(ZoneOffset.UTC);		
		long ageSec = expDueDate.getEpochSecond() - dateOfBirth.getEpochSecond();
		long ageYears = ageSec / (60L * 60L * 24L * 365L);
		if (ageYears >= 35) {
			flags.add("Warning: Advanced Maternal Age");
		}
		
		if(!getRelevantConditions().isEmpty()){
			flags.add("Mother has a relevant pre-existing condition (listed Below)");
		}
		
		if(!getAllergies().isEmpty()){
			flags.add("There are relevant maternal allergies (listed Below)");
		}
		if(weightdifference > 35 || weightdifference < 15){
			flags.add("Atypical Weight Change:" + weightdifference + " pounds (between first and last OB Office visit)");
		}
		
		return flags;
	}
	
	public boolean getComplication(ObstetricsVisit visit){
		return !getPregnancyWarningFlagsForVisit(visit).isEmpty();
	}
	
	
	/**
	 * Logs the viewing of a report
	 */
	public void logReportView() {
		logTransaction(TransactionType.LABOR_DELIVERY_REPORT, "");
	}
	
}
