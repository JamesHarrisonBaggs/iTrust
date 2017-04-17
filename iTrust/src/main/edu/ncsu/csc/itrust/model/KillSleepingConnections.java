package edu.ncsu.csc.itrust.model;

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

public class KillSleepingConnections {
	
	DataSource ds;
	Connection conn;
	PreparedStatement stmt;
	
	public void kill() throws DBException, SQLException{
		try {
			Context ctx = new InitialContext();
			this.ds = ConverterDAO.getDataSource();
			conn = ds.getConnection();
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.stmt = conn.prepareStatement("select concat('KILL ',id,';') from information_schema.processlist where Command='Sleep' and time>100;");
	

		ResultSet results = stmt.executeQuery();
		while(results.next()){
			String s = results.getString(1);
			this.stmt = conn.prepareStatement(s);
			stmt.executeQuery();
		}
	}
	
}

