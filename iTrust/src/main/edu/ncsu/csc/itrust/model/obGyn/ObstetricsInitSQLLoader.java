package edu.ncsu.csc.itrust.model.obGyn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Interface between SQL objects and ObstetricsInitBean
 * @author erein
 *
 */
public class ObstetricsInitSQLLoader {
	
	private static ObstetricsInitSQLLoader instance;
	
	/**
	 * Returns the singleton instance of ObstetricsInitSQLLoader
	 */
	public static ObstetricsInitSQLLoader getInstance() {
		if (instance == null) instance = new ObstetricsInitSQLLoader();
		return instance;
	}
	
	/**
	 * For database updates.
	 * Loads data from an Obstetrics Bean to a PreparedStatement
	 */
	public PreparedStatement loadUpdate(PreparedStatement ps, ObstetricsInitBean bean) throws SQLException {
		int i = 1;
		ps.setLong(i++, bean.getPatientId());
		Timestamp current = new Timestamp(Calendar.getInstance().getTime().getTime());
		ps.setTimestamp(i++, current);
		ps.setTimestamp(i++, bean.getLMPTimestamp());
		ps.setBoolean(i++, bean.isCurrent());
		
		// set again for duplicate
		ps.setLong(i++, bean.getPatientId());
		ps.setTimestamp(i++, current);
		ps.setTimestamp(i++, bean.getLMPTimestamp());
		ps.setBoolean(i++, bean.isCurrent());
		
		return ps;
	}
	
	/**
	 * For database queries.
	 * Loads data from a ResultsSet to an Obstetrics Bean
	 */
	public ObstetricsInitBean loadResults(ResultSet results) throws SQLException {
		ObstetricsInitBean bean = new ObstetricsInitBean();
		bean.setPatientId(results.getLong("id"));
		bean.setInitTimestamp(results.getTimestamp("init_date"));
		bean.setLMPTimestamp(results.getTimestamp("lmp_date"));
		bean.setCurrent(results.getBoolean("current"));
		return bean;
	}

}
