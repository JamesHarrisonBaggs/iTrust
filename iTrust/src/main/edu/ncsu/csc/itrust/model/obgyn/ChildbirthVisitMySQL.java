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

public class ChildbirthVisitMySQL {
	
	private ChildbirthVisitSQLLoader loader;
	private ChildbirthVisitValidator validator;
	private DataSource ds;
	
	public ChildbirthVisitMySQL() throws DBException {
		this.loader = new ChildbirthVisitSQLLoader();
		this.validator = new ChildbirthVisitValidator();
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
	}
	
	public ChildbirthVisitMySQL(DataSource ds) {
		this.loader = new ChildbirthVisitSQLLoader();
		this.validator = new ChildbirthVisitValidator();
		this.ds = ds;
	}

	public int update(ChildbirthVisit bean) throws DBException, FormValidationException {
		validator.validate(bean);
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = loader.loadParameters(conn, null, bean, true)) {
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	public List<ChildbirthVisit> getByVisit(long visitID) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM childbirth_visits WHERE visitID="+visitID);
				ResultSet results = stmt.executeQuery()) {
			return loader.loadList(results);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	

}
