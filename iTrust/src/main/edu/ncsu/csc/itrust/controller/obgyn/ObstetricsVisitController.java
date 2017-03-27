package edu.ncsu.csc.itrust.controller.obgyn;

import java.time.LocalDateTime;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
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

	public ObstetricsVisitController() throws DBException {
		super();
		sql = new ObstetricsVisitMySQL();
		ob = getObstetricsVisit();
		ob.setVisitDate(getOfficeVisit().getDate());
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
		sql.update(bean);
	}

	public void add() throws DBException {

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


	public int getWeeksPregnant() {
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


	public void setWeeksPregnant(int weeksPregnant) {
		this.weeksPregnant = weeksPregnant;
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

}
