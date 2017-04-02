package edu.ncsu.csc.itrust.model.obgyn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;

public class ObstetricsInitMySQL {

	private ObstetricsInitSQLLoader loader;
	private ObstetricsInitValidator validator;
	private DataSource ds;
	
	/**
	 * Constructs ObstetricsInitMySQL
	 */
	public ObstetricsInitMySQL() throws DBException {
		this.loader = new ObstetricsInitSQLLoader();
		this.validator = new ObstetricsInitValidator();
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
	}
	
	/**
	 * Constructs ObstetricsInitMySQL with a data source
	 */
	public ObstetricsInitMySQL(DataSource ds) {
		this.ds = ds;
		this.loader = new ObstetricsInitSQLLoader();
		this.validator = new ObstetricsInitValidator();
	}
	
	/**
	 * Creates or updates an obstetrics record in the database
	 */
	public int update(ObstetricsInit bean) throws DBException, FormValidationException {
		validator.validate(bean);
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = loader.loadParameters(conn, null, bean, true)) {
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}		
	}
	
	/**
	 * Returns all obstetrics records for a patient
	 */
	public List<ObstetricsInit> getByID(long id) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM obstetrics WHERE id="+id+" ORDER BY init_date DESC");
				ResultSet results = stmt.executeQuery()) {
			return loader.loadList(results);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * Returns a single obstetric record for a patient on a date
	 */
	public List<ObstetricsInit> getByDate(long id, Timestamp date) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM obstetrics WHERE id="+id+" AND init_date=?")) {
			stmt.setTimestamp(1, date);
			ResultSet results = stmt.executeQuery();
			return loader.loadList(results);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
}
