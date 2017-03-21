package edu.ncsu.csc.itrust.controller.obGyn;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.obGyn.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

@ManagedBean(name = "obgyn_controller")
public class ObstetricsController extends iTrustController {
	private DAOFactory factory;
	private ObstetricsInitMySQL databaseObInit;
	private List<ObstetricsInitBean> dataListObInit;


	public ObstetricsController() throws DBException {
		super();
		this.factory = DAOFactory.getProductionInstance();
		this.databaseObInit = factory.getObInitDataSQL();
		dataListObInit = new ArrayList<ObstetricsInitBean>();
	}
}