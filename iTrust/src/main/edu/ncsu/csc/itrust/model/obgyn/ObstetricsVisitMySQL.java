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
	
	public ObstetricsVisitMySQL() throws DBException {
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
		loader = new ObstetricsVisitSQLLoader();
		validator = new ObstetricsVisitValidator();
	}
	
	public ObstetricsVisitMySQL(DataSource ds) throws DBException {
		this.ds = ds;
		loader = new ObstetricsVisitSQLLoader();
		validator = new ObstetricsVisitValidator();
	}
	
	public int update(ObstetricsVisit bean) throws DBException {
		try {
			validator.validate(bean);
		} catch (FormValidationException e) {
			throw new DBException(new SQLException(e));
		}
		try (Connection conn = ds.getConnection();
			PreparedStatement stmt = loader.loadParameters(conn, null, bean, true)) {
			int results = stmt.executeUpdate();
			return results;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	public List<ObstetricsVisit> getByID(long id) throws DBException {
		// TODO implement
		return null;
	}
	
	public ObstetricsVisit getByFetus(long id, int fetus) throws DBException {
		// TODO implement
		return null;		
	}
	
	public List<ObstetricsVisit> getByVisit(long visitId) throws DBException {
		// TODO implement
		return null;
	}
	
	public ObstetricsVisit getByVisitAndFetus(long visitId, int fetus) throws DBException {
		// TODO implement
		return null;		
	}
	
}
