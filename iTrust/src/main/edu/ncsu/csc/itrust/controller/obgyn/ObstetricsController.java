package edu.ncsu.csc.itrust.controller.obgyn;

import java.util.List;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.faces.bean.ManagedBean;
import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.obgyn.Pregnancy;
import edu.ncsu.csc.itrust.model.obgyn.PregnancyMySQL;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

@ManagedBean(name = "obgyn_controller")
public class ObstetricsController extends iTrustController {

	private DAOFactory factory;
	private ObstetricsInitMySQL initDB;
	private PregnancyMySQL pregDB;
	private PatientDAO patientDB;

	private List<ObstetricsInit> obstetricsList;
	private List<Pregnancy> pregnancyList;
	
	private boolean obGyn;
	private boolean eligible;
	private boolean updatedInit;

	private String viewDate;
	private Long mid;
	private Long hcpid;
	private String currentDate;
	private String newInitDate;
	/**
	 * @return the newInitDate
	 */
	public String getNewInitDate() {
		return newInitDate;
	}

	/**
	 * @param newInitDate the newInitDate to set
	 */
	public void setNewInitDate(String newInitDate) {
		this.newInitDate = newInitDate;
	}

	private ObstetricsInit obData;

	public ObstetricsController() throws DBException {
		super();

		// get databases
		this.factory = DAOFactory.getProductionInstance();
		this.initDB = factory.getObstetricsInitDAO();
		this.pregDB = factory.getPregnanciesDAO();
		this.patientDB = factory.getPatientDAO();

		obstetricsList = getObstetricsList();
		pregnancyList = getPregnancyList();

		hcpid = getSessionUtils().getSessionLoggedInMIDLong();
		mid = getSessionUtils().getCurrentPatientMIDLong();
		
		if (hcpid != null) {
			setObGyn(hcpid);
		}
		if (mid != null) {
			setEligible(patientDB.getPatient(mid).isObgynEligible()); 
		}
		
		setCurrentDate(LocalDate.now().toString());
		setNewInitDate(currentDate);
		setObData(new ObstetricsInit(true));
		
	}
	
	public String getEstimatedDueDate(){
		return LocalDate.parse(newInitDate).plusDays(280).toString();
	}

	/**
	 * @return all obstetrics records for the current patient
	 * @throws DBException
	 */
	public List<ObstetricsInit> getObstetricsList() throws DBException {
		logTransaction(TransactionType.VIEW_OBSTETRIC_RECORD, "");
		Long id = getSessionUtils().getCurrentPatientMIDLong();
		return initDB.getByID(id);
	}

	/**
	 * @return the obstetrics record for the current patient for the given date
	 * @throws DBException
	 */
	public List<ObstetricsInit> getObstetricsRecordByDate(LocalDate date) throws DBException {
		logTransaction(TransactionType.VIEW_OBSTETRIC_RECORD, "");
		long id = getSessionUtils().getCurrentPatientMIDLong();
		return initDB.getByDate(id, Timestamp.valueOf(date.atStartOfDay()));
	}
	
	/**
	 * @return all pregnancies for the current patient
	 * @throws DBException
	 */
	public List<Pregnancy> getPregnancyList() throws DBException {
		long id = getSessionUtils().getCurrentPatientMIDLong();
		return pregDB.getByID(id);
	}
	
	/**
	 * @return the pregnancy for the current patient for the given date
	 * @throws DBException
	 */
	public List<Pregnancy> getPregnancyByDate(LocalDate date) throws DBException {
		long id = getSessionUtils().getCurrentPatientMIDLong();
		return pregDB.getByDate(id, Timestamp.valueOf(date.atStartOfDay()));
	}

	/**
	 * Add or update the obstetric record specified in the given bean
	 * @throws DBException
	 */
	public void updateObstetricRecord(ObstetricsInit bean) throws DBException {
		try {
			int result = initDB.update(bean);
			logTransaction(result != 2 ? TransactionType.CREATE_OBSTETRIC_RECORD : TransactionType.UPDATE_OBSTETRIC_RECORD, "");
		} catch (FormValidationException e) {
			// TODO handle
		}
	}

	/**
	 * Add or update the pregnancy specified in the given bean
	 * @throws DBException
	 */
	public void updatePregnancy(Pregnancy bean) throws DBException {
		bean.setPatientId(mid);
		try {
			pregDB.update(bean);
			// TODO log
		} catch (FormValidationException e) {
			// TODO handle
		}
	}

	public void submitCreate() {

	}
	
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

	public void submitViewDate() {
		// TODO
	}
	
	public void submitNewObgynInit() {
		setObData(new ObstetricsInit(true));
	}
	
	public String getWeeksPregnant(){
		int totDays = (int) ChronoUnit.DAYS.between(LocalDate.parse(newInitDate).atStartOfDay(), LocalDate.now().atStartOfDay());
		int days = totDays%7;
		int weeks = totDays/7;
		return (weeks + "weeks and " + days + " Days Pregnant!");
	}
	
	public void submitNewDate() throws DBException, FormValidationException {
		obData.setCurrent(true);
		obData.setEstimatedDueDate(LocalDate.parse(newInitDate).plusDays(280));
		obData.setPatientId(getSessionUtils().getCurrentPatientMIDLong());
		updatedInit = true;
		initDB.update(obData);
	}
	
	/** GETTERS AND SETTERS **/

	public void setObGyn(Long mid) throws DBException {
		if (mid != null) {
			PersonnelBean p = factory.getPersonnelDAO().getPersonnel(mid);
			if (p.getSpecialty().equals("OB/GYN") || p.getSpecialty().equals("ob/gyn")) {
				obGyn = true;
			} else {
				obGyn = false;
			}
		}
	}
	
	public boolean getObGyn() {
		return obGyn;
	}

	//TODO Finish this
	public boolean initDateExistsInDB(){
		return updatedInit;
	}
	public List<ObstetricsInit> getObGynList() {
		return obstetricsList;
	}

	public List<Pregnancy> getPriorPregList() {
		return pregnancyList;
	}

	public boolean isEligible() {
		return eligible;
	}

	public void setEligible(boolean eligible) {
		this.eligible = eligible;
	}

	public String getViewDate() {
		if (viewDate != null) {
			return viewDate;
		} else {
			return "-1";
		}
	}

	public void setViewDate(String viewDate) {
		this.viewDate = viewDate;
	}

	public void logViewPIR() {
		// TODO Auto-generated method stud
	}
	public boolean isBothObGynEligible() {
		return (obGyn && eligible);
	}

	public ObstetricsInit getObData() {
		if(obData == null) {
			return new ObstetricsInit(true);
		}
		return obData;
	}

	public void setObData(ObstetricsInit obData) {
		this.obData = obData;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
}