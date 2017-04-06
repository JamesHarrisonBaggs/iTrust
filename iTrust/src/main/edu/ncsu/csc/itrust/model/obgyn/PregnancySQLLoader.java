package edu.ncsu.csc.itrust.model.obgyn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.SQLLoader;

public class PregnancySQLLoader implements SQLLoader<Pregnancy> {

	/**
	 * Returns a list of beans from a result set
	 */
	@Override
	public List<Pregnancy> loadList(ResultSet results) throws SQLException {
		List<Pregnancy> list = new ArrayList<Pregnancy>();
		while (results.next()) {
			list.add(loadSingle(results));
		}
		return list;
	}

	/**
	 * Maps a row in a results set to a bean
	 */
	@Override
	public Pregnancy loadSingle(ResultSet results) throws SQLException {
		Pregnancy bean = new Pregnancy();
		bean.setPatientId(results.getLong("id"));
		bean.setDateOfBirth(results.getTimestamp("birth_date").toLocalDateTime().toLocalDate());
		bean.setYearOfConception(results.getInt("conception_year"));
		bean.setDaysPregnant(results.getInt("days_pregnant"));
		bean.setHoursInLabor(results.getInt("hours_labor"));
		bean.setWeightGain(results.getDouble("weight_gain"));
		bean.setDeliveryType(results.getString("delivery_type"));
		bean.setAmount(results.getInt("amount"));		
		return bean;
	}

	/**
	 * Creates a prepared statement from a bean; used for updates
	 */
	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, Pregnancy bean,
			boolean newInstance) throws SQLException {
		
		String statement = "INSERT INTO pregnancies "
				+ "(id, birth_date, conception_year, days_pregnant, "
				+ "hours_labor, weight_gain, delivery_type, amount) "
				+ "VALUES(?,?,?,?,?,?,?,?) "
				+ "ON DUPLICATE KEY UPDATE "
				+ "id=?, birth_date=?, conception_year=?, days_pregnant=?, "
				+ "hours_labor=?, weight_gain=?, delivery_type=?, amount=?";
		ps = conn.prepareStatement(statement);
		
		// set parameters
		int i = 1;
		ps.setLong(i++, bean.getPatientId());
		ps.setTimestamp(i++, Timestamp.valueOf(bean.getDateOfBirth().atStartOfDay()));
		ps.setInt(i++, bean.getYearOfConception());
		ps.setInt(i++, bean.getDaysPregnant());
		ps.setInt(i++, bean.getHoursInLabor());
		ps.setDouble(i++, bean.getWeightGain());
		ps.setString(i++, bean.getDeliveryType());
		ps.setInt(i++, bean.getAmount());

		// set again for duplicate
		ps.setLong(i++, bean.getPatientId());
		ps.setTimestamp(i++, Timestamp.valueOf(bean.getDateOfBirth().atStartOfDay()));
		ps.setInt(i++, bean.getYearOfConception());
		ps.setInt(i++, bean.getDaysPregnant());
		ps.setInt(i++, bean.getHoursInLabor());
		ps.setDouble(i++, bean.getWeightGain());
		ps.setString(i++, bean.getDeliveryType());
		ps.setInt(i++, bean.getAmount());

		return ps;
	}
	
}