/**
 * 
 */
package edu.ncsu.csc.itrust.controller.obgyn;

import java.time.LocalDateTime;

import javax.faces.bean.ManagedBean;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.obgyn.ChildbirthVisit;
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
	private int visitId;
	private boolean preScheduled;
	private String deliveryType;
	private int pitocin;
	private int nitrousOxide;
	private int pethidine;
	private int epiduralAnaesthesia;
	private int magnesiumSO4;
	private String gender;
	private LocalDateTime birthDate;
	private int birthID;
	private boolean estimated;
	public ChildbirthController() {
		super();
		sessionUtils = getSessionUtils();
	}

	public ChildbirthController(SessionUtils sessionUtils, TransactionLogger logger) {
		super(sessionUtils, logger);
	}
	public ChildbirthVisit getChildBirthVisit() {
		visitId = Integer.parseInt(sessionUtils.getRequestParameter("visitID"));
		pid = Integer.parseInt(sessionUtils.getCurrentPatientMID());
		return null;
		
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

	public int getVisitId() {
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
		return birthDate;
	}

	public void setBirthDate(LocalDateTime birthDate) {
		this.birthDate = birthDate;
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
	
}
