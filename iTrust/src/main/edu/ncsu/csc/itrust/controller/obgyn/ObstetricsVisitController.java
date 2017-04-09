package edu.ncsu.csc.itrust.controller.obgyn;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import edu.ncsu.csc.itrust.action.ApptAction;
import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsVisit;
import edu.ncsu.csc.itrust.model.obgyn.ObstetricsVisitMySQL;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import edu.ncsu.csc.itrust.model.old.beans.ApptTypeBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ApptDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ApptRequestDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ApptTypeDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

import javax.sql.DataSource;

@ManagedBean(name = "obs_visit_controller")
public class ObstetricsVisitController extends iTrustController {

	private ObstetricsVisitMySQL sql;
	private ObstetricsVisit ob;
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
	
	private ObstetricsInitController obc;
	private OfficeVisitController ovc;
	
	private ApptDAO aDAO;
	private ApptRequestDAO arDAO;
	private ApptTypeDAO atDAO;

	private boolean eligible;
	private boolean obgyn;
	private boolean notice;

	private List<ObstetricsInit> obstetricsList;
	private DAOFactory factory;
	private SessionUtils sessionUtils;
	private boolean run;
	private boolean autoSchedule;

	public ObstetricsVisitController() throws DBException {
		super();
		
		obc = new ObstetricsInitController();
		sql = new ObstetricsVisitMySQL();
		ovc = new OfficeVisitController();
		sessionUtils = getSessionUtils();
		
		setUpController();
		
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

		String run = sessionUtils.getRequestParameter("run");
		if (run != null) {
			if(Boolean.parseBoolean(sessionUtils.getRequestParameter("run"))) {
				try {
					autoScheduleAppt();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public ObstetricsVisitController(DataSource ds, SessionUtils utils) throws DBException {
		super();
		sessionUtils = utils;
		obc = new ObstetricsInitController(ds);
		obc.setSessionUtils(utils);
		sql = new ObstetricsVisitMySQL(ds);
		ovc = new OfficeVisitController(ds);
		ovc.setSessionUtils(utils);		
		setUpController();
	}
	
	private void setUpController() throws DBException {
		ObstetricsVisit ov = getObstetricsVisit();
		ob = ov == null ? ov : new ObstetricsVisit();
	}
	
	/**
	 * Creates or updates an ObstetricsVisit
	 */
	public void update(ObstetricsVisit bean) {
		try {
			int result = sql.update(bean);
			printFacesMessage(FacesMessage.SEVERITY_INFO, "Obstetrics Visit updated", 
					"Obstetrics Visit updated", "manage_obstetrics_formSuccess");
			logTransaction(result != 2 ? TransactionType.CREATE_OBSTETRIC_OFFICE_VISIT : 
				TransactionType.EDIT_OBSTETRIC_OFFICE_VISIT, String.valueOf(visitId));
		} catch (Exception e) { //FormValidation, DB, etc.
			printFacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(),
					e.getMessage(), "manage_obstetrics_formError");			
		}
	}

	public OfficeVisit getOfficeVisit() {
		long id = sessionUtils.getCurrentOfficeVisitId();
		return ovc.getVisitByID(id);
	}
	
	public ObstetricsVisit getObstetricsVisit() {
		long id = sessionUtils.getCurrentOfficeVisitId();
		logTransaction(TransactionType.VIEW_OBSTETRIC_OFFICE_VISIT, String.valueOf(visitId));
		ObstetricsVisit ov = null;
		try {
			ov = sql.getByVisit(id);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(),
					e.getMessage(), "manage_obstetrics_formError");			
		}
		return ov;
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
		this.setAutoSchedule(false);
		this.update(ob);
		this.setAutoSchedule(true);
	}
	
	public boolean autoScheduleAppt() throws DBException, SQLException {
//		factory = DAOFactory.getProductionInstance();
//		aDAO = new ApptDAO(factory);
//		arDAO = new ApptRequestDAO(factory);
//		atDAO = new ApptTypeDAO(factory);
//		LocalDateTime visitDate2 = visitDate;
//		long pid = sessionUtils.getCurrentPatientMIDLong();
//		long mid = sessionUtils.getSessionLoggedInMIDLong();
//		ApptBean appt = new ApptBean();
//		ApptTypeBean apptType = atDAO.getApptType("OB/GYN");
//		appt.setApptType(apptType.getName());
//		appt.setPrice(apptType.getPrice());
//		appt.setHcp(mid);
//		appt.setPatient(pid);
//		if(visitDate2.getHour() > 16 || visitDate2.getHour() < 9) {
//			visitDate2 = visitDate2.withHour(12);
//		}
//		if(visitDate2.getDayOfWeek() == DayOfWeek.SATURDAY) {
//			visitDate2 = visitDate2.plusDays(2);
//		}
//		if (visitDate2.getDayOfWeek() == DayOfWeek.SUNDAY) {
//			visitDate2 = visitDate2.plusDays(1);
//		}
//		if (weeksPregnant >= 0 && weeksPregnant <= 13) {
//			long months = 1;
//			visitDate2 = visitDate2.plusMonths(months);
//			Timestamp timestamp = Timestamp.valueOf(visitDate2);
//			appt.setDate(timestamp);
//		} else if (weeksPregnant >= 14 && weeksPregnant <= 28) {
//			long weeks = 2;
//			visitDate2 = visitDate2.plusWeeks(weeks);
//			Timestamp timestamp = Timestamp.valueOf(visitDate2);
//			appt.setDate(timestamp);
//		} else if (weeksPregnant >= 29 && weeksPregnant <= 40) {
//			long weeks = 1;
//			visitDate2 = visitDate2.plusWeeks(weeks);
//			Timestamp timestamp = Timestamp.valueOf(visitDate2);
//			appt.setDate(timestamp);
//		} else if (weeksPregnant >= 40 && weeksPregnant <= 42) {
//			long days = 2;
//			visitDate2 = visitDate2.plusDays(days);
//			if(visitDate2.getDayOfWeek() == DayOfWeek.SATURDAY) {
//				visitDate2 = visitDate2.plusDays(2);
//			}
//			if (visitDate2.getDayOfWeek() == DayOfWeek.SUNDAY) {
//				visitDate2 = visitDate2.plusDays(1);
//			}
//			Timestamp timestamp = Timestamp.valueOf(visitDate2);
//			appt.setDate(timestamp);
//		} 
//		List<ApptBean> conflictsHCP = aDAO.getAllHCPConflictsForAppt(mid, appt);
//		List<ApptBean> conflictsPatient = aDAO.getAllPatientConflictsForAppt(pid, appt);
//		if (conflictsHCP.isEmpty() && conflictsPatient.isEmpty()) {
//			aDAO.scheduleAppt(appt);	
//		} else {
//			while(!conflictsHCP.isEmpty() || !conflictsPatient.isEmpty()) {
//				long days = 2;
//				visitDate2 = visitDate2.plusDays(days);
//				Timestamp timestamp = Timestamp.valueOf(visitDate2);
//				appt.setDate(timestamp);
//				conflictsHCP = aDAO.getAllHCPConflictsForAppt(mid, appt);
//				conflictsPatient = aDAO.getAllPatientConflictsForAppt(pid, appt);
//			}
//			aDAO.scheduleAppt(appt);
//			
//		}
//		List<ApptBean> appzs = aDAO.getAllApptsFor(mid);
//		logTransaction(TransactionType.SCHEDULE_OFFICE_VISIT, String.valueOf(this.visitId)+ ", " + String.valueOf(appzs.get(appzs.size()-1).getApptID()));
		return true;
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
		LocalDate lmp = obc.getObstetricsRecords().get(0).getLastMenstrualPeriod();
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
		for (int i = 0; i < obstetricsList.size(); i++) {
			LocalDate date = obstetricsList.get(i).getInitDate();
			if (date.isAfter(tmp)) {
				tmp = date;
				break;
			}
		}
		eligible = !tmp.plusWeeks(49).isBefore(visitDate.toLocalDate());
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
		this.obstetricsList = obc.getObstetricsRecords();
	}

	public boolean isNotice() {
		setNotice();
		return notice;
	}

	public void setNotice() {
		notice = (weeksPregnant > 28);
	}

	public boolean isAutoSchedule() {
		return autoSchedule;
	}

	public void setAutoSchedule(boolean autoSchedule) {
		this.autoSchedule = autoSchedule;
	}

	public ObstetricsVisitMySQL getSql() {
		return sql;
	}

	public void setSql(ObstetricsVisitMySQL sql) {
		this.sql = sql;
	}

}
