package edu.ncsu.csc.itrust.model.healthtracker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.healthtracker.HealthTrackerBean;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * 
 * A class that performs SQL operations pertaining to Health Tracker Data
 * 
 * @author mcgibson
 *
 */
public class HealthTrackerMySQL {
	
	// Should eventually implement HealthTrackerData Interface
	
	private DAOFactory factory;
	private HealthTrackerSQLLoader loader;
	private HealthTrackerValidator validator;

	/**
	 * Constructs a new SQL class
	 * @param factory - DAO factory
	 */
	public HealthTrackerMySQL(DAOFactory factory) {
		this.factory = factory;
		this.loader = new HealthTrackerSQLLoader();
		this.validator = HealthTrackerValidator.getInstance();
	}

	/**
	 * @return all health tracker data for a specified patient on a specified day
	 * @param pid - the MID of the patient
	 * @param day - the requested day for data
	 * @throws DBException
	 */
	public ArrayList<HealthTrackerBean> getDataOnDay(long pid, Timestamp day) throws DBException {
		ArrayList<HealthTrackerBean> beans = new ArrayList<HealthTrackerBean>();
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM healthtrackerdata WHERE id = ? AND data_date = ?")) {
			stmt.setLong(1, pid);
			stmt.setTimestamp(2, day);
			final ResultSet results = stmt.executeQuery();
			while (results.next()) {
				beans.add(loader.loadResults(results));
			}
			results.close();
		} catch (SQLException e) {
			throw new DBException(e);
		}
		return beans;
	}

	/**
	 * @return all health tracker data for a specified patient in a specified range
	 * @param pid - the MID of the patient
	 * @param start - the starting date of the data range
	 * @param end - the ending date of the data range
	 * @throws DBException
	 */
	public ArrayList<HealthTrackerBean> getDataInRange(long pid, Timestamp start, Timestamp end) throws DBException {
		ArrayList<HealthTrackerBean> beans = new ArrayList<HealthTrackerBean>();
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM healthtrackerdata WHERE id = ? AND data_date >= ? AND data_date <= ?")) {
			stmt.setLong(1, pid);
			stmt.setTimestamp(2, start);
			stmt.setTimestamp(3, end);
			final ResultSet results = stmt.executeQuery();
			while (results.next()) {
				beans.add(loader.loadResults(results));
			}
			results.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			throw new DBException(e);
		}
		return beans;
	}
	
	/**
	 * Returns all health tracker data
	 * @return all health tracker data
	 * @throws DBException
	 */
	public List<HealthTrackerBean> getAll() throws DBException {
		ArrayList<HealthTrackerBean> beans = new ArrayList<HealthTrackerBean>();
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM healthtrackerdata")) {
			final ResultSet results = stmt.executeQuery();
			while (results.next()) {
				beans.add(loader.loadResults(results));
			}
			results.close();
		} catch (SQLException e) {
			throw new DBException(e);
		}
		return beans;
	}

	/**
	 * Returns all health tracker data for a patient
	 * @return all health tracker data for a patient
	 * @param id - the MID of the patient
	 * @throws DBException
	 */
	public ArrayList<HealthTrackerBean> getByID(long id) throws DBException {
		ArrayList<HealthTrackerBean> beans = new ArrayList<HealthTrackerBean>();
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM healthtrackerdata WHERE id = ?")) {
			stmt.setLong(1, id);
			final ResultSet results = stmt.executeQuery();
			while (results.next()) {
				beans.add(loader.loadResults(results));
			}
			results.close();
		} catch (SQLException e) {
			throw new DBException(e);
		}
		return beans;
	}

	/**
	 * Updates health tracker data in the SQL database
	 * @param data - the data bean
	 * @return the number of rows updated (1 for inserted, 2 for updated)
	 * @throws DBException, FormValidationException
	 */
	public int updateData(HealthTrackerBean data) throws DBException, FormValidationException {
		validator.validate(data);
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = loader.loadUpdate(conn.prepareStatement(""
						+ "INSERT INTO healthtrackerdata "
						+ "(id, data_date, calories, steps, distance, floors, mins_sed, mins_light, "
						+ "mins_fair, mins_very, act_cals, act_hours, hr_low, hr_high, "
						+ "hr_avg, uv_exp) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) "
						+ "ON DUPLICATE KEY UPDATE "
						+ "calories=?, steps=?, distance=?, floors=?, "
						+ "mins_sed=?, mins_light=?, mins_fair=?, mins_very=?, act_cals=?, act_hours=?, "
						+ "hr_low=?, hr_high=?, hr_avg=?, uv_exp=?"), data)) {
				int rows = stmt.executeUpdate();
				return rows;
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				throw new DBException(e);
			}
	}
	
}
