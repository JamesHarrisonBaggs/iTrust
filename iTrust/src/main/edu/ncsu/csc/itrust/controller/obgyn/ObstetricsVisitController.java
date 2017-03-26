package edu.ncsu.csc.itrust.controller.obgyn;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

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
	
	public ObstetricsVisitController() throws DBException {
		super();
		sql = new ObstetricsVisitMySQL();
	}

	public ObstetricsVisitController(DataSource ds) throws DBException {
		super();
		sql = new ObstetricsVisitMySQL(ds);
	}
	
	private OfficeVisit getOfficeVisit() throws DBException {
		long id = getSessionUtils().getCurrentOfficeVisitId();
		return new OfficeVisitController().getVisitByID(id);
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
	
}
