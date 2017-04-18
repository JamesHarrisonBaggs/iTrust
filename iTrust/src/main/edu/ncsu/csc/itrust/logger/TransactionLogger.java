package edu.ncsu.csc.itrust.logger;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

/**
 * Singleton provider of the transaction logging mechanism.
 * 
 * @author mwreesjo
 *
 */
public class TransactionLogger {

	/** The DAO which exposes logging functionality */
	TransactionDAO dao;
	
	private TransactionLogger() {
		dao = DAOFactory.getProductionInstance().getTransactionDAO();
	}

	private TransactionLogger(DAOFactory factory) {
		dao = factory.getTransactionDAO();
	}
	
	/**
	 * @return A new transaction logging mechanism.
	 * WARNING: Constructs using the production instance of DAOFactory
	 */
	public static synchronized TransactionLogger getInstance() {
		return new TransactionLogger();
	}
	
	/**
	 * @return A new transaction logging mechanism.
	 * Constructs using a specific DAOFactory (can use inject TestDAOFactory)
	 */
	public static synchronized TransactionLogger getInstance(DAOFactory factory) {
		if (factory != null)
			return new TransactionLogger(factory);
		return new TransactionLogger();
	}

	/**
	 * Logs a transaction. @see {@link TransactionDAO#logTransaction}
	 */
	public void logTransaction(TransactionType type, Long loggedInMID, Long secondaryMID, String addedInfo) {
		try {
			dao.logTransaction(type, loggedInMID, secondaryMID, addedInfo);
		} catch (DBException e) {
			e.printStackTrace();
		}
	}
}