package edu.ncsu.csc.itrust.model.obgyn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.ncsu.csc.itrust.model.SQLLoader;

public class ObstetricsVisitSQLLoader implements SQLLoader<ObstetricsVisit> {

	/**
	 * Returns a list of beans from a result set
	 */
	@Override
	public List<ObstetricsVisit> loadList(ResultSet rs) throws SQLException {
		List<ObstetricsVisit> list = new ArrayList<ObstetricsVisit>();
		while(rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	/**
	 * Maps a row in a results set to a bean
	 */
	@Override
	public ObstetricsVisit loadSingle(ResultSet rs) throws SQLException {
		ObstetricsVisit bean = new ObstetricsVisit();
		bean.setPatientId(rs.getLong("id"));
		bean.setVisitId(rs.getLong("visitID"));
		bean.setVisitDate(rs.getTimestamp("visitDate").toLocalDateTime());
		bean.setWeeksPregnant(rs.getInt("weeksPregnant"));
		bean.setWeight(rs.getDouble("weight"));
		bean.setBloodPressure(rs.getString("bloodPressure"));
		bean.setFetalHeartRate(rs.getInt("fetalHR"));
		bean.setAmount(rs.getInt("amount"));
		bean.setLowLyingPlacenta(rs.getBoolean("lowLying"));
		bean.setRhFlag(rs.getBoolean("rhFlag"));
		return bean;
	}

	/**
	 * Creates a prepared statement from a bean; used for updates
	 */
	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, ObstetricsVisit bean,
			boolean newInstance) throws SQLException {		
		
		// prepare statement
		String statement = "INSERT INTO obstetrics_visits(id, visitID, visitDate, weeksPregnant, "
				+ "weight, bloodPressure, fetalHR, amount, lowLying, rhFlag)"
				+ "VALUES(?,?,?,?,?,?,?,?,?)"
				+ "ON DUPLICATE KEY UPDATE id=?, visitID=?, visitDate=?, weeksPregnant=?, "
				+ "weight=?, bloodPressure=?, fetalHR=?, amount=?, lowLying=?, rhFlag=?";
		ps = conn.prepareStatement(statement);
		
		// set parameters
		int i = 1;
		ps.setLong(i++, bean.getPatientId());
		ps.setLong(i++, bean.getVisitId());
		ps.setTimestamp(i++, Timestamp.valueOf(bean.getVisitDate()));
		ps.setInt(i++, bean.getWeeksPregnant());
		ps.setDouble(i++, bean.getWeight());
		ps.setString(i++, bean.getBloodPressure());
		ps.setInt(i++, bean.getFetalHeartRate());
		ps.setInt(i++, bean.getAmount());
		ps.setBoolean(i++, bean.isLowLyingPlacenta());
		ps.setBoolean(i++, bean.isRhFlag());
		
		// set again for duplicate
		ps.setLong(i++, bean.getPatientId());
		ps.setLong(i++, bean.getVisitId());
		ps.setTimestamp(i++, Timestamp.valueOf(bean.getVisitDate()));
		ps.setInt(i++, bean.getWeeksPregnant());
		ps.setDouble(i++, bean.getWeight());
		ps.setString(i++, bean.getBloodPressure());
		ps.setInt(i++, bean.getFetalHeartRate());
		ps.setInt(i++, bean.getAmount());
		ps.setBoolean(i++, bean.isLowLyingPlacenta());
		ps.setBoolean(i++, bean.isRhFlag());
		
		return ps;
	}

}
