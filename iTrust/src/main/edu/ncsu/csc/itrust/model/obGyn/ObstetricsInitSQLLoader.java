package edu.ncsu.csc.itrust.model.obGyn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import edu.ncsu.csc.itrust.controller.obGyn.ObstetricsInitBean;

/**
 * Interface between SQL objects and ObstetricsInitBean
 * @author erein
 *
 */
public class ObstetricsInitSQLLoader {
	
	//	id				BIGINT			UNSIGNED DEFAULT 0,
	//	init_date		TIMESTAMP		NOT NULL DEFAULT CURRENT_TIMESTAMP,
	//	current			BOOLEAN			NOT NULL DEFAULT FALSE,
	//	lmp_date		TIMESTAMP		NOT NULL,
	
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
		ps.setBoolean(i++, bean.isCurrent());
		ps.setTimestamp(i++, bean.getLMPTimestamp());
		return ps;
	}
	
	/**
	 * For database queries.
	 * Loads data from a ResultsSet to an Obstetrics Bean
	 */
	public ObstetricsInitBean loadResults(ResultSet results) throws SQLException {
		ObstetricsInitBean bean = new ObstetricsInitBean();
		bean.setPatientId(results.getLong("id"));
		bean.setInitDateTimestamp(results.getTimestamp("init_date"));
		bean.setCurrent(results.getBoolean("current"));
		bean.setLMPTimestamp(results.getTimestamp("lmp_date"));
		return bean;
	}

}
