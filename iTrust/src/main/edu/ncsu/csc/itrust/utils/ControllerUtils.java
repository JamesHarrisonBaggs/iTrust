package edu.ncsu.csc.itrust.utils;

import java.sql.SQLException;

import javax.faces.application.FacesMessage;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.medicalProcedure.MedicalProcedure;
import edu.ncsu.csc.itrust.model.medicalProcedure.MedicalProcedureMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.prescription.Prescription;
import edu.ncsu.csc.itrust.model.prescription.PrescriptionMySQL;
import edu.ncsu.csc.itrust.model.user.User;
import edu.ncsu.csc.itrust.model.user.UserMySQLConverter;
import edu.ncsu.csc.itrust.model.user.patient.PatientMySQLConverter;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

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
	
	public static void add(PrescriptionMySQL psql, MedicalProcedureMySQL mpsql, Prescription prescription, MedicalProcedure medicalProcedure) {
		String message = "";
		SessionUtils session = SessionUtils.getInstance();
		iTrustController controller = new iTrustController();
		try {
			if (psql != null && prescription != null) {
				message = "Invalid Prescription";
				if (psql.add(prescription)) {
					session.printFacesMessage(FacesMessage.SEVERITY_INFO, "Prescription is successfully created",
							"Prescription is successfully created", null);
					controller.logTransaction(TransactionType.PRESCRIPTION_EDIT, session.getCurrentOfficeVisitId().toString());
				}
			} else {
				message = "Invalid Medical Procedure";
				if (mpsql.add(medicalProcedure)) {
					session.printFacesMessage(FacesMessage.SEVERITY_INFO, "Medical Procedure successfully created",
							"Medical Procedure successfully created", null);
					Long ovid = session.getCurrentOfficeVisitId();
					controller.logTransaction(TransactionType.PROCEDURE_ADD, ovid == null ? null : ovid.toString());
				}
			}
		} catch (SQLException e) {
			session.printFacesMessage(FacesMessage.SEVERITY_ERROR, message, e.getMessage(), null);
		} catch (Exception e) {
			session.printFacesMessage(FacesMessage.SEVERITY_ERROR, message, message, null);
		}
	}
}
