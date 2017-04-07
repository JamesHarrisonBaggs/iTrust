package edu.ncsu.csc.itrust.model.obgyn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.SQLLoader;

public class ObstetricsInitSQLLoader implements SQLLoader<ObstetricsInit> {
	
	/**
	 * Returns a list of beans from a result set
	 */
	@Override
	public List<ObstetricsInit> loadList(ResultSet rs) throws SQLException {
		List<ObstetricsInit> list = new ArrayList<ObstetricsInit>();
		while(rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}
	
	/**
	 * Maps a row in a results set to a bean
	 */
	@Override
	public ObstetricsInit loadSingle(ResultSet rs) throws SQLException {
		ObstetricsInit bean = new ObstetricsInit();
		bean.setPatientId(rs.getLong("id"));
		bean.setInitDate(rs.getTimestamp("init_date").toLocalDateTime().toLocalDate());
		bean.setLMPTimestamp(rs.getTimestamp("lmp_date"));
		bean.setCurrent(rs.getBoolean("current"));
		return bean;
	}
	
	/**
	 * Creates a prepared statement from a bean; used for updates
	 */
	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, ObstetricsInit bean,
			boolean newInstance) throws SQLException {	
		
		// prepare statement
		String statement = "INSERT INTO obstetrics_inits(id, init_date, lmp_date, current) "
				+ "VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE "
				+ "id=?, init_date=?, lmp_date=?, current=?";
		ps = conn.prepareStatement(statement);
		
		// set parameters
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
	
}
