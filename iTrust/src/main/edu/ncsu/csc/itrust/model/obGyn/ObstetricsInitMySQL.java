package edu.ncsu.csc.itrust.model.obGyn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.controller.obGyn.ObstetricsInitBean;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * 
 * @author erein
 *
 */
public class ObstetricsInitMySQL {
	
	//	id				BIGINT			UNSIGNED DEFAULT 0,
	//	init_date		TIMESTAMP		NOT NULL DEFAULT CURRENT_TIMESTAMP,
	//	current			BOOLEAN			NOT NULL DEFAULT FALSE,
	//	lmp_date		TIMESTAMP		NOT NULL,

	private DAOFactory factory;
	private ObstetricsInitSQLLoader loader;
	private ObstetricsInitValidator validator;
	
	/**
	 * Constructs a new ObstetricsInitMySQL
	 */
	public ObstetricsInitMySQL(DAOFactory factory) {
		this.factory = factory;
		this.loader = ObstetricsInitSQLLoader.getInstance();
		this.validator = ObstetricsInitValidator.getInstance();
	}
	
	/**
	 * Returns all obstetrics records for a patient
	 */
	public List<ObstetricsInitBean> getByID(long id) throws DBException {
		ArrayList<ObstetricsInitBean> beans = new ArrayList<ObstetricsInitBean>();
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM obstetrics WHERE id = ?")) {
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
	 * Adds or updates an obstetrics record in the database
	 */
	public int update(ObstetricsInitBean bean) throws DBException, FormValidationException {
		validator.validate(bean);
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = loader.loadUpdate(conn.prepareStatement(""
						+ "INSERT INTO obstetrics "
						+ "(id, init_date, current, lmp_date) "
						+ "VALUES(?,?,?,?) "
						+ "ON DUPLICATE KEY UPDATE "
						+ "id=?, init_date=?, current=?, lmp_date=?"), bean)) {
			int result = stmt.executeUpdate();
			return result;
		} catch (SQLException e) {
			throw new DBException(e);
		}		
	}
	
}
