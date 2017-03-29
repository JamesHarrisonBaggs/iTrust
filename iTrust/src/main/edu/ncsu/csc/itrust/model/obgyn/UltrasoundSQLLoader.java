package edu.ncsu.csc.itrust.model.obgyn;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.SQLLoader;

public class UltrasoundSQLLoader implements SQLLoader<Ultrasound> {
	
	@Override
	public List<Ultrasound> loadList(ResultSet rs) throws SQLException {
		List<Ultrasound> list = new ArrayList<Ultrasound>();
		while(rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}
	
	@Override
	public Ultrasound loadSingle(ResultSet rs) throws SQLException {
		Ultrasound bean = new Ultrasound();
		bean.setPatientId(rs.getLong("id"));
		bean.setVisitId(rs.getLong("visitID"));
		bean.setVisitDate(rs.getTimestamp("visitDate").toLocalDateTime());
		bean.setFetusId(rs.getInt("fetus"));
		bean.setCrownRumpLength(rs.getInt("crl"));
		bean.setBiparietalDiameter(rs.getInt("bpd"));
		bean.setHeadCircumference(rs.getInt("hc"));
		bean.setFemurLength(rs.getInt("fl"));
		bean.setOccipitofrontalDiameter(rs.getInt("ofd"));
		bean.setAbdominalCircumference(rs.getInt("ac"));
		bean.setHumerusLength(rs.getInt("hl"));
		bean.setEstimatedFetalWeight(rs.getDouble("efw"));
		Blob hold = rs.getBlob("file");
		if (hold != null){
			bean.setUploadFile(hold.getBinaryStream());
		}
		return bean;
	}
	
	/**
	 * Creates a prepared statement from a bean; used for updates
	 */
	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, Ultrasound bean,
			boolean newInstance) throws SQLException {		
		
		// prepare statement
		String statement = "INSERT INTO ultrasounds(id, visitID, visitDate, fetus, "
				+ "crl, bpd, hc, fl, ofd, ac, hl, efw, file)"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)"
				+ "ON DUPLICATE KEY UPDATE id=?, visitID=?, visitDate=?, fetus=?, "
				+ "crl=?, bpd=?, hc=?, fl=?, ofd=?, ac=?, hl=?, efw=?";
		ps = conn.prepareStatement(statement);

		// set parameters
		int i = 1;
		ps.setLong(i++, bean.getPatientId());
		ps.setLong(i++, bean.getVisitId());
		ps.setTimestamp(i++, Timestamp.valueOf(bean.getVisitDate()));
		ps.setInt(i++, bean.getFetusId());
		ps.setInt(i++, bean.getCrownRumpLength());
		ps.setInt(i++, bean.getBiparietalDiameter());
		ps.setInt(i++, bean.getHeadCircumference());
		ps.setInt(i++, bean.getFemurLength());
		ps.setInt(i++, bean.getOccipitofrontalDiameter());
		ps.setInt(i++, bean.getAbdominalCircumference());
		ps.setInt(i++, bean.getHumerusLength());
		ps.setDouble(i++, bean.getEstimatedFetalWeight());
		ps.setBlob(i++, bean.getUploadFile());
		
		// set again for duplicate
		ps.setLong(i++, bean.getPatientId());
		ps.setLong(i++, bean.getVisitId());
		ps.setTimestamp(i++, Timestamp.valueOf(bean.getVisitDate()));
		ps.setInt(i++, bean.getFetusId());
		ps.setInt(i++, bean.getCrownRumpLength());
		ps.setInt(i++, bean.getBiparietalDiameter());
		ps.setInt(i++, bean.getHeadCircumference());
		ps.setInt(i++, bean.getFemurLength());
		ps.setInt(i++, bean.getOccipitofrontalDiameter());
		ps.setInt(i++, bean.getAbdominalCircumference());
		ps.setInt(i++, bean.getHumerusLength());
		ps.setDouble(i++, bean.getEstimatedFetalWeight());
		
		return ps;
	}
}
