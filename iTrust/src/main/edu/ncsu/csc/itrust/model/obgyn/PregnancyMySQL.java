package edu.ncsu.csc.itrust.model.obgyn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * 
 * @author erein
 *
 */
public class PregnancyMySQL {

	private DAOFactory factory;
	private PregnancySQLLoader loader;
	private PregnancyValidator validator;
	
	/**
	 * Constructs a new PregnancyMySQL
	 */
	public PregnancyMySQL(DAOFactory factory) {
		this.factory = factory;
		this.loader = new PregnancySQLLoader();
		this.validator = new PregnancyValidator();
	}
	
	/**
	 * Returns all pregnancies for a patient
	 */
	public List<Pregnancy> getByID(long id) throws DBException {
		ArrayList<Pregnancy> beans = new ArrayList<Pregnancy>();
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM pregnancies WHERE id = ?")) {
			stmt.setLong(1, id);
			final ResultSet results = stmt.executeQuery();
			while (results.next()) {
				beans.add(loader.loadResults(results));
			}
			results.close();
		} catch (SQLException e) {
			throw new DBException(e);
		}
		return beans;
	}
	
	/**
	 * Returns all pregnancies for a patient before a specific date
	 */
	public List<Pregnancy> getByDate(long id, Timestamp date) throws DBException {
		ArrayList<Pregnancy> beans = new ArrayList<Pregnancy>();
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM pregnancies WHERE id = ? AND birth_date < ?")) {
			stmt.setLong(1, id);
			stmt.setTimestamp(2, date);
			final ResultSet results = stmt.executeQuery();
			while (results.next()) {
				beans.add(loader.loadResults(results));
			}
			results.close();
		} catch (SQLException e) {
			throw new DBException(e);
		}
		return beans;
	}
	
	/**
	 * Adds or updates an obstetrics record in the database
	 */
	public int update(Pregnancy bean) throws DBException, FormValidationException {
		validator.validate(bean);
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = loader.loadUpdate(conn.prepareStatement(""
						+ "INSERT INTO pregnancies "
						+ "(id, birth_date, conception_year, days_pregnant, "
						+ "hours_labor, weight_gain, delivery_type, amount) "
						+ "VALUES(?,?,?,?,?,?,?,?) "
						+ "ON DUPLICATE KEY UPDATE "
						+ "id=?, birth_date=?, conception_year=?, days_pregnant=?, "
						+ "hours_labor=?, weight_gain=?, delivery_type=?, amount=?"), bean)) {
			int result = stmt.executeUpdate();
			return result;
		} catch (SQLException e) {
			throw new DBException(e);
		}		
	}
	
}
