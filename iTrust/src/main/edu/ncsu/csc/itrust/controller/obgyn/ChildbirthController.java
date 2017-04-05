/**
 * 
 */
package edu.ncsu.csc.itrust.controller.obgyn;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.faces.bean.ManagedBean;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.obgyn.Childbirth;
import edu.ncsu.csc.itrust.model.obgyn.ChildbirthMySQL;
import edu.ncsu.csc.itrust.model.obgyn.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.obgyn.ChildbirthVisitMySQL;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

/**
 * @author John Sarle Jpsarle
 *
 */
@ManagedBean(name = "child_controller")
public class ChildbirthController extends iTrustController {

	private SessionUtils sessionUtils;
	private long pid;
	private long mid;
	private long visitId;
	private boolean preScheduled;
	private String deliveryType;
	private int pitocin;
	private int nitrousOxide;
	private int pethidine;
	private int epiduralAnaesthesia;
	private int magnesiumSO4;
	private String gender;
	private LocalDateTime dateOfBirth;
	private int birthID;
	private boolean estimated;
	private ChildbirthMySQL cbs;
	private ChildbirthVisitMySQL cbvs;
	private OfficeVisitController ovc;
	private LocalDateTime visitDate;
	private boolean newBaby;
	private LocalTime timeOfBirth;
	private boolean preEstimateFlag;
	private String name;
	
	public ChildbirthController() throws DBException {
		super();
		sessionUtils = getSessionUtils();
		cbs = new ChildbirthMySQL();
		cbvs = new ChildbirthVisitMySQL();
		ovc = new OfficeVisitController();
		visitId = Long.parseLong(sessionUtils.getRequestParameter("visitID"));
		pid = Integer.parseInt(sessionUtils.getCurrentPatientMID());
		setValues();
	}
	private void setValues() {
		ChildbirthVisit cbv;
		try {
			cbv = getChildbirthVisitVisitID();
		} catch (DBException e) {
			cbv = new ChildbirthVisit();
		}
		
		deliveryType = cbv.getDeliveryType();
		pitocin = cbv.getPitocin();
		nitrousOxide = cbv.getNitrousOxide();
		pethidine = cbv.getPethidine();
		epiduralAnaesthesia = cbv.getEpiduralAnaesthesia();
		magnesiumSO4 = cbv.getMagnesiumSO4(); 
		visitDate = ovc.getVisitByID(visitId).getDate();
	
		newBaby = Boolean.parseBoolean(sessionUtils.getRequestParameter("newBaby"));
	
	}
	public void submit() {
		ChildbirthVisit cbv = new ChildbirthVisit();
		cbv.setDeliveryType(deliveryType);
		cbv.setEpiduralAnaesthesia(epiduralAnaesthesia);
		cbv.setMagnesiumSO4(magnesiumSO4);
		cbv.setNitrousOxide(nitrousOxide);
		cbv.setPatientID(pid);
		cbv.setPethidine(pethidine);
		cbv.setPitocin(pitocin);
		cbv.setVisitID(visitId);
		cbv.setVisitDate(visitDate.toLocalDate());
		try {
			cbvs.update(cbv);
			// TODO Success faces message here
		} catch (DBException e) {
			e.printStackTrace();
		} catch (FormValidationException e) {
			// TODO Error faces message here
		}
	}
	public void submitNew() {
		setNewBaby(true);
	}
	public void submitSave() {
		Childbirth cb = new Childbirth();
		dateOfBirth = dateOfBirth.withHour(timeOfBirth.getHour());
		dateOfBirth = dateOfBirth.withMinute(timeOfBirth.getMinute());
		dateOfBirth = dateOfBirth.withSecond(timeOfBirth.getSecond());
		cb.setBirthdate(dateOfBirth);
		cb.setEstimated(preEstimateFlag);
		cb.setGender(gender);
		try {
			cbs.update(cb);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FormValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setNewBaby(false);
	}

	public ChildbirthController(SessionUtils sessionUtils, TransactionLogger logger) {
		super(sessionUtils, logger);
	}
	public List<Childbirth> getChildBirthVisitID() throws DBException {
		return cbs.getByVisitID(visitId);
	}
	public ChildbirthVisit getChildbirthVisitVisitID() throws DBException {
		return cbvs.getByVisitId(visitId);
	}

	public boolean isPreScheduled() {
		return preScheduled;
	}

	public void setPreScheduled(boolean preScheduled) {
		this.preScheduled = preScheduled;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public int getPitocin() {
		return pitocin;
	}

	public void setPitocin(int pitocin) {
		this.pitocin = pitocin;
	}

	public int getNitrousOxide() {
		return nitrousOxide;
	}

	public void setNitrousOxide(int nitrousOxide) {
		this.nitrousOxide = nitrousOxide;
	}

	public int getPethidine() {
		return pethidine;
	}

	public void setPethidine(int pethidine) {
		this.pethidine = pethidine;
	}

	public int getEpiduralAnaesthesia() {
		return epiduralAnaesthesia;
	}

	public void setEpiduralAnaesthesia(int epiduralAnaesthesia) {
		this.epiduralAnaesthesia = epiduralAnaesthesia;
	}

	public int getMagnesiumSO4() {
		return magnesiumSO4;
	}

	public void setMagnesiumSO4(int magnesiumSO4) {
		this.magnesiumSO4 = magnesiumSO4;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}

	public long getVisitId() {
		return visitId;
	}

	public void setVisitId(int visitId) {
		this.visitId = visitId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDateTime getBirthDate() {
		return dateOfBirth;
	}

	public void setBirthDate(LocalDateTime birthDate) {
		this.dateOfBirth = birthDate;
	}

	public int getBirthID() {
		return birthID;
	}

	public void setBirthID(int birthID) {
		this.birthID = birthID;
	}

	public boolean isEstimated() {
		return estimated;
	}

	public void setEstimated(boolean estimated) {
		this.estimated = estimated;
	}
	public boolean isNewBaby() {
		return newBaby;
	}
	public void setNewBaby(boolean newBaby) {
		this.newBaby = newBaby;
	}
	public boolean isPreEstimateFlag() {
		return preEstimateFlag;
	}
	public void setPreEstimateFlag(boolean preEstimateFlag) {
		this.preEstimateFlag = preEstimateFlag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalTime getTimeOfBirth() {
		return timeOfBirth;
	}
	public void setTimeOfBirth(LocalTime timeOfBirth) {
		this.timeOfBirth = timeOfBirth;
	}
	public LocalDateTime getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDateTime dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
}
