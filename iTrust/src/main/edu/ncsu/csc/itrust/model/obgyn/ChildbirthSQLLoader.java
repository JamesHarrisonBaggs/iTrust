package edu.ncsu.csc.itrust.model.obgyn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.SQLLoader;

public class ChildbirthSQLLoader implements SQLLoader<Childbirth> {

	/**
	 * Loads a list of results from a query
	 */
	@Override
	public List<Childbirth> loadList(ResultSet rs) throws SQLException {
		List<Childbirth> list = new ArrayList<Childbirth>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	/**
	 * Loads a single result from a query
	 */
	@Override
	public Childbirth loadSingle(ResultSet rs) throws SQLException {
		Childbirth bean = new Childbirth();
		bean.setParentID(rs.getLong("parentID"));
		bean.setVisitID(rs.getLong("visitID"));
		bean.setBirthID(rs.getInt("birthID"));
		bean.setBirthdate(rs.getTimestamp("birthDate").toLocalDateTime());
		bean.setGender(rs.getString("gender"));
		bean.setEstimated(rs.getBoolean("estimated"));
		bean.setAdded(rs.getBoolean("added"));
		return bean;
	}

	/**
	 * Loads parameters from an update
	 */
	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, Childbirth bean,
			boolean newInstance) throws SQLException {
		String stmt = "";
		int i = 1;
		if (newInstance) {
			stmt = "INSERT INTO childbirths"
					+ "(parentID, visitID, birthDate, gender, estimated, added)"
					+ "VALUES(?, ?, ?, ?, ?, ?) ";
			ps = conn.prepareStatement(stmt);
			ps.setLong(i++, bean.getParentID());
			ps.setLong(i++, bean.getVisitID());
			ps.setTimestamp(i++, Timestamp.valueOf(bean.getBirthdate()));
			ps.setString(i++, bean.getGender());
			ps.setBoolean(i++, bean.isEstimated());
			ps.setBoolean(i++, bean.isAdded());
		} else {
			stmt = "UPDATE childbirths SET added=?, birthdate=?, gender=?, estimated=? "
					+ "WHERE visitID=? AND birthID=?";
			ps = conn.prepareStatement(stmt);
			ps.setBoolean(i++, bean.isAdded());
			ps.setTimestamp(i++, Timestamp.valueOf(bean.getBirthdate()));
			ps.setString(i++, bean.getGender());
			ps.setBoolean(i++, bean.isEstimated());
			ps.setLong(i++, bean.getVisitID());
			ps.setInt(i++, bean.getBirthID());
		}
		return ps;
	}

}
