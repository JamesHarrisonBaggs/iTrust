package edu.ncsu.csc.itrust.controller.obGyn;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.obGyn.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "obgyn_controller")
public class ObstetricsController extends iTrustController {
	private DAOFactory factory;
	private ObstetricsInitMySQL databaseObInit;
	private List<ObstetricsInitBean> obGynList;
	private List<PregnancyBean> priorPregList;
	private boolean obGyn = false;
	private SessionUtils sessionUtils;
	private boolean eligible = false;
	private String viewDate;


	
	public ObstetricsController() throws DBException {
		super();
		sessionUtils = super.getSessionUtils();
		this.factory = DAOFactory.getProductionInstance();
		this.databaseObInit = factory.getObInitDataSQL();
		obGynList = new ArrayList<ObstetricsInitBean>();
		setObGynList();
		priorPregList = new ArrayList<PregnancyBean>();
		setPriorPregList();
		setObGyn(sessionUtils.getSessionLoggedInMIDLong());
	}
	public void submitCreate() {
		
	}
	public void submitNotEligible() {
		setEligible(false);
	}
	public void submitEligible() {
		setEligible(true);
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