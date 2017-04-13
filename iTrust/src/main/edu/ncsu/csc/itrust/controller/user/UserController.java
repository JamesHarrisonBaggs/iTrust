package edu.ncsu.csc.itrust.controller.user;

import javax.faces.bean.ManagedBean;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;
import edu.ncsu.csc.itrust.model.old.enums.Role;
import edu.ncsu.csc.itrust.model.user.User;
import edu.ncsu.csc.itrust.model.user.UserMySQLConverter;

@ManagedBean(name="user")
public class UserController {
	private DataBean<User> userData;
	
	public UserController() throws DBException{
		userData = new UserMySQLConverter();	
	}
	
	public String getUserNameForID(String mid) throws DBException {
		User user = getUser(mid, true);
		if (user == null) return "";
		if (user.getRole().equals(Role.TESTER)){
			return Long.toString(user.getMID());
		} else {
			return user.getLastName().concat(", " + user.getFirstName());
		}			
	}

	public String getUserRoleForID(String mid) throws DBException {
		User user = getUser(mid, false);
		if (user == null) return "";
		return user.getRole().getUserRolesString().toLowerCase();
	}
	
	public boolean doesUserExistWithID(String mid) throws DBException{
		User user = getUser(mid, true);
		return user != null;
	}

	/**
	 * Returns a User associated with an MID
	 * Returns null for an invalid MID or missing patient
	 * @param allowInvalidMIDs - true to search for users with MID < 1
	 */
	private User getUser(String mid, boolean allowInvalidMIDs) throws DBException {
		if (mid == null || mid.isEmpty()) {
			return null;
		}
		long id = -1;
		try {
			id = Long.parseLong(mid);
		} catch (NumberFormatException n) {
			return null;
		}
		if (!allowInvalidMIDs && id < 1) {
			return null;
		}
		return userData.getByID(id);
	}

}
