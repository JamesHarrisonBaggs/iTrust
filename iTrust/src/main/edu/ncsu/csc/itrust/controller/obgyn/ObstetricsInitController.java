package edu.ncsu.csc.itrust.controller.obgyn;

import java.util.List;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
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

@ManagedBean(name = "obgyn_controller")
public class ObstetricsInitController extends iTrustController {

	private ObstetricsInitMySQL sql;
	private List<ObstetricsInit> list;
	
	private ObstetricsInit newInit;
	private boolean updatedInit;
	
	private DAOFactory factory;
	private PatientDAO patientDB;

	private Long mid;
	private Long hcpid;
	private String viewDate;
	
	private boolean obGyn;
	private boolean eligible;
		
	// TODO should handle exceptions and not just throw them

	public ObstetricsInitController() throws DBException {
		super();

		// get databases
		this.sql = new ObstetricsInitMySQL();
		this.factory = DAOFactory.getProductionInstance();
		this.patientDB = factory.getPatientDAO();
		
		// get list of patient obstetrics records
		list = getObstetricsRecords();
		// value="Date Range: #{obgyn_controller.list.get(0).getLastMenstrualPeriod().getMonthValue()}/#{obgyn_controller.list.get(0).getLastMenstrualPeriod().getDayOfMonth()}/#{obgyn_controller.obstetricsList.get(0).getLastMenstrualPeriod().getYear()} - #{obs_visit_controller.visitDate.getMonthValue()}/#{obs_visit_controller.visitDate.getDayOfMonth()}/#{obs_visit_controller.visitDate.getYear()}" />
		
		// determine if current HCP is an OB/GYN
		hcpid = getSessionUtils().getSessionLoggedInMIDLong();
		setObGyn(hcpid);
		
		// determine if patient is OB/GYN eligible
		mid = getSessionUtils().getCurrentPatientMIDLong();
		if (mid != null)
			setEligible(patientDB.getPatient(mid).isObgynEligible());
				
		// set up submission bean
		newInit = new ObstetricsInit();
		updatedInit = false;

	}
	
	// broken for now
	public ObstetricsInitController(DataSource ds) {
		super();
		this.sql = new ObstetricsInitMySQL(ds);
		this.patientDB = DAOFactory.getProductionInstance().getPatientDAO();
	}
		
	/**
	 * Return all obstetrics initialization records for the current patient
	 */
	public List<ObstetricsInit> getObstetricsRecords() throws DBException {
		logTransaction(TransactionType.VIEW_OBSTETRIC_RECORD, "");
		Long id = getSessionUtils().getCurrentPatientMIDLong();
		return sql.getByID(id);
	}

	/**
	 * Return the obstetrics record for the current patient for the given date
	 */
	public List<ObstetricsInit> getObstetricsRecordByDate(LocalDate date) throws DBException {
		logTransaction(TransactionType.VIEW_OBSTETRIC_RECORD, "");
		long id = getSessionUtils().getCurrentPatientMIDLong();
		return sql.getByDate(id, Timestamp.valueOf(date.atStartOfDay()));
	}

	/**
	 * Create or update the obstetric record specified in the given bean
	 */
	public void update(ObstetricsInit bean) throws DBException {
		bean.setPatientId(getSessionUtils().getCurrentPatientMIDLong());
		try {
			int result = sql.update(bean);
			logTransaction(result != 2 ? TransactionType.CREATE_OBSTETRIC_RECORD : TransactionType.UPDATE_OBSTETRIC_RECORD, "");
			String msg = (result != 2) ? "Created obstetrics initialization" : "Updated obstetrics initialization"; 
			printFacesMessage(FacesMessage.SEVERITY_INFO, msg, msg, null);
		} catch (FormValidationException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage(), null);
		}
	}
	
	public List<ObstetricsInit> getList() {
		return list;
	}
	
	/** New Initialization */
	
	public void submitInit() throws DBException {
		newInit.setInitDate(LocalDate.now());
		this.update(newInit);
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

	public boolean getUpdatedInit(){
		return updatedInit;
	}
	
	public void setUpdatedInit(boolean updatedInit) {
		this.updatedInit = updatedInit;
	}
	
	/** Other Methods */
	
	
	/** Eligibility */
	
	public void submitEligible() throws DBException {
		PatientBean p = patientDB.getPatient(mid);
		p.setObgynEligible(true);
		patientDB.editPatient(p, hcpid);
		setEligible(p.isObgynEligible());
	}

	public void submitNotEligible() throws DBException {
		PatientBean p = patientDB.getPatient(mid);
		p.setObgynEligible(false);
		patientDB.editPatient(p, hcpid);
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
	
	public void setObGyn(Long mid) throws DBException {
		if (mid != null) {
			PersonnelBean p = factory.getPersonnelDAO().getPersonnel(mid);
			String specialty = p.getSpecialty(); // may be null
			obGyn = "obgyn".equalsIgnoreCase(specialty) || "ob/gyn".equalsIgnoreCase(specialty);
		}
	}

	public boolean isBothObGynEligible() {
		return (obGyn && eligible);
	}
	
}