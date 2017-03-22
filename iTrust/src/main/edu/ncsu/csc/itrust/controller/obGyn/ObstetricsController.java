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
	private boolean obGyn = false;
	private boolean eligible;
	
	private String viewDate;
	private Long mid;
	private Long hcpid;

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
		setObGyn(getSessionUtils().getSessionLoggedInMIDLong());
		
		mid = getSessionUtils().getCurrentPatientMIDLong();
		hcpid = getSessionUtils().getSessionLoggedInMIDLong();
		
		if (mid != null) {
			eligible = patientDB.getPatient(mid).isObgynEligible();
		}
	}
	
	/** added by Eric **/
	
	public List<ObstetricsInitBean> getRecords() throws DBException {
		long id = getSessionUtils().getCurrentPatientMIDLong();
		return initDB.getByID(id);
	}
	
	public List<PregnancyBean> getPregnancies() throws DBException {
		long id = getSessionUtils().getCurrentPatientMIDLong();
		return pregDB.getByID(id);
	}
	
	public List<PregnancyBean> getPregnanciesByDate(LocalDate date) throws DBException {
		long id = getSessionUtils().getCurrentPatientMIDLong();
		Timestamp ts = Timestamp.valueOf(date.atStartOfDay());
		return pregDB.getByDate(id, ts);
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
	
	public void setObGyn(Long mid) throws DBException {
		PersonnelBean p = factory.getPersonnelDAO().getPersonnel(mid); 
		obGyn = p.getSpecialty().equals("ob/gyn");
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
		return viewDate;
	}

	public void setViewDate(String viewDate) {
		this.viewDate = viewDate;
	}
	
	public void logViewPIR() {
		// TODO Auto-generated method stud
	}
	
}