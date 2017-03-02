package edu.ncsu.csc.itrust.model.healthtracker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interfaces between SQL objects and the Health Tracker Bean
 * 
 * @author ereinst
 *
 */
public class HealthTrackerSQLLoader {
	
	/**
	 * FOR DATABASE UPDATES
	 * Loads data from a Bean to a SQL PreparedStatement
	 * @param update - true if updating, false if adding
	 */
	public PreparedStatement loadUpdate(PreparedStatement ps, HealthTrackerBean ht) throws SQLException {
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
	
	/**
	 * FOR DATABASE QUERIES
	 * Loads data from a SQL ResultsSet into a Bean
	 */
	public HealthTrackerBean loadResults(ResultSet results) throws SQLException {
		HealthTrackerBean ht = new HealthTrackerBean();
		ht.setPatientId(results.getLong("id"));
		ht.setTimestamp(results.getTimestamp("data_date"));
		ht.setCalories(results.getInt("calories"));
		ht.setSteps(results.getInt("steps"));
		ht.setDistance(results.getDouble("distance"));
		ht.setFloors(results.getInt("floors"));
		ht.setMinutesSedentary(results.getInt("mins_sed"));
		ht.setMinutesLightlyActive(results.getInt("mins_light"));
		ht.setMinutesFairlyActive(results.getInt("mins_fair"));
		ht.setMinutesVeryActive(results.getInt("mins_very"));
		ht.setActivityCalories(results.getInt("act_cals"));
		ht.setActiveHours(results.getInt("act_hours"));
		ht.setHeartrateLow(results.getInt("hr_low"));
		ht.setHeartrateHigh(results.getInt("hr_high"));
		ht.setHeartrateAverage(results.getInt("hr_avg"));
		ht.setUvExposure(results.getInt("uv_exp"));				
		return ht;
	}

}
