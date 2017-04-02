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

public class UltrasoundMySQL {

	private DataSource ds;
	private UltrasoundSQLLoader loader;
	private UltrasoundValidator validator;

	/**
	 * Constructs an UltrasoundMySQL
	 */
	public UltrasoundMySQL() throws DBException {
		loader = new UltrasoundSQLLoader();
		validator = new UltrasoundValidator();
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
	}
	
	/**
	 * Constructs an UltrasoundMySQL with a data source
	 */
	public UltrasoundMySQL(DataSource ds) {
		this.ds = ds;
		loader = new UltrasoundSQLLoader();
		validator = new UltrasoundValidator();
	}

	/**
	 * Creates or updates an Ultrasound record
	 */
	public int update(Ultrasound bean) throws DBException, FormValidationException {
		validator.validate(bean);
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = loader.loadParameters(conn, null, bean, true)) {
			int results = stmt.executeUpdate();
			return results;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Gets an Ultrasound by office visit ID and fetus number
	 */
	public Ultrasound getByVisitFetus(long visitID, int fetus) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ultrasounds WHERE visitID="+visitID+" AND fetus="+fetus);
				ResultSet results = stmt.executeQuery()) {
			List<Ultrasound> list = loader.loadList(results);
			if (list.size() > 0) {
				return list.get(0);
			} else {
				return new Ultrasound();
			}
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Gets a list of Ultrasound records by patient ID
	 */
	public List<Ultrasound> getByPatientId(long patientID) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ultrasounds WHERE id="+patientID);
				ResultSet results = stmt.executeQuery()) {
			return loader.loadList(results);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * Gets a list of Ultrasound records by patient ID and office visit ID
	 */
	public List<Ultrasound> getByPatientIdVisitId(long patientID, long visitID) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ultrasounds WHERE id="+patientID+" AND visitID="+visitID);
				ResultSet results = stmt.executeQuery()) {
			return loader.loadList(results);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Deletes an Ultrasound from the database given office visit ID and fetus number
	 */
	public boolean removeUltrasound(long visitID, int fetus) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("DELETE FROM ultrasounds WHERE visitID="+visitID+" AND fetus="+fetus)) {
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new DBException(e);
		}	
	} 
	
	/**
	 * Removes an Ultrasound image from the database
	 */
	public boolean removeUltrasoundImage(long visitID, int fetus) throws DBException{
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("Update ultrasounds SET file=null WHERE visitID=" + visitID +" AND fetus=" + fetus)) {
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new DBException(e);
		}	
	}

}
