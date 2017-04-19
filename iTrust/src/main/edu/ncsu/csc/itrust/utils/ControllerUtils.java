package edu.ncsu.csc.itrust.utils;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.user.User;
import edu.ncsu.csc.itrust.model.user.UserMySQLConverter;
import edu.ncsu.csc.itrust.model.user.patient.PatientMySQLConverter;

public class ControllerUtils {
	public static User getUser(String mid, boolean usePatientDB, boolean invalidMIDAllowed) throws DBException {
		if (mid == null || mid.isEmpty()) {
			return null;
		}
		long id = -1;
		try {
			id = Long.parseLong(mid);
		} catch (NumberFormatException n) {
			return null;
		}
		if (!invalidMIDAllowed && id < 1) {
			return null;
		}
		if (usePatientDB) {
			if(!(ValidationFormat.NPMID.getRegex().matcher(mid).matches())) return null;
			PatientMySQLConverter patientData = new PatientMySQLConverter();
			return patientData.getByID(id);
		} else {
			UserMySQLConverter userData = new UserMySQLConverter();	
			return userData.getByID(id);
		}
	}
}
