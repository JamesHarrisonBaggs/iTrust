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

public class ChildbirthMySQL {

	private ChildbirthSQLLoader loader;
	private ChildbirthValidator validator;
	private DataSource ds;

	public ChildbirthMySQL() throws DBException {
		this.loader = new ChildbirthSQLLoader();
		this.validator = new ChildbirthValidator();
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
	}

	public ChildbirthMySQL(DataSource ds) {
		this.loader = new ChildbirthSQLLoader();
		this.validator = new ChildbirthValidator();
		this.ds = ds;
	}

	public int update(Childbirth bean) throws DBException, FormValidationException {
		validator.validate(bean);
		
		// new (create)
		if (bean.getBirthID() == -1) {
			try (Connection conn = ds.getConnection();
					PreparedStatement stmt = loader.loadParameters(conn, null, bean, true)) {
				return stmt.executeUpdate();
			} catch (SQLException e) {
				throw new DBException(e);
			}
		}
		// existing (update)
		else {
			try (Connection conn = ds.getConnection();
					PreparedStatement stmt = loader.loadParameters(conn, null, bean, false)) {
				return stmt.executeUpdate();
			} catch (SQLException e) {
				throw new DBException(e);
			}
		}
	}
	
	public List<Childbirth> getByParentID(long parentID) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM childbirths WHERE parentID="+parentID);
				ResultSet results = stmt.executeQuery()) {
			return loader.loadList(results);
		} catch (SQLException e) {
			throw new DBException(e);
		}		
	}

	public List<Childbirth> getByVisitID(long visitID) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM childbirths WHERE visitID="+visitID);
				ResultSet results = stmt.executeQuery()) {
			return loader.loadList(results);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public Childbirth getByBirthID(long visitID, int birthID) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM childbirths WHERE visitID="+visitID+" AND birthID="+birthID);
				ResultSet results = stmt.executeQuery()) {
			List<Childbirth> list = loader.loadList(results);
			if (list.size() > 0) {
				return list.get(0);
			} else {
				return new Childbirth();
			}
		} catch (SQLException e) {
			throw new DBException(e);			
		}
	}

	public void remove(long visitID, int birthID) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement("DELETE FROM childbirths WHERE visitID="+visitID+" AND birthID="+birthID))
		{
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);			
		}
	}

}
