package edu.ncsu.csc.itrust.model.obgyn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;

public class UltrasoundMySQL {
	private DataSource ds;
	private UltrasoundValidator validator;
	private UltrasoundSQLLoader loader;
	
	public UltrasoundMySQL() throws DBException {
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
		loader = new UltrasoundSQLLoader();
		validator = new UltrasoundValidator();
	}
	
	public UltrasoundMySQL(DataSource ds) throws DBException {
		this.ds = ds;
		loader = new UltrasoundSQLLoader();
		validator = new UltrasoundValidator();
	}
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
	
	public Ultrasound getByVisitFetus(long id, int fetus) throws DBException {
		try (Connection conn = ds.getConnection();) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("SELECT * FROM itrust.ultrasounds Where visitID='");
			stringBuilder.append(id);
			stringBuilder.append("' and fetus='");
			stringBuilder.append(fetus);
			stringBuilder.append("';");
			String statement = stringBuilder.toString();
			PreparedStatement stmt = conn.prepareStatement(statement);
			ResultSet results = stmt.executeQuery();
			if(results.next()) {
				return loader.loadSingle(results);
			} else {
				return new Ultrasound();
			}
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	public List<Ultrasound> getByPatientId(long id) throws DBException {
		try (Connection conn = ds.getConnection();) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("SELECT * FROM itrust.ultrasounds Where id='");
			stringBuilder.append(id);
			stringBuilder.append("';");
			String statement = stringBuilder.toString();
			PreparedStatement stmt = conn.prepareStatement(statement);
			ResultSet results = stmt.executeQuery();
			if(results.next()) {
				return loader.loadList(results);
			} else {
				return new ArrayList<Ultrasound>();
			}
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public Ultrasound getByVisit(long id) throws DBException {
		try (Connection conn = ds.getConnection();) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("SELECT * FROM itrust.ultrasounds Where visitID='");
			stringBuilder.append(id);
			stringBuilder.append("';");
			String statement = stringBuilder.toString();
			PreparedStatement stmt = conn.prepareStatement(statement);
			ResultSet results = stmt.executeQuery();
			if(results.next()) {
				return loader.loadSingle(results);
			} else {
				return new Ultrasound();
			}
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public void removeUltrasound(long visitId2, int fetusId2) throws DBException {
		try (Connection conn = ds.getConnection();) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("DELETE FROM itrust.ultrasounds Where visitID='");
			stringBuilder.append(visitId2);
			stringBuilder.append("' and fetus='");
			stringBuilder.append(fetusId2);
			stringBuilder.append("';");
			String statement = stringBuilder.toString();
			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.execute();
		} catch (SQLException e) {
			throw new DBException(e);
		}	
	}
	
}
