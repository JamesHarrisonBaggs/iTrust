package edu.ncsu.csc.itrust.model.obgyn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;

public class ObstetricsVisitMySQL {

	private ObstetricsVisitSQLLoader loader;
	private ObstetricsVisitValidator validator;
	private DataSource ds;
	
	// TODO lacks a lot of standard methods like getAll() and remove()

	/**
	 * Constructs ObstetricsVisitMySQL
	 */
	public ObstetricsVisitMySQL() throws DBException {
		this.loader = new ObstetricsVisitSQLLoader();
		this.validator = new ObstetricsVisitValidator();
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
	}

	/**
	 * Constructs ObstetricsVisitMySQL with a data source
	 */
	public ObstetricsVisitMySQL(DataSource ds) {
		this.ds = ds;
		this.loader = new ObstetricsVisitSQLLoader();
		this.validator = new ObstetricsVisitValidator();
	}

	/**
	 * Creates or updates an obstetrics visit
	 */
	public int update(ObstetricsVisit bean) throws DBException, FormValidationException {
		validator.validate(bean);
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = loader.loadParameters(conn, null, bean, true)) {
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * Gets a list of obstetrics office visits by patient ID
	 */
	public List<ObstetricsVisit> getByID(long id) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM obstetrics_visits WHERE id="+id + " ORDER BY visitDate DESC");
				ResultSet results = stmt.executeQuery()) {
			return loader.loadList(results);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Gets an obstetric visit by office visit ID
	 */
	public ObstetricsVisit getByVisit(long visitId) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM obstetrics_visits WHERE visitID="+visitId);
				ResultSet results = stmt.executeQuery()) {
			List<ObstetricsVisit> list = loader.loadList(results);
			if (list.size() > 0) {
				return list.get(0);
			} else {
				return new ObstetricsVisit();
			}
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * Gets an obstetric visit by patient ID and office visit ID
	 */
	public List<ObstetricsVisit> getByPatientVisit(long patientId, long visitId) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM obstetrics_visits WHERE id="+patientId+" AND visitID="+visitId);
				ResultSet results = stmt.executeQuery()) {
			return loader.loadList(results);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

}
