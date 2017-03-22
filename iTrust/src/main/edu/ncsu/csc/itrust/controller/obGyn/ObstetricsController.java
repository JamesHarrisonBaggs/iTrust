package edu.ncsu.csc.itrust.controller.obGyn;

import java.util.ArrayList;
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
	private boolean newObgynInit;

	private String viewDate;
	private Long mid;
	private Long hcpid;
	private ObstetricsInitBean obData;

	public ObstetricsController() throws DBException {
		super();

		// get databases
		this.factory = DAOFactory.getProductionInstance();
		this.initDB = factory.getObstetricsInitDAO();
		this.pregDB = factory.getPregnanciesDAO();
		this.patientDB = factory.getPatientDAO();

		obstetricsList = new ArrayList<ObstetricsInitBean>();
		setObGynList();

		pregnancyList = new ArrayList<PregnancyBean>();
		setPriorPregList();

		mid = getSessionUtils().getCurrentPatientMIDLong();
		hcpid = getSessionUtils().getSessionLoggedInMIDLong();
		if (hcpid != null) {
			setObGyn(hcpid);
		}
		if (mid != null) {
			setEligible(patientDB.getPatient(mid).isObgynEligible()); 
		}		
	}

	/** added by Eric **/

	public List<ObstetricsInitBean> getObstetricsList() throws DBException {
		List<ObstetricsInitBean> tmp = new ArrayList<ObstetricsInitBean>();
		Long id = getSessionUtils().getCurrentPatientMIDLong();
		if (id != null) {
			tmp = initDB.getByID(id);
		}
		return tmp;
	}

	public List<PregnancyBean> getPregnancyList() throws DBException {
		long id = getSessionUtils().getCurrentPatientMIDLong();
		return pregDB.getByID(id);
	}

	public List<PregnancyBean> getPregnanciesByDate(LocalDate date) throws DBException {
		long id = getSessionUtils().getCurrentPatientMIDLong();
		Timestamp ts = Timestamp.valueOf(date.atStartOfDay());
		return pregDB.getByDate(id, ts);
	}
	public ObstetricsInitBean getObstetricsInitBeanByDate(LocalDate date) throws DBException {
		long id = getSessionUtils().getCurrentPatientMIDLong();
		Timestamp ts = Timestamp.valueOf(date.atStartOfDay());
		return initDB.getByDate(id, ts);
	}

	// add or update
	public void updateRecord(ObstetricsInitBean bean) throws DBException {
		try {
			int result = initDB.update(bean);
			// TODO log
		} catch (FormValidationException e) {
			// TODO handle
		}
	}

	// add or update
	public void updatePregnancy(PregnancyBean bean) throws DBException {
		try {
			int result = pregDB.update(bean);
			// TODO log
		} catch (FormValidationException e) {
			// TODO handle
		}
	}

	public void submitCreate() {
		
	}
	public void submitClose() {
		setNewObgynInit(false);
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

	}
	public void submitNewObgynInit() {
		setObData(new ObstetricsInitBean(true));
		setNewObgynInit(true);
	}
	public void submitNewDate() {
		obData.getEstimatedDueDate();
		setNewObgynInit(true);
	}

	public void setObGyn(Long mid) throws DBException {
		if (mid != null) {
			PersonnelBean p = factory.getPersonnelDAO().getPersonnel(mid); 
			obGyn = p.getSpecialty().equals("ob/gyn");
		}
	}

	public boolean getObGyn() {
		return obGyn;
	}

	public List<ObstetricsInitBean> getObGynList() {
		return obstetricsList;
	}

	private void setObGynList() {
		// TODO Auto-generated method stub	
	}

	public List<PregnancyBean> getPriorPregList() {
		return pregnancyList;
	}

	private void setPriorPregList() {
		// TODO Auto-generated method stub
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

	public boolean isNewObgynInit() {
		return newObgynInit;
	}

	public void setNewObgynInit(boolean newObgynInit) {
		this.newObgynInit = newObgynInit;
	}

	public ObstetricsInitBean getObData() {
		return obData;
	}

	public void setObData(ObstetricsInitBean obData) {
		this.obData = obData;
	}
}