package edu.ncsu.csc.itrust.model.obgyn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;

public class PregnancyMySQL {

	private PregnancySQLLoader loader;
	private PregnancyValidator validator;
	private DataSource ds;
	
	/**
	 * Constructs a new PregnancyMySQL
	 */
	public PregnancyMySQL() throws DBException {
		this.loader = new PregnancySQLLoader();
		this.validator = new PregnancyValidator();
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
	}
	
	/**
	 * Constructs a new PregnancyMySQL with a data source
	 */
	public PregnancyMySQL(DataSource ds) {
		this.ds = ds;
		this.loader = new PregnancySQLLoader();
		this.validator = new PregnancyValidator();
	}
	
	/**
	 * Creates or updates an obstetrics record in the database
	 */
	public int update(Pregnancy bean) throws DBException, FormValidationException {
		validator.validate(bean);
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = loader.loadParameters(conn, null, bean, true)) {
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}		
	}
	
	/**
	 * Returns all pregnancies for a patient
	 */
	public List<Pregnancy> getByID(long id) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM pregnancies WHERE id="+id);
				ResultSet results = stmt.executeQuery()) {
			return loader.loadList(results);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * Returns all pregnancies for a patient before a specific date
	 */
	public List<Pregnancy> getByDate(long id, LocalDate date) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM pregnancies WHERE id = ? AND birth_date <= ?")) {
			stmt.setLong(1, id);
			stmt.setTimestamp(2, Timestamp.valueOf(date.atStartOfDay()));
			ResultSet results = stmt.executeQuery();
			return loader.loadList(results);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

}
