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
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

@ManagedBean(name = "pregnancy_controller")
public class PregnancyController extends iTrustController {
	
	private PregnancyMySQL sql;
	
	public PregnancyController() throws DBException {
		super();
		sql = new PregnancyMySQL();
	}
	
	public PregnancyController(DataSource ds) {
		super();
		sql = new PregnancyMySQL(ds);
	}
	
	public List<Pregnancy> getPregnancies() throws DBException {
		logTransaction(TransactionType.VIEW_PREGNANCY, "");
		long id = getSessionUtils().getCurrentPatientMIDLong();
		return sql.getByID(id);
	}
	
	public List<Pregnancy> getPregnancyByDate(LocalDate date) throws DBException {
		logTransaction(TransactionType.VIEW_PREGNANCY, "");
		long id = getSessionUtils().getCurrentPatientMIDLong();
		return sql.getByDate(id, date);
	}
	
	public void update(Pregnancy bean) {
		long id = getSessionUtils().getCurrentPatientMIDLong();
		bean.setPatientId(id);
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
	
}
