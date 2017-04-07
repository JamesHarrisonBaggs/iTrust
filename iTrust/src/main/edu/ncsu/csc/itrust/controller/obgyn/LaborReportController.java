package edu.ncsu.csc.itrust.controller.obgyn;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.obgyn.ChildbirthMySQL;
import edu.ncsu.csc.itrust.model.obgyn.ChildbirthVisitMySQL;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsVisit;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsVisitMySQL;
import edu.ncsu.csc.itrust.model.obgyn.Pregnancy;
import edu.ncsu.csc.itrust.model.obgyn.PregnancyFlag;
import edu.ncsu.csc.itrust.model.obgyn.PregnancyMySQL;
import edu.ncsu.csc.itrust.model.obgyn.UltrasoundMySQL;
import edu.ncsu.csc.itrust.model.old.beans.AllergyBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AllergyDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

@ManagedBean(name="report_controller")
@SessionScoped
public class LaborReportController extends iTrustController {

	/** OBGYN databases */
	private ObstetricsInitMySQL initsDB;
	private PregnancyMySQL pregnancyDB;
	private ObstetricsVisitMySQL obVisitsDB;
//	private UltrasoundMySQL ultrasoundsDB;
//	private ChildbirthMySQL childbirthsDB;
//	private ChildbirthVisitMySQL cbVisitsDB;
	
	/** OBGYN data */
	private ObstetricsInit currentInit;
	private List<ObstetricsVisit> obVisits;
	private List<Pregnancy> pregnancies;
	
	/** Patient databases */
	private PatientDAO patientDB;
	private AllergyDAO allergyDB;
	
	/** Patient data */
	private long patientID;
	private PatientBean patientBean;
	private List<AllergyBean> allergies;
	
	/**
	 * Constructs a LaborReportController
	 */
	public LaborReportController() throws DBException {
		super();
		
		// OBGYN databases
		initsDB = new ObstetricsInitMySQL();
		pregnancyDB = new PregnancyMySQL();
		obVisitsDB = new ObstetricsVisitMySQL();
//		ultrasoundsDB = new UltrasoundMySQL();
//		childbirthsDB = new ChildbirthMySQL();
//		cbVisitsDB = new ChildbirthVisitMySQL();

		// patient databases
		patientDB = DAOFactory.getProductionInstance().getPatientDAO();
		allergyDB = DAOFactory.getProductionInstance().getAllergyDAO();

		// patient data
		patientID = getSessionUtils().getCurrentPatientMIDLong().longValue();
		patientBean = patientDB.getPatient(patientID);
		allergies = allergyDB.getAllergies(patientID);
		
		// OBGYN data
		pregnancies = pregnancyDB.getByID(patientID);
		obVisits = obVisitsDB.getByID(patientID);
		currentInit = initsDB.getByID(patientID).get(0);

	}
	
	/**
	 * Constructs a LaborReportController with a Data Source
	 */
	public LaborReportController(DataSource ds) throws DBException {
// 		TODO INCOMPLETE STILL NEED TO DO A LOT OF STUFF
//		super();
//		initsDB = new ObstetricsInitMySQL(ds);
//		pregnancyDB = new PregnancyMySQL(ds);
//		obVisitsDB = new ObstetricsVisitMySQL(ds);
//		ultrasoundsDB = new UltrasoundMySQL(ds);
//		childbirthsDB = new ChildbirthMySQL(ds);
//		cbVisitsDB = new ChildbirthVisitMySQL(ds);
	}
		
	/**
	 * Returns data to display
	 */	
	public ObstetricsInit getCurrentInit() {
		return currentInit;
	}
	public List<ObstetricsVisit> getObVisits() {
		return obVisits;
	}	
	public List<Pregnancy> getPregnancies() {
		return pregnancies;
	}
	public List<AllergyBean> getAllergies() {
		return allergies;
	}
	
	/**
	 * Returns the patient's blood type
	 */
	public String getBloodType() {
		return patientBean.getBloodType().toString();
	}
	
	/**
	 * Returns a list of all Pregnancy Warning Flags
	 */
	public List<PregnancyFlag>getPregnancyWarningFlags() {
		
		// all warning flags
		boolean rhFlag = false;
		boolean highBP = false;
		boolean advancedAge = false;
		boolean preExisting = false;
		boolean allergies = false;
		boolean lowLying = false;
		boolean genMiscarriage = false;
		boolean abnormalFHR = false;
		boolean multiples = false;
		boolean atypicalWeight = false;
		boolean hGravidarum = false;
		boolean hypothyroidism = false;
		
		// get flags from visit
		for (ObstetricsVisit v : obVisits) {
			if (v.isRhFlag()) rhFlag = true;
			// high blood pressure?
			if (v.isLowLyingPlacenta()) lowLying = true;
			int fhr = v.getFetalHeartRate();
			if (fhr < 120 || fhr > 160) abnormalFHR = true;
			if (v.getAmount() > 1) multiples = true;
		}
		
		// advanced age
		Instant dateOfBirth = patientBean.getDateOfBirth().toInstant();
		Instant expDueDate = currentInit.getEstimatedDueDate().atStartOfDay().toInstant(ZoneOffset.UTC);		
		long ageSec = expDueDate.getEpochSecond() - dateOfBirth.getEpochSecond();
		long ageYears = ageSec / (60L * 60L * 24L * 365L);
		if (ageYears >= 35) {
			advancedAge = true;
		}
		
		// TODO pre existing?
		// TODO allergies?
		// TODO genetic miscarriage?
		// TODO atypical weight?
		// TODO hyperemesis gravidarum?
		// TODO hypothyroidism?

		// assemble flag list
		List<PregnancyFlag> flags = new ArrayList<PregnancyFlag>();
		flags.add(new PregnancyFlag(rhFlag, "RH factor"));
		flags.add(new PregnancyFlag(highBP, "High Blood Pressure"));
		flags.add(new PregnancyFlag(advancedAge, "Advanced Maternal Age"));
		flags.add(new PregnancyFlag(lowLying, "Low-Lying Placenta"));
		flags.add(new PregnancyFlag(multiples, "Multiples"));
		flags.add(new PregnancyFlag(abnormalFHR, "Abnormal Fetal Heart Rate"));
		
		return flags;
	}
		
	// TODO get pre-existing conditions???
	
	/**
	 * Logs the viewing of a report
	 */
	public void logReportView() {
		logTransaction(TransactionType.LABOR_DELIVERY_REPORT, "");
	}
	
}
