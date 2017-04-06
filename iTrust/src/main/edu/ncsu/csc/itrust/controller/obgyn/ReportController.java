package edu.ncsu.csc.itrust.controller.obgyn;

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
import edu.ncsu.csc.itrust.model.obgyn.PregnancyMySQL;
import edu.ncsu.csc.itrust.model.obgyn.UltrasoundMySQL;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

@ManagedBean(name="report_controller")
@SessionScoped
public class ReportController extends iTrustController {

	/** OBGYN databases */
	private ObstetricsInitMySQL inits;
	private PregnancyMySQL pregnancies;
	private ObstetricsVisitMySQL visits;
	private UltrasoundMySQL ultrasounds;
	private ChildbirthMySQL babies;
	private ChildbirthVisitMySQL cbVisits;
	
	/** Other databases */
	private PatientDAO patients;
	
	/** Other stuff */
	private long patientID;
	
	public ReportController() throws DBException {
		super();
		inits = new ObstetricsInitMySQL();
		pregnancies = new PregnancyMySQL();
		visits = new ObstetricsVisitMySQL();
		ultrasounds = new UltrasoundMySQL();
		babies = new ChildbirthMySQL();
		cbVisits = new ChildbirthVisitMySQL();
		patientID = getSessionUtils().getCurrentPatientMIDLong().longValue();
		patients = DAOFactory.getProductionInstance().getPatientDAO();
	}
	
	public ReportController(DataSource ds) throws DBException {
		super();
		inits = new ObstetricsInitMySQL(ds);
		pregnancies = new PregnancyMySQL(ds);
		visits = new ObstetricsVisitMySQL(ds);
		ultrasounds = new UltrasoundMySQL(ds);
		babies = new ChildbirthMySQL(ds);
		cbVisits = new ChildbirthVisitMySQL(ds);
		// need to set patientID
		// need to initialize PatientDAO
	}
	
	public List<Pregnancy> getAllPregnancies() {
		try {
			return pregnancies.getByID(patientID);
		} catch (DBException e) {
			// display error
			return new ArrayList<Pregnancy>();
		}
	}
	
	// get current initialization
	// get delivery type
	
	public ObstetricsInit getCurrentInitialization() {
		try {
			return inits.getByID(patientID).get(0);
		} catch (DBException e) {
			// display error
			return null;
		}
	}
	
	public String getBloodType() {
		try {
			PatientBean patient = patients.getPatient(patientID);
			return patient.getBloodType().toString();
		} catch (DBException e) {
			// display error
			return null;
		}
	}
	
	// get patient DAO
	// get blood type
	
	public List<ObstetricsVisit> getAllVisits() {
		try {
			return visits.getByID(patientID);
		} catch (DBException e) {
			// display error
			return new ArrayList<ObstetricsVisit>();
		}
	}
	
	public void logReportView() {
		logTransaction(TransactionType.LABOR_DELIVERY_REPORT, "");
	}
	
}
