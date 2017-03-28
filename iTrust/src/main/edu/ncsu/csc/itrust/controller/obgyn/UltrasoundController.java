package edu.ncsu.csc.itrust.controller.obgyn;

import java.time.LocalDateTime;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.obgyn.Ultrasound;
import edu.ncsu.csc.itrust.model.obgyn.UltrasoundMySQL;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "ultrasound_controller")
public class UltrasoundController extends iTrustController {
	private long visitId;
	private int crl;
	private int bpd;
	private int hc;
	private int fl;
	private int ofd;
	private int ac;
	private int hl;
	private int efw;
	private int fetusId; 
	private Ultrasound us;
	private UltrasoundMySQL sql;
	private long patientId;
	private LocalDateTime visitDate;
	
	public UltrasoundController() throws DBException {
		super();
		sql = new UltrasoundMySQL();
		us = getUltrasoundVisitID();
		if (getOfficeVisit().getVisitID() == 0) {
			us.setVisitId(getOfficeVisit().getVisitID());
			us.setVisitDate(getOfficeVisit().getDate());
		
		try {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("officeVisitId", us.getVisitId());
		} catch (NullPointerException e) {
			// Do nothing
		}
		visitId = us.getVisitId();
		patientId = us.getPatientId();
		if (patientId == 0) {
			patientId = Long.parseLong(
					(String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pid"));
		}
		crl = us.getCrownRumpLength();
		bpd = us.getBiparietalDiameter();
		hc = us.getHeadCircumference();
		fl = us.getFemurLength();
		ofd = us.getOccipitofrontalDiameter();
		ac = us.getAbdominalCircumference();
		hl = us.getHumerusLength();
		efw = us.getEstimatedFetalWeight();
		fetusId = us.getFetusId();
		} else {
			us = new Ultrasound();
		}
	}

	public UltrasoundController(SessionUtils sessionUtils, TransactionLogger logger) {
		super(sessionUtils, logger);
	}

	public OfficeVisit getOfficeVisit() throws DBException {
		long id = getSessionUtils().getCurrentOfficeVisitId();
		return new OfficeVisitController().getVisitByID(id);
	}
	public Ultrasound getUltrasoundVisitID() throws DBException {
		long id = getSessionUtils().getCurrentOfficeVisitId();
		return sql.getByVisit(id);
	}
	public void submit() throws DBException {
		us.setPatientId(getSessionUtils().getCurrentPatientMIDLong());
		us.setVisitDate(getOfficeVisit().getDate());
		us.setVisitId(getSessionUtils().getCurrentOfficeVisitId());
		us.setCrownRumpLength(crl);
		us.setBiparietalDiameter(bpd);
		us.setHeadCircumference(hc);
		us.setFemurLength(fl);
		us.setOccipitofrontalDiameter(ofd);
		us.setAbdominalCircumference(ac);
		us.setHumerusLength(hl);
		us.setEstimatedFetalWeight(efw);
		us.setFetusId(fetusId);
		update(us);
	}
	public void update(Ultrasound bean) throws DBException {
		try {
			sql.update(bean);
			FacesContext.getCurrentInstance().addMessage("ultrasound_formSuccess", new FacesMessage("Ultrasound Updated Successfully"));
		} catch (FormValidationException e) {
			FacesContext.getCurrentInstance().addMessage("ultrasound_formError", new FacesMessage(e.getMessage()));
		}
	}
	public List<Ultrasound> getUltrasoundsForCurrentPatient() throws DBException {
		long id = getSessionUtils().getCurrentPatientMIDLong();
		return sql.getByPatientId(id);
	}
	
	public int getCrl() {
		return crl;
	}

	public int getBpd() {
		return bpd;
	}

	public int getHc() {
		return hc;
	}

	public int getFl() {
		return fl;
	}

	public int getOfd() {
		return ofd;
	}

	public int getAc() {
		return ac;
	}

	public int getHl() {
		return hl;
	}

	public int getEfw() {
		return efw;
	}

	public void setCrl(int crl) {
		this.crl = crl;
	}

	public void setBpd(int bpd) {
		this.bpd = bpd;
	}

	public void setHc(int hc) {
		this.hc = hc;
	}

	public void setFl(int fl) {
		this.fl = fl;
	}

	public void setOfd(int ofd) {
		this.ofd = ofd;
	}

	public void setAc(int ac) {
		this.ac = ac;
	}

	public void setHl(int hl) {
		this.hl = hl;
	}

	public void setEfw(int efw) {
		this.efw = efw;
	}

	public int getFetusId() {
		return fetusId;
	}

	public void setFetusId(int fetusId) {
		this.fetusId = fetusId;
	}

}


