package edu.ncsu.csc.itrust.controller.healthtracker;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.healthtracker.HealthTrackerBean;

/**
 * Bean providing the data for the Google Chart
 * 
 * @author erein
 *
 */
@ManagedBean(name = "chartBean")
@SessionScoped
public class ChartDataController implements Serializable {

	private static final long serialVersionUID = 1L;
	private String chartData;
	private ArrayList<HealthTrackerBean> dataList;
	private HealthTrackerController htcontrol;
	
	private LocalDate startDate;
	private LocalDate endDate;

	public ChartDataController() throws DBException {
		htcontrol = new HealthTrackerController();
		this.dataList = htcontrol.getAllData();
		refreshChartData();
	}
	
	public void refreshChartData() throws DBException {
		this.chartData = this.getStepData();
	}
	
	/**
	 * Parses the Bean list to create a string version of the data for Google Charts
	 * @return a string version of the step data
	 * @throws NumberFormatException
	 * @throws DBException
	 */
	public String getStepData() throws NumberFormatException, DBException {
		StringBuilder sb = new StringBuilder();
		for (HealthTrackerBean b : dataList) {
			sb.append("[");
			sb.append("'");
			sb.append(b.getDate());
			sb.append("',");
			sb.append(b.getSteps());
			sb.append("],");
		}
		String steps = sb.toString();		
		return steps.length() > 0 ? steps.substring(0, steps.length() - 1) : "";
	}
	
	/**
	 * @throws DBException
	 * @throws IOException
	 */
	public void updateChartRange() throws DBException, IOException {
		// get data in range
		dataList = htcontrol.getDataInRange(Timestamp.valueOf(startDate.atStartOfDay()), Timestamp.valueOf(endDate.atStartOfDay()));
		// refresh chart data
		refreshChartData();
		// reload page
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}
	
	/**
	 * @return the chartData
	 */
	public String getChartData() {
		return chartData;
	}

	/**
	 * @param chartData the chartData to set
	 */
	public void setChartData(String chartData) {
		this.chartData = chartData;
	}

	/**
	 * @return the dataList
	 */
	public ArrayList<HealthTrackerBean> getDataList() {
		return dataList;
	}

	/**
	 * @param dataList the dataList to set
	 */
	public void setDataList(ArrayList<HealthTrackerBean> dataList) {
		this.dataList = dataList;
	}

	/**
	 * @return the startDate
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * @return the endDate
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
}