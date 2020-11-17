package pits.automation.web.dbms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;

import com.qaprosoft.carina.core.gui.AbstractUIObject;

public class DataBaseQueries extends AbstractUIObject {
	public DataBaseQueries(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);
	}

	public List<String> fetchTaskTitleFromDatabase(String connectionUrl, String requiredDate, String employeeId) {
		List<String> timeSheetIdList = new ArrayList<String>();
		List<String> taskTitleList = new ArrayList<String>();
		ResultSet rsTaskTitle = null;
		ResultSet rsTimeSheetId = null;
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
			String queryToFetchTimeSheetId = "select TimeSheetID from [HRMS].[trnTimeSheet] where EmployeeID="
					+ employeeId + " and TimeSheetDate ='" + requiredDate + "'";
			rsTimeSheetId = stmt.executeQuery(queryToFetchTimeSheetId);

			while (rsTimeSheetId.next()) {
				timeSheetIdList.add(rsTimeSheetId.getString("TimeSheetID"));
			}

			for (String timesheetId : timeSheetIdList) {
				String queryToFetchTaskDetails = "select * from HRMS.trnTimeSheetDt where TimeSheetID ='" + timesheetId
						+ "'";
				rsTaskTitle = stmt.executeQuery(queryToFetchTaskDetails);
			}
			while (rsTaskTitle.next()) {
				taskTitleList.add(rsTaskTitle.getString("Title"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return taskTitleList;

	}

	public List<String> fetchTaskDetailsFromDataBase(String connectionUrl, String requiredDate, String employeeId) {

		List<String> timeSheetIdList = new ArrayList<String>();
		List<String> taskDetailsList = new ArrayList<String>();
		ResultSet rsTaskDetails = null;
		ResultSet rsTimeSheetId = null;
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
			String queryToFetchTimeSheetId = "select TimeSheetID from [HRMS].[trnTimeSheet] where EmployeeID="
					+ employeeId + " and TimeSheetDate ='" + requiredDate + "'";
			rsTimeSheetId = stmt.executeQuery(queryToFetchTimeSheetId);

			while (rsTimeSheetId.next()) {
				timeSheetIdList.add(rsTimeSheetId.getString("TimeSheetID"));
			}

			for (String timesheetId : timeSheetIdList) {
				String queryToFetchTaskDetails = "select * from HRMS.trnTimeSheetDt where TimeSheetID ='" + timesheetId
						+ "'";
				rsTaskDetails = stmt.executeQuery(queryToFetchTaskDetails);
			}
			while (rsTaskDetails.next()) {
				taskDetailsList.add(rsTaskDetails.getString("TaskID") + rsTaskDetails.getString("TaskDescription")
						+ rsTaskDetails.getString("TimeTaken") + rsTaskDetails.getString("Title"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return taskDetailsList;

	}

}
