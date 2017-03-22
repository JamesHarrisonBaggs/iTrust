package edu.ncsu.csc.itrust.model.obGyn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface between SQL objects and PregnancyBean
 * @author erein
 *
 */
public class PregnancySQLLoader {

	private static PregnancySQLLoader instance;
	
	/**
	 * Returns the singleton instance of PregnancySQLLoader
	 */
	public static PregnancySQLLoader getInstance() {
		if (instance == null) instance = new PregnancySQLLoader();
		return instance;
	}
	
	/**
	 * For database updates.
	 * Loads data from an Obstetrics Bean to a PreparedStatement
	 */
	public PreparedStatement loadUpdate(PreparedStatement ps, PregnancyBean bean) throws SQLException {
		int i = 1;
		ps.setLong(i++, bean.getPatientId());
		ps.setTimestamp(i++, bean.getDOBTimestamp());
		ps.setInt(i++, bean.getYearOfConception());
		ps.setInt(i++, bean.getDaysPregnant());
		ps.setInt(i++, bean.getHoursInLabor());
		ps.setDouble(i++, bean.getWeightGain());
		ps.setString(i++, bean.getDeliveryType());
		ps.setInt(i++, bean.getAmount());

		// set again for duplicate
		ps.setLong(i++, bean.getPatientId());
		ps.setTimestamp(i++, bean.getDOBTimestamp());
		ps.setInt(i++, bean.getYearOfConception());
		ps.setInt(i++, bean.getDaysPregnant());
		ps.setInt(i++, bean.getHoursInLabor());
		ps.setDouble(i++, bean.getWeightGain());
		ps.setString(i++, bean.getDeliveryType());
		ps.setInt(i++, bean.getAmount());

		return ps;
	}
	
	/**
	 * For database queries.
	 * Loads data from a ResultsSet to an Obstetrics Bean
	 */
	public PregnancyBean loadResults(ResultSet results) throws SQLException {
		PregnancyBean bean = new PregnancyBean();
		
		bean.setPatientId(results.getLong("id"));
		bean.setDOBTimestamp(results.getTimestamp("birth_date"));
		bean.setYearOfConception(results.getInt("conception_year"));
		bean.setDaysPregnant(results.getInt("days_pregnant"));
		bean.setHoursInLabor(results.getInt("hours_labor"));
		bean.setWeightGain(results.getDouble("weight_gain"));
		bean.setDeliveryType(results.getString("delivery_type"));
		bean.setAmount(results.getInt("amount"));		
		
		return bean;
	}
	
}