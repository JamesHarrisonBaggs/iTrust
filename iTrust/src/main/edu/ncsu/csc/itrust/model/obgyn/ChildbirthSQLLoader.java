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

	@Override
	public List<Childbirth> loadList(ResultSet rs) throws SQLException {
		List<Childbirth> list = new ArrayList<Childbirth>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	@Override
	public Childbirth loadSingle(ResultSet rs) throws SQLException {
		Childbirth bean = new Childbirth();
		bean.setParentID(rs.getLong("parentID"));
		bean.setVisitID(rs.getLong("visitID"));
		bean.setBirthID(rs.getInt("birthID"));
		bean.setBirthdate(rs.getTimestamp("birthDate").toLocalDateTime());
		bean.setGender(rs.getString("gender"));
		bean.setEstimated(rs.getBoolean("estimated"));
		return bean;
	}

	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, Childbirth bean,
			boolean newInstance) throws SQLException {
		String statement = "INSERT INTO childbirths(parentID, visitID, birthDate, gender, estimated) "
				+ "VALUES(?, ?, ?, ?, ?) "
				+ "ON DUPLICATE KEY UPDATE parentID=?, visitID=?, birthDate=?, gender=?, estimated=?";
		ps = conn.prepareStatement(statement);

		// set parameters
		int i = 1;
		ps.setLong(i++, bean.getParentID());
		ps.setLong(i++, bean.getVisitID());
		ps.setTimestamp(i++, Timestamp.valueOf(bean.getBirthdate()));
		ps.setString(i++, bean.getGender());
		ps.setBoolean(i++, bean.isEstimated());
		
		// set again for duplicate
		ps.setLong(i++, bean.getParentID());
		ps.setLong(i++, bean.getVisitID());
		ps.setTimestamp(i++, Timestamp.valueOf(bean.getBirthdate()));
		ps.setString(i++, bean.getGender());
		ps.setBoolean(i++, bean.isEstimated());
	
		return ps;
	}

}
