package edu.ncsu.csc.itrust.controller.obgyn;

import java.time.LocalDate;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.obgyn.Pregnancy;
import edu.ncsu.csc.itrust.model.obgyn.PregnancyMySQL;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "pregnancy_controller")
public class PregnancyController extends iTrustController {
	
	private PregnancyMySQL sql;
	private long patientId;
	private SessionUtils utils;
	
	public PregnancyController() throws DBException {
		this.utils = this.getSessionUtils();
		this.sql = new PregnancyMySQL();
		this.patientId = utils.getCurrentPatientMIDLong().longValue();
	}
	
	public PregnancyController(DataSource ds, SessionUtils utils, DAOFactory factory) {
		super(null, null, factory);
		this.utils = utils;
		this.sql = new PregnancyMySQL(ds);
		this.patientId = utils.getCurrentPatientMIDLong().longValue();
	}
	
	public List<Pregnancy> getPregnancies() throws DBException {
		logTransaction(TransactionType.VIEW_PREGNANCY, "");
		return sql.getByID(patientId);
	}
	
	public List<Pregnancy> getPregnancyByDate(LocalDate date) throws DBException {
		logTransaction(TransactionType.VIEW_PREGNANCY, "");
		return sql.getByDate(patientId, date);
	}
	
	public void update(Pregnancy bean) {
		bean.setPatientId(patientId);
		try {
			int result = sql.update(bean);
			logTransaction(result != 2 ? TransactionType.CREATE_PREGNANCY : TransactionType.UPDATE_PREGNANCY, "");
			String msg = result != 2 ? "Prior pregnancy added" : "Prior pregnancy updated";
			printFacesMessage(FacesMessage.SEVERITY_INFO, msg, msg, "pregnancyForm");
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to modify Pregnancy", e.getExtendedMessage(), "pregnancyForm");
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to modify Pregnancy", e.getMessage(), "pregnancyForm");
		}
	}

	public PregnancyMySQL getSql() {
		return sql;
	}

	public void setSql(PregnancyMySQL sql) {
		this.sql = sql;
	}
	
}
