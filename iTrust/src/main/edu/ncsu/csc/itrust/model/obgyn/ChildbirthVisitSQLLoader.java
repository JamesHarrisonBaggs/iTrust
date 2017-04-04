package edu.ncsu.csc.itrust.model.obgyn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.SQLLoader;

public class ChildbirthVisitSQLLoader implements SQLLoader<ChildbirthVisit> {

	@Override
	public List<ChildbirthVisit> loadList(ResultSet rs) throws SQLException {
		List<ChildbirthVisit> list = new ArrayList<ChildbirthVisit>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	@Override
	public ChildbirthVisit loadSingle(ResultSet rs) throws SQLException {
		ChildbirthVisit bean = new ChildbirthVisit();
		bean.setPatientID(rs.getLong("patientID"));
		bean.setVisitID(rs.getLong("visitID"));
		bean.setVisitDate(rs.getTimestamp("visitDate").toLocalDateTime().toLocalDate());
		bean.setPreSchedule(rs.getBoolean("preScheduled"));
		bean.setDeliveryType(rs.getString("deliveryType"));
		bean.setPitocin(rs.getInt("pitocin"));
		bean.setNitrousOxide(rs.getInt("nitrousOxide"));
		bean.setPethidine(rs.getInt("pethidine"));
		bean.setEpiduralAnaesthesia(rs.getInt("epiduralAnaesthesia"));
		bean.setMagnesiumSO4(rs.getInt("magnesiumSO4"));
		return bean;
	}
	
	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, ChildbirthVisit bean,
			boolean newInstance) throws SQLException {
		String statement = "INSERT INTO childbirths(patientID, visitID, visitDate, preScheduled, "
				+ "deliveryType, pitocin, nitrousOxide, pethidine, epiduralAnaesthesia, magnesiumSO4) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
				+ "ON DUPLICATE KEY UPDATE patientID=?, visitID=?, visitDate=?, preScheduled=?," 
				+ "deliveryType=?, pitocin=?, nitrousOxide=?, pethidine=?, epiduralAnaesthesia=?, magnesiumSO4=?";
		ps = conn.prepareStatement(statement);
		
		// set parameters
		int i = 1;
		ps.setLong(i++, bean.getPatientID());
		ps.setLong(i++, bean.getVisitID());
		ps.setTimestamp(i++, Timestamp.valueOf(bean.getVisitDate().atStartOfDay()));
		ps.setBoolean(i++, bean.isPreSchedule());
		ps.setString(i++, bean.getDeliveryType());
		ps.setInt(i++, bean.getPitocin());
		ps.setInt(i++, bean.getNitrousOxide());
		ps.setInt(i++, bean.getPethidine());
		ps.setInt(i++, bean.getEpiduralAnaesthesia());
		ps.setInt(i++, bean.getMagnesiumSO4());

		// set again for duplicate
		ps.setLong(i++, bean.getPatientID());
		ps.setLong(i++, bean.getVisitID());
		ps.setTimestamp(i++, Timestamp.valueOf(bean.getVisitDate().atStartOfDay()));
		ps.setBoolean(i++, bean.isPreSchedule());
		ps.setString(i++, bean.getDeliveryType());
		ps.setInt(i++, bean.getPitocin());
		ps.setInt(i++, bean.getNitrousOxide());
		ps.setInt(i++, bean.getPethidine());
		ps.setInt(i++, bean.getEpiduralAnaesthesia());
		ps.setInt(i++, bean.getMagnesiumSO4());
		
		return ps;
	}

}
