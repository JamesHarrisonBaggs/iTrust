package edu.ncsu.csc.itrust.controller.apptType;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.apptType.*;

@ManagedBean(name="appt_type_controller")
public class ApptTypeController {
	
	private static ApptTypeData apptTypeData;
	
	public ApptTypeController() throws DBException{
		if(apptTypeData == null){
			ApptTypeController.apptTypeData = new ApptTypeMySQLConverter();
			
		}
		
	}
	
	public String getApptTypeForID(String apptTypeID) {
		String apptType = null;
		try {
			List<ApptType> list = apptTypeData.getAll();
			long id = Long.parseLong(apptTypeID);			
			for (ApptType appt : list) {
				if (appt.getID() == id) {
					apptType = appt.getName();
				}
			}			
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Appointment Type ID", "Invalid Appointment Type ID");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		return apptType;
	}
	
	public List<ApptType> getApptTypeList() throws DBException{
		return apptTypeData.getAll();
	}


}
