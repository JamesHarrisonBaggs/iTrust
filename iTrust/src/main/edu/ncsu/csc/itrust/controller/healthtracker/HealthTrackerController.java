package edu.ncsu.csc.itrust.controller.healthtracker;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.healthtracker.HealthTrackerBean;
import edu.ncsu.csc.itrust.model.healthtracker.HealthTrackerMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

/**
 * A controller for the HealthTracker XHTML pages.
 * Extends iTrustController to use session and logging utilities.
 * Uses HealthTrackerSQL to interact with the MySQL database.
 * 
 * TODO Log VIEW_HEALTHTRACKER_SUMMARY
 *  
 * @author erein
 *
 */
@ManagedBean(name = "htcontrol")
public class HealthTrackerController extends iTrustController {

	private HealthTrackerMySQL sql;
	private List<HealthTrackerBean> dataList;
	private LocalDate searchDate;
	
	/**
	 * Constructs a new Health Tracker Controller
	 */
	public HealthTrackerController() throws DBException {
		super();
		this.sql = new HealthTrackerMySQL();
		dataList = new ArrayList<HealthTrackerBean>();
	}
	
	/**
	 * Constructs a new Health Tracker Controller with a data source
	 */
	public HealthTrackerController(DataSource ds) throws DBException {
		super();
		this.sql = new HealthTrackerMySQL(ds);
		dataList = new ArrayList<HealthTrackerBean>();
	}
		
	/**
	 * USED BY ENTER DATA
	 * Updates a set of data in the HealthTracker database table
	 * Creates and inserts the data if it does not exist; updates and overwrites it if it does
	 * @param bean - the data bean
	 * @throws FormValidationException
	 */
	public void updateData(HealthTrackerBean bean) throws FormValidationException {
		bean.setPatientId(getSessionUtils().getCurrentPatientMIDLong());
		try {
			int rows = sql.updateData(bean);
			if (rows == 2) {
				logTransaction(TransactionType.EDIT_HEALTHTRACKER_DATA, "Rows modified: " + rows);
			} else {
				logTransaction(TransactionType.ENTER_HEALTHTRACKER_DATA, "Rows modified: " + rows);
			}
		} catch (DBException | FormValidationException e) {
			FacesContext.getCurrentInstance().addMessage("htform", new FacesMessage(e.getMessage()));
		}
	}
	
	/**
	 * USED BY VIEW DATA
	 * Sets dataList for the current patient on the specified date
	 * If no results are found, adds a default bean to the list
	 * @throws DBException
	 */
	public void searchForData() throws DBException {
		// search for current patient and date
		dataList = this.getDataInDay(Timestamp.valueOf(searchDate.atStartOfDay()));
		// if no results, add empty bean
		if (dataList.isEmpty()) {
			dataList.add(new HealthTrackerBean());
		}
		// log transaction
		logTransaction(TransactionType.VIEW_HEALTHTRACKER_DATA, "");		
	}
		
	/**
	 * USED BY FILE UPLOAD BEAN
	 * Updates the beans in the database
	 * @param beans
	 */
	public void uploadData(ArrayList<HealthTrackerBean> beans) throws DBException, FormValidationException {
		int rows = 0;
		for (HealthTrackerBean b : beans) {
			b.setPatientId(getSessionUtils().getCurrentPatientMIDLong());
			rows += sql.updateData(b);
		}
		logTransaction(TransactionType.UPLOAD_HEALTHTRACKER_DATA, "Rows modified: " + rows);
	}
	
	/** BASE METHODS CORRESPONDING TO SQL CLASS */
	public List<HealthTrackerBean> getDataInDay(Timestamp day) throws DBException {
		return sql.getDataOnDay(getSessionUtils().getCurrentPatientMIDLong(), day);
	}
	public List<HealthTrackerBean> getDataInRange(Timestamp start, Timestamp end) throws DBException {
		return sql.getDataInRange(getSessionUtils().getCurrentPatientMIDLong(), start, end);
	}	
	public List<HealthTrackerBean> getAllData() throws DBException {
		return sql.getByID(getSessionUtils().getCurrentPatientMIDLong());
	}

	public HealthTrackerMySQL getSql() {
		return sql;
	}

	public List<HealthTrackerBean> getDataList() {
		return dataList;
	}

	public LocalDate getSearchDate() {
		return searchDate;
	}

	public void setSql(HealthTrackerMySQL sql) {
		this.sql = sql;
	}

	public void setDataList(List<HealthTrackerBean> dataList) {
		this.dataList = dataList;
	}

	public void setSearchDate(LocalDate searchDate) {
		this.searchDate = searchDate;
	}

}
