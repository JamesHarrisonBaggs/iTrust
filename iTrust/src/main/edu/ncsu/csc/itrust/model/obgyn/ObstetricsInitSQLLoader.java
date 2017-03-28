package edu.ncsu.csc.itrust.model.obgyn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface between SQL objects and ObstetricsInitBean
 * @author erein
 *
 */
public class ObstetricsInitSQLLoader {
	
	/**
	 * For database updates.
	 * Loads data from an Obstetrics Bean to a PreparedStatement
	 */
	public PreparedStatement loadUpdate(PreparedStatement ps, ObstetricsInit bean) throws SQLException {
		int i = 1;
		ps.setLong(i++, bean.getPatientId());
		ps.setTimestamp(i++, bean.getInitTimestamp());
		ps.setTimestamp(i++, bean.getLMPTimestamp());
		ps.setBoolean(i++, bean.isCurrent());
		
		// set again for duplicate
		ps.setLong(i++, bean.getPatientId());
		ps.setTimestamp(i++, bean.getInitTimestamp());
		ps.setTimestamp(i++, bean.getLMPTimestamp());
		ps.setBoolean(i++, bean.isCurrent());
		
		return ps;
	}
	
	/**
	 * For database queries.
	 * Loads data from a ResultsSet to an Obstetrics Bean
	 */
	public ObstetricsInit loadResults(ResultSet results) throws SQLException {
		ObstetricsInit bean = new ObstetricsInit();
		bean.setPatientId(results.getLong("id"));
		bean.setInitTimestamp(results.getTimestamp("init_date"));
		bean.setLMPTimestamp(results.getTimestamp("lmp_date"));
		bean.setCurrent(results.getBoolean("current"));
		return bean;
	}

}
