package edu.ncsu.csc.itrust.controller.obGyn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obGyn.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

@ManagedBean(name = "obgyn_controller")
public class ObstetricsController extends iTrustController {
	
	private DAOFactory factory;
	private ObstetricsInitMySQL initDB;
	private List<ObstetricsInitBean> dataListObInit;

	/**
	 * Constructs a new Obstetrics Controller
	 * @throws DBException
	 */
	public ObstetricsController() throws DBException {
		super();
		this.factory = DAOFactory.getProductionInstance();
		this.initDB = factory.getObstetricsInitDAO();
		dataListObInit = new ArrayList<ObstetricsInitBean>();
	}
	
	public List<ObstetricsInitBean> getRecords() throws DBException {
		long id = getSessionUtils().getCurrentPatientMIDLong();
		return initDB.getByID(id);
	}
	
	public List<PregnancyBean> getPregnancies() throws DBException {
		long id = getSessionUtils().getCurrentPatientMIDLong();
		return null;
//		return databasePreg.getByID(id);
	}
	
	public List<PregnancyBean> getPregnanciesByDate(LocalDate date) throws DBException {
		long id = getSessionUtils().getCurrentPatientMIDLong();
		return null;
//		return databasePreg.getByDate(id, date);
	}
	
	// add or update
	public void updateRecord(ObstetricsInitBean bean) throws DBException {
		try {
			int result = initDB.update(bean);
			// TODO log
		} catch (FormValidationException e) {
			// TODO handle
		}
	}
	
	public void updatePregnancy(PregnancyBean bean) throws DBException {
//		try {
//			int result = databasePreg.update(bean);
//			// TODO log
//		} catch (FormValidationException e) {
//			// TODO handle
//		}
	}

}