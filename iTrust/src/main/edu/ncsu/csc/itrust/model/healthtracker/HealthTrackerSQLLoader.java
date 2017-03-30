package edu.ncsu.csc.itrust.model.healthtracker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.SQLLoader;

/**
 * Interfaces between SQL objects and the Health Tracker Bean
 * 
 * @author ereinst
 *
 */
public class HealthTrackerSQLLoader implements SQLLoader<HealthTrackerBean> {
	
	@Override
	public List<HealthTrackerBean> loadList(ResultSet rs) throws SQLException {
		List<HealthTrackerBean> list = new ArrayList<HealthTrackerBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}
	
	/**
	 * FOR DATABASE QUERIES
	 * Loads data from a SQL ResultsSet into a Bean
	 */
	@Override
	public HealthTrackerBean loadSingle(ResultSet rs) throws SQLException {
		HealthTrackerBean bean = new HealthTrackerBean();
		bean.setPatientId(rs.getLong("id"));
		bean.setTimestamp(rs.getTimestamp("data_date"));
		bean.setCalories(rs.getInt("calories"));
		bean.setSteps(rs.getInt("steps"));
		bean.setDistance(rs.getDouble("distance"));
		bean.setFloors(rs.getInt("floors"));
		bean.setMinutesSedentary(rs.getInt("mins_sed"));
		bean.setMinutesLightlyActive(rs.getInt("mins_light"));
		bean.setMinutesFairlyActive(rs.getInt("mins_fair"));
		bean.setMinutesVeryActive(rs.getInt("mins_very"));
		bean.setActivityCalories(rs.getInt("act_cals"));
		bean.setActiveHours(rs.getInt("act_hours"));
		bean.setHeartrateLow(rs.getInt("hr_low"));
		bean.setHeartrateHigh(rs.getInt("hr_high"));
		bean.setHeartrateAverage(rs.getInt("hr_avg"));
		bean.setUvExposure(rs.getInt("uv_exp"));				
		return bean;
	}

	/**
	 * FOR DATABASE UPDATES
	 * Loads data from a Bean to a SQL PreparedStatement
	 */
	 @Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps,
			HealthTrackerBean ht, boolean newInstance) throws SQLException {
		
		String statement = 	"INSERT INTO healthtrackerdata "
						+ "(id, data_date, calories, steps, distance, floors, mins_sed, mins_light, "
						+ "mins_fair, mins_very, act_cals, act_hours, hr_low, hr_high, "
						+ "hr_avg, uv_exp) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) "
						+ "ON DUPLICATE KEY UPDATE "
						+ "calories=?, steps=?, distance=?, floors=?, "
						+ "mins_sed=?, mins_light=?, mins_fair=?, mins_very=?, act_cals=?, act_hours=?, "
						+ "hr_low=?, hr_high=?, hr_avg=?, uv_exp=?";
		ps = conn.prepareStatement(statement);
		
		int i = 1;
		ps.setLong(i++, ht.getPatientId());
		ps.setTimestamp(i++, ht.getTimestamp());
		
		ps.setInt(i++, ht.getCalories());
		ps.setInt(i++, ht.getSteps());
		ps.setDouble(i++, ht.getDistance());
		ps.setInt(i++, ht.getFloors());	
		ps.setInt(i++, ht.getMinutesSedentary());
		ps.setInt(i++, ht.getMinutesLightlyActive());
		ps.setInt(i++, ht.getMinutesFairlyActive());
		ps.setInt(i++, ht.getMinutesVeryActive());
		ps.setInt(i++, ht.getActivityCalories());
		ps.setInt(i++, ht.getActiveHours());
		ps.setInt(i++, ht.getHeartrateLow());
		ps.setInt(i++, ht.getHeartrateHigh());		
		ps.setInt(i++, ht.getHeartrateAverage());
		ps.setInt(i++, ht.getUvExposure());
		
		ps.setInt(i++, ht.getCalories());
		ps.setInt(i++, ht.getSteps());
		ps.setDouble(i++, ht.getDistance());
		ps.setInt(i++, ht.getFloors());	
		ps.setInt(i++, ht.getMinutesSedentary());
		ps.setInt(i++, ht.getMinutesLightlyActive());
		ps.setInt(i++, ht.getMinutesFairlyActive());
		ps.setInt(i++, ht.getMinutesVeryActive());
		ps.setInt(i++, ht.getActivityCalories());
		ps.setInt(i++, ht.getActiveHours());
		ps.setInt(i++, ht.getHeartrateLow());
		ps.setInt(i++, ht.getHeartrateHigh());		
		ps.setInt(i++, ht.getHeartrateAverage());
		ps.setInt(i++, ht.getUvExposure());
		
		return ps;
	}
	
}
