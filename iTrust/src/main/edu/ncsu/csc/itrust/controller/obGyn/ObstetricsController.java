package edu.ncsu.csc.itrust.controller.obGyn;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.obGyn.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "obgyn_controller")
public class ObstetricsController extends iTrustController {
	private DAOFactory factory;
	private ObstetricsInitMySQL databaseObInit;
	private PatientDAO databasePatient;
	private List<ObstetricsInitBean> obGynList;
	private List<PregnancyBean> priorPregList;
	private boolean obGyn = false;
	private SessionUtils sessionUtils;
	private boolean eligible;
	private String viewDate;
	private Long mid;
	private Long hcpid;

	public ObstetricsController() throws DBException {
		super();
		sessionUtils = super.getSessionUtils();
		this.factory = DAOFactory.getProductionInstance();
		this.databasePatient = factory.getPatientDAO();
		this.databaseObInit = factory.getObstetricsInitDAO();
		obGynList = new ArrayList<ObstetricsInitBean>();
		setObGynList();
		priorPregList = new ArrayList<PregnancyBean>();
		setPriorPregList();
		setObGyn(sessionUtils.getSessionLoggedInMIDLong());
		mid = sessionUtils.getCurrentPatientMIDLong();
		hcpid = sessionUtils.getSessionLoggedInMIDLong();
		if (mid != null) {
			eligible = databasePatient.getPatient(mid).isObgynEligible();
		}
	}
	public void submitCreate() {
		
	}
	
	public void submitEligible() throws DBException {
		PatientBean p = databasePatient.getPatient(mid);
		p.setObgynEligible(true);
		databasePatient.editPatient(p, hcpid);
		setEligible(p.isObgynEligible());
	}
	public void submitNotEligible() throws DBException {
		PatientBean p = databasePatient.getPatient(mid);
		p.setObgynEligible(false);
		databasePatient.editPatient(p, hcpid);
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
		return obGynList;
	}
	private void setObGynList() {
		// TODO Auto-generated method stub
		
	}
	public List<PregnancyBean> getPriorPregList() {
		return priorPregList;
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
		//TODO Auto-generated method stud
	}
	
}