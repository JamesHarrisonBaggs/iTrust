package edu.ncsu.csc.itrust.controller.obGyn;

import java.util.List;
import java.sql.Timestamp;
import java.time.LocalDate;

import javax.faces.bean.ManagedBean;
import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obGyn.ObstetricsInitBean;
import edu.ncsu.csc.itrust.model.obGyn.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.obGyn.PregnancyBean;
import edu.ncsu.csc.itrust.model.obGyn.PregnancyMySQL;
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

	private List<ObstetricsInitBean> obstetricsList;
	private List<PregnancyBean> pregnancyList;
	
	private boolean obGyn;
	private boolean eligible;

	private String viewDate;
	private Long mid;
	private Long hcpid;
	private String currentDate;
	private ObstetricsInitBean obData;

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
		setObData(new ObstetricsInitBean(true));
		
	}

	/**
	 * @return all obstetrics records for the current patient
	 * @throws DBException
	 */
	public List<ObstetricsInitBean> getObstetricsList() throws DBException {
		logTransaction(TransactionType.VIEW_OBSTETRIC_RECORD, "");
		Long id = getSessionUtils().getCurrentPatientMIDLong();
		return initDB.getByID(id);
	}

	/**
	 * @return the obstetrics record for the current patient for the given date
	 * @throws DBException
	 */
	public List<ObstetricsInitBean> getObstetricsRecordByDate(LocalDate date) throws DBException {
		logTransaction(TransactionType.VIEW_OBSTETRIC_RECORD, "");
		long id = getSessionUtils().getCurrentPatientMIDLong();
		return initDB.getByDate(id, Timestamp.valueOf(date.atStartOfDay()));
	}
	
	/**
	 * @return all pregnancies for the current patient
	 * @throws DBException
	 */
	public List<PregnancyBean> getPregnancyList() throws DBException {
		long id = getSessionUtils().getCurrentPatientMIDLong();
		return pregDB.getByID(id);
	}
	
	/**
	 * @return the pregnancy for the current patient for the given date
	 * @throws DBException
	 */
	public List<PregnancyBean> getPregnancyByDate(LocalDate date) throws DBException {
		long id = getSessionUtils().getCurrentPatientMIDLong();
		return pregDB.getByDate(id, Timestamp.valueOf(date.atStartOfDay()));
	}

	/**
	 * Add or update the obstetric record specified in the given bean
	 * @throws DBException
	 */
	public void updateObstetricRecord(ObstetricsInitBean bean) throws DBException {
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
	public void updatePregnancy(PregnancyBean bean) throws DBException {
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
		setObData(new ObstetricsInitBean(true));
	}
	
	public void submitNewDate() {
		LocalDate l = LocalDate.parse(viewDate);
		obData.setEstimatedDueDate(l);
	}
	
	/** GETTERS AND SETTERS **/

	public void setObGyn(Long mid) throws DBException {
		if (mid != null) {
			PersonnelBean p = factory.getPersonnelDAO().getPersonnel(mid);
			if (p.getSpecialty().equals("ob/gyn") || p.getSpecialty().equals("ob/gyn")) {
				obGyn = true;
			} else {
				obGyn = false;
			}
		}
	}
	
	public boolean getObGyn() {
		return obGyn;
	}

	public List<ObstetricsInitBean> getObGynList() {
		return obstetricsList;
	}

	public List<PregnancyBean> getPriorPregList() {
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

	public ObstetricsInitBean getObData() {
		if(obData == null) {
			return new ObstetricsInitBean(true);
		}
		return obData;
	}

	public void setObData(ObstetricsInitBean obData) {
		this.obData = obData;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
}