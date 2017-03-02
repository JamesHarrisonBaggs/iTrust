package edu.ncsu.csc.itrust.controller.healthtracker;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.healthtracker.HealthTrackerBean;
import edu.ncsu.csc.itrust.model.healthtracker.HealthTrackerMySQL;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

/**
 * A controller for the HealthTracker XHTML pages.
 * Extends iTrustController to use session and logging utilities.
 * Uses HealthTrackerValidator to validate incoming data.
 * Uses HealthTrackerSQL to interact with the MySQL database.
 * 
 * TODO Log these events
 * VIEW_HEALTHTRACKER_SUMMARY
 *  
 * @author erein
 *
 */
@ManagedBean(name = "htcontrol")
public class HealthTrackerController extends iTrustController {

	private DAOFactory factory;
	private HealthTrackerMySQL database;
	
	private List<HealthTrackerBean> dataList;
	private LocalDate searchDate;
	
	/**
	 * Constructs a new Health Tracker Controller
	 * @throws DBException
	 */
	public HealthTrackerController() throws DBException {
		super();
		this.factory = DAOFactory.getProductionInstance();
		this.database = factory.getHealthTrackerDataSQL();
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
			int rows = database.updateData(bean);
			// log transaction
			if (rows == 2) {
				logTransaction(TransactionType.EDIT_HEALTHTRACKER_DATA, "Rows modified: " + rows);
			} else {
				logTransaction(TransactionType.ENTER_HEALTHTRACKER_DATA, "Rows modified: " + rows);
			}
		} catch (DBException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
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
			rows += database.updateData(b);
		}
		logTransaction(TransactionType.UPLOAD_HEALTHTRACKER_DATA, "Rows modified: " + rows);
	}
	
	/** BASE METHODS CORRESPONDING TO SQL CLASS */
	public ArrayList<HealthTrackerBean> getDataInDay(Timestamp day) throws DBException {
		return database.getDataOnDay(getSessionUtils().getCurrentPatientMIDLong(), day);
	}
	public ArrayList<HealthTrackerBean> getDataInRange(Timestamp start, Timestamp end) throws DBException {
		return database.getDataInRange(getSessionUtils().getCurrentPatientMIDLong(), start, end);
	}	
	public ArrayList<HealthTrackerBean> getAllData() throws DBException {
		return database.getByID(getSessionUtils().getCurrentPatientMIDLong());
	}

	/**
	 * @return the dataList
	 */
	public List<HealthTrackerBean> getDataList() {
		return dataList;
	}

	/**
	 * @return the searchDate
	 */
	public LocalDate getSearchDate() {
		return searchDate;
	}

	/**
	 * @param dataList the dataList to set
	 */
	public void setDataList(List<HealthTrackerBean> dataList) {
		this.dataList = dataList;
	}

	/**
	 * @param searchDate the searchDate to set
	 */
	public void setSearchDate(LocalDate searchDate) {
		this.searchDate = searchDate;
	}

}
