package edu.ncsu.csc.itrust.controller.obgyn;

import java.util.List;
import java.time.LocalDate;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.user.patient.Patient;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "obgyn_controller")
@SessionScoped
public class ObstetricsInitController extends iTrustController {

	private ObstetricsInitMySQL sql;
	private ObstetricsInit newInit;
	private boolean added;
	
	private DAOFactory factory;
	private PatientDAO patientDB;
	
	private SessionUtils utils;
	
	private boolean obGyn;
	private boolean eligible;
	
	// TODO should handle exceptions and not just throw them

	/**
	 * Constructs an ObstetricsInitController
	 */
	public ObstetricsInitController() throws DBException {
		super();
		this.utils = getSessionUtils();
		this.factory = DAOFactory.getProductionInstance();
		this.sql = new ObstetricsInitMySQL();
		this.setUpInitialization();
	}
	/**
	 * Constructs an ObstetricsInitController with a data source
	 */
	public ObstetricsInitController(DataSource ds, SessionUtils utils, DAOFactory factory) throws DBException {
		super();
		this.utils = utils;
		this.factory = factory;
		this.sql = new ObstetricsInitMySQL(ds);
		this.setUpInitialization();
	}
	
	/**
	 * Called from both constructors to set up class
	 */
	private void setUpInitialization() throws DBException {
		// set up patient database
		this.patientDB = factory.getPatientDAO();
		
		// determine if current HCP is an OB/GYN
		setObGyn(utils.getSessionLoggedInMIDLong());
		
		// determine if patient is OB/GYN eligible
		Long mid = utils.getCurrentPatientMIDLong();
		if (mid != null) {		
			PatientBean p = patientDB.getPatient(mid.longValue());
			setEligible(p.isObgynEligible());
		}
		
		// set up submission bean
		newInit = new ObstetricsInit();
		added = false;
	}
	
	/**
	 * Return all obstetrics initialization records for a specific patient
	 */
	public List<ObstetricsInit> getInitializations(long mid) throws DBException {
		return sql.getByID(mid);		
	}
		
	/**
	 * Return all obstetrics initialization records for the current patient
	 */
	public List<ObstetricsInit> getObstetricsRecords() throws DBException {
		long mid = utils.getCurrentPatientMIDLong();
		return sql.getByID(mid);
	}

	/**
	 * Return the obstetrics record for the current patient for the given date
	 */
	public List<ObstetricsInit> getObstetricsRecordByDate(LocalDate date) throws DBException {
		long mid = utils.getCurrentPatientMIDLong();
		return sql.getByDate(mid, date);
	}
	
	/**
	 * Return the most recent obstetrics initialization record
	 */
	public ObstetricsInit getCurrentInitialization() throws DBException {
		long mid = utils.getCurrentPatientMIDLong();
		return sql.getByID(mid).get(0);
	}

	/**
	 * Create or update the obstetric record specified in the given bean
	 */
	public void update(ObstetricsInit bean) throws DBException {
		long mid = utils.getCurrentPatientMIDLong();
		bean.setPatientId(mid);
		try {
			int result = sql.update(bean);
			logTransaction(result != 2 ? TransactionType.CREATE_OBSTETRIC_RECORD : TransactionType.UPDATE_OBSTETRIC_RECORD, "");
			String msg = (result != 2) ? "Created obstetrics initialization" : "Updated obstetrics initialization"; 
			printFacesMessage(FacesMessage.SEVERITY_INFO, msg, msg, null);
		} catch (FormValidationException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage(), null);
		}
	}
	
	public void logView() {
		logTransaction(TransactionType.VIEW_OBSTETRIC_RECORD, "");
	}
	
	/** New Initialization */
	
	public void submitInit() throws DBException {
		newInit.setInitDate(LocalDate.now());
		this.update(newInit);
		added = true;
	}
	
	public ObstetricsInit getNewInit() {
		if (newInit == null) {
			newInit = new ObstetricsInit();
		}
		return newInit;
	}

	public void setNewInit(ObstetricsInit newInit) {
		this.newInit = newInit;
	}
	
	public boolean isAdded() {
		return this.added;
	}
	
	public void setAdded(boolean added) {
		this.added = added;
	}
	
	/** Eligibility */
	
	public void submitEligible() throws DBException {
		PatientBean p = patientDB.getPatient(utils.getCurrentPatientMIDLong().longValue());
		p.setObgynEligible(true);
		patientDB.editPatient(p, utils.getSessionLoggedInMIDLong().longValue());
		setEligible(p.isObgynEligible());
	}
	
	public void submitNotEligible() throws DBException {
		PatientBean p = patientDB.getPatient(utils.getCurrentPatientMIDLong().longValue());
		p.setObgynEligible(false);
		patientDB.editPatient(p, utils.getSessionLoggedInMIDLong().longValue());
		setEligible(p.isObgynEligible());
	}
	
	public boolean isEligible() {
		return eligible;
	}

	public void setEligible(boolean eligible) {
		this.eligible = eligible;
	}
	
	public boolean getObGyn() {
		return obGyn;
	}
	
	public void setObGyn(Long hcpid) throws DBException {
		if (hcpid != null) {
			PersonnelBean p = factory.getPersonnelDAO().getPersonnel(hcpid);
			if (p != null) {
				String specialty = p.getSpecialty(); // may be null
				obGyn = "obgyn".equalsIgnoreCase(specialty) || "ob/gyn".equalsIgnoreCase(specialty);				
			}
		}
	}

	public boolean isBothObGynEligible() {
		return (obGyn && eligible);
	}
	
}