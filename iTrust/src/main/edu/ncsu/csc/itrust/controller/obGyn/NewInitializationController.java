package edu.ncsu.csc.itrust.controller.obGyn;

import java.time.LocalDate;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.model.obGyn.ObstetricsInitBean;
import edu.ncsu.csc.itrust.model.obGyn.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.obGyn.PregnancyMySQL;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;

@ManagedBean(name = "newinit_controller")
@SessionScoped
public class NewInitializationController extends iTrustController {
	private DAOFactory factory;
	private ObstetricsInitMySQL initDB;
	private PregnancyMySQL pregDB;
	private PatientDAO patientDB;
	
	private LocalDate newInitDate;
	private ObstetricsInitBean obData;
	private Object hcpid;
	
	public NewInitializationController() {
		super();
		this.factory = DAOFactory.getProductionInstance();
		this.initDB = factory.getObstetricsInitDAO();
		this.pregDB = factory.getPregnanciesDAO();
		this.patientDB = factory.getPatientDAO();

		setHcpid(getSessionUtils().getSessionLoggedInMIDLong());
		setObData(new ObstetricsInitBean(true));
		newInitDate = obData.getLastMenstrualPeriod();
//		if(getNewInitDate() != null) {
//			getObData().setEstimatedDueDate(getNewInitDate());
//		}
	}
	public void submitNewDate() {
		obData.setEstimatedDueDate(newInitDate);
	}
	public LocalDate getNewInitDate() {
		return newInitDate;
	}
	public void setNewInitDate(LocalDate newInitDate) {
		this.newInitDate = newInitDate;
	}
	public Object getHcpid() {
		return hcpid;
	}
	public void setHcpid(Object hcpid) {
		this.hcpid = hcpid;
	}
	public ObstetricsInitBean getObData() {
		return obData;
	}
	public void setObData(ObstetricsInitBean obData) {
		this.obData = obData;
	}
}
