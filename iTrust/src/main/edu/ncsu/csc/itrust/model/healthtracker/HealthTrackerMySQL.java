package edu.ncsu.csc.itrust.model.healthtracker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.model.healthtracker.HealthTrackerBean;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;

public class HealthTrackerMySQL {
		
	private DataSource ds;
	private HealthTrackerSQLLoader loader;
	private HealthTrackerValidator validator;

	/**
	 * Constructs a HealthTrackerMySQL
	 */
	public HealthTrackerMySQL() throws DBException {
		this.loader = new HealthTrackerSQLLoader();
		this.validator = new HealthTrackerValidator();
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}		
	}
	
	/**
	 * Constructs a HealthTrackerMySQL with a data source
	 */
	public HealthTrackerMySQL(DataSource ds) throws DBException {
		this.ds = ds;
		this.loader = new HealthTrackerSQLLoader();
		this.validator = new HealthTrackerValidator();
	}

	/**
	 * @return all health tracker data for a specified patient on a specified day
	 * @param id - the MID of the patient
	 * @param day - the requested day for data
	 * @throws DBException
	 */
	public List<HealthTrackerBean> getDataOnDay(long id, Timestamp day) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM healthtrackerdata WHERE id = "+id+" AND data_date = ?")) {
			stmt.setTimestamp(1, day);
			ResultSet results = stmt.executeQuery();
			return loader.loadList(results);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * @return all health tracker data for a specified patient in a specified range
	 * @param id - the MID of the patient
	 * @param start - the starting date of the data range
	 * @param end - the ending date of the data range
	 * @throws DBException
	 */
	public List<HealthTrackerBean> getDataInRange(long id, Timestamp start, Timestamp end) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM healthtrackerdata WHERE id = "+id+" AND data_date >= ? AND data_date <= ?")) {
			stmt.setTimestamp(1, start);
			stmt.setTimestamp(2, end);
			final ResultSet results = stmt.executeQuery();
			return loader.loadList(results);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * Returns all health tracker data
	 * @return all health tracker data
	 * @throws DBException
	 */
	public List<HealthTrackerBean> getAll() throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM healthtrackerdata");
				ResultSet results = stmt.executeQuery()) {
			return loader.loadList(results);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns all health tracker data for a patient
	 * @return all health tracker data for a patient
	 * @param id - the MID of the patient
	 * @throws DBException
	 */
	public List<HealthTrackerBean> getByID(long id) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM healthtrackerdata WHERE id="+id);
				ResultSet results = stmt.executeQuery()) {
			return loader.loadList(results);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Updates health tracker data in the SQL database
	 * @param data - the data bean
	 * @return the number of rows updated (1 for inserted, 2 for updated)
	 * @throws DBException, FormValidationException
	 */
	public int updateData(HealthTrackerBean data) throws DBException, FormValidationException {
		validator.validate(data);
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = loader.loadParameters(conn, null, data, true)) {
				return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
}
