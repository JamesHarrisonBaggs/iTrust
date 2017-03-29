package edu.ncsu.csc.itrust.controller.obgyn;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsVisit;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsVisitMySQL;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

import javax.sql.DataSource;

@ManagedBean(name = "obs_visit_controller")
public class ObstetricsVisitController extends iTrustController {

	ObstetricsVisitMySQL sql;
	ObstetricsVisit ob;
	private long visitId;
	private long patientId;
	private LocalDateTime visitDate;
	private int weeksPregnant;
	private double weight;
	private String bloodPressure;
	private int fetalHeartRate;
	private int amount;
	private boolean lowLyingPlacenta;
	private boolean rhFlag;
	private ObstetricsController obc;

	private boolean eligible;
	private boolean obgyn;
	private boolean notice;

	private List<ObstetricsInit> obstetricsList;

	public ObstetricsVisitController() throws DBException {
		super();
		obc = new ObstetricsController();
		sql = new ObstetricsVisitMySQL();
		ob = getObstetricsVisit();
		if (getOfficeVisit().getVisitID() != 0) {
			ob.setVisitId(getOfficeVisit().getVisitID());
			ob.setVisitDate(getOfficeVisit().getDate());
		}
		try {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("officeVisitId", ob.getVisitId());
		} catch (NullPointerException e) {
			// Do nothing
		}
		visitId = ob.getVisitId();
		patientId = ob.getPatientId();
		if (patientId == 0) {
			patientId = Long.parseLong(
					(String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pid"));
		}
		visitDate = ob.getVisitDate();
		weeksPregnant = ob.getWeeksPregnant();
		weight = ob.getWeight();
		bloodPressure = ob.getBloodPressure();
		fetalHeartRate = ob.getFetalHeartRate();
		amount = ob.getAmount();
		lowLyingPlacenta = ob.isLowLyingPlacenta();
		rhFlag = ob.isRhFlag();
		setObgyn();
		setObstetricsList();
		setEligible();
		setNotice();
	}


	public ObstetricsVisitController(DataSource ds) throws DBException {
		super();
		sql = new ObstetricsVisitMySQL(ds);
	}

	public OfficeVisit getOfficeVisit() throws DBException {
		long id = getSessionUtils().getCurrentOfficeVisitId();
		return new OfficeVisitController().getVisitByID(id);
	}
	public ObstetricsVisit getObstetricsVisit() throws DBException {
		long id = getSessionUtils().getCurrentOfficeVisitId();
		return sql.getByVisit(id);
	}
	public void update(ObstetricsVisit bean) throws DBException {
		try {
			sql.update(bean);
			FacesContext.getCurrentInstance().addMessage("manage_obstetrics_formSuccess", new FacesMessage("Obstetrics Visit Updated Successfully"));
		} catch (FormValidationException e) {
			FacesContext.getCurrentInstance().addMessage("manage_obstetrics_formError", new FacesMessage(e.getMessage()));
		}
	}

	public void add() throws DBException, FormValidationException {

		// create bean
		ObstetricsVisit bean = new ObstetricsVisit();
		bean.setPatientId(getSessionUtils().getCurrentPatientMIDLong());
		bean.setVisitId(getSessionUtils().getCurrentOfficeVisitId());
		bean.setVisitDate(getOfficeVisit().getDate());
		// test data
		bean.setBloodPressure("120/90");
		bean.setWeeksPregnant(20);
		bean.setWeight(120.2);
		bean.setFetalHeartRate(60);
		bean.setAmount(2);
		bean.setLowLyingPlacenta(false);

		// update
		try {
			int result = sql.update(bean);
			printFacesMessage(FacesMessage.SEVERITY_INFO, "Visit added", "Visit added", "addBtn");
			logTransaction(result != 2 ? TransactionType.CREATE_OBSTETRIC_RECORD : TransactionType.UPDATE_OBSTETRIC_RECORD, "");
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to add visit", "Failed to add visit", "add");
		}

	}
	public void submit() throws DBException {
		ob.setPatientId(patientId);
		ob.setVisitDate(visitDate);
		ob.setVisitId(visitId);
		ob.setWeeksPregnant(weeksPregnant);
		ob.setWeight(weight);
		ob.setBloodPressure(bloodPressure);
		ob.setFetalHeartRate(fetalHeartRate);
		ob.setAmount(amount);
		ob.setLowLyingPlacenta(lowLyingPlacenta);
		ob.setRhFlag(rhFlag);
		ob.setLowLyingPlacenta(lowLyingPlacenta);
		update(ob);


	}

	public long getVisitId() {
		return visitId;
	}


	public long getPatientId() {
		return patientId;
	}


	public LocalDateTime getVisitDate() {
		return visitDate;
	}


	public int getWeeksPregnant() throws DBException {
		LocalDate lmp = obc.getObstetricsList().get(0).getLastMenstrualPeriod();
		LocalDate visitDateLD = visitDate.toLocalDate();
		long days = Math.abs(lmp.toEpochDay() - visitDateLD.toEpochDay());
		weeksPregnant = (int) (days/7);
		ob.setWeeksPregnant(weeksPregnant);
		return weeksPregnant;
	}


	public double getWeight() {
		return weight;
	}


	public String getBloodPressure() {
		return bloodPressure;
	}


	public int getFetalHeartRate() {
		return fetalHeartRate;
	}


	public int getAmount() {
		return amount;
	}


	public boolean isLowLyingPlacenta() {
		return lowLyingPlacenta;
	}


	public boolean isRhFlag() {
		return rhFlag;
	}


	public void setVisitId(long visitId) {
		this.visitId = visitId;
	}


	public void setPatientId(long patientMID) {
		this.patientId = patientMID;
	}


	public void setVisitDate(LocalDateTime visitDate) {
		this.visitDate = visitDate;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}


	public void setBloodPressure(String bloodPressure) {
		this.bloodPressure = bloodPressure;
	}


	public void setFetalHeartRate(int fetalHeartRate) {
		this.fetalHeartRate = fetalHeartRate;
	}


	public void setAmount(int amount) {
		this.amount = amount;
	}


	public void setLowLyingPlacenta(boolean lowLyingPlacenta) {
		this.lowLyingPlacenta = lowLyingPlacenta;
	}


	public void setRhFlag(boolean rhFlag) {
		this.rhFlag = rhFlag;
	}


	public boolean isEligible() {
		return eligible;
	}


	public void setEligible() {
		LocalDate tmp = LocalDate.MIN;
		for(int i = 0; i < obstetricsList.size(); i++) {
			boolean flag = obstetricsList.get(i).getInitDate().isAfter(tmp);
			if (flag) {
				tmp = obstetricsList.get(i).getInitDate();
				break;
			}
		}
		long days = 342;
		if(tmp.plusDays(days).isBefore(visitDate.toLocalDate())) {
			eligible = false;
		} else {
			eligible = true; 
		}
	}


	public boolean isObgyn() {
		return obgyn;
	}


	public void setObgyn() throws DBException {
		this.obgyn = obc.getObGyn();
	}


	public List<ObstetricsInit> getObstetricsList() {
		return obstetricsList;
	}


	public void setObstetricsList() throws DBException {
		this.obstetricsList = obc.getObstetricsList();
	}


	public boolean isNotice() {
		setNotice();
		return notice;
	}


	public void setNotice() {
		boolean tmp = (weeksPregnant > 28);
		notice = tmp;
	}

}
