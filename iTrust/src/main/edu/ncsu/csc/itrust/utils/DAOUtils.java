package edu.ncsu.csc.itrust.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

public class DAOUtils {
	public static String getName(long mid, DAOFactory factory, String type) throws ITrustException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT firstName, lastName FROM " + type + " WHERE MID=?");) {
			stmt.setLong(1, mid);
			ResultSet results = stmt.executeQuery();
			if (!results.next()) {
				throw new ITrustException("User does not exist");
			}
			final String result = results.getString("firstName") + " " + results.getString("lastName");
			results.close();
			return result;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	public static boolean checkExisits(long pid, DAOFactory factory, String type) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + type + " WHERE MID=?")) {
			stmt.setLong(1, pid);
			final ResultSet results = stmt.executeQuery();
			final boolean exists = results.next();
			results.close();
			return exists;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
