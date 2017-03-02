package edu.ncsu.csc.itrust.model.healthtracker;

import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.DataBean;

public interface HealthTrackerData extends DataBean<HealthTrackerBean> {

	public List<HealthTrackerBean> getAll() throws DBException;
	
	public HealthTrackerBean getByID(long id) throws DBException;
	
	public boolean add(HealthTrackerBean bean) throws FormValidationException, DBException;
	
	/**
	 * Adds or updates data in the database
	 * @param data - the data bean
	 * @return the number of rows updated
	 * @throws DBException
	 */
	public int updateData(HealthTrackerBean bean) throws DBException, FormValidationException;
	
}
