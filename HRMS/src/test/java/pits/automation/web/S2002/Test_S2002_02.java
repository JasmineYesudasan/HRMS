package pits.automation.web.S2002;

import static org.junit.jupiter.api.Assertions.fail;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qaprosoft.carina.core.foundation.utils.R;

import pits.automation.web.hrms.employee.pages.TimeSheetDetailsCalenderBox;
import pits.automation.web.hrms.manager.pages.EmployeeTaskListBox;
import pits.automation.web.hrms.manager.pages.ManagerCalenderBox;
import pits.automation.web.hrms.manager.pages.ManagerLandingPage;
import pits.automation.web.hrms.manager.pages.ManagerTaskDetailsMenuBox;
import pits.automation.web.hrms.manager.pages.RejectTaskBox;
import pits.automation.web.hrms.manager.pages.TaskApprovalListBox;
import pits.automation.web.hrms.pages.HeaderBox;
import pits.automation.web.hrms.pages.TimesheetLandingPage;
import pits.automation.web.login.BaseTest;

public class Test_S2002_02 extends BaseTest {

	// HPB_28 - [CALENDAR REFINEMENT] - Task resume limitation in calendar view

	@Test(priority = 1)
	public void test_HPB28() {

		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
		TimeSheetDetailsCalenderBox timeSheetDetailsCalenderBox = timesheetLandingPage.getTimeSheetDetailsCalenderBox();

		timesheetLandingPage.toggleToCalender();
		Assert.assertTrue(timeSheetDetailsCalenderBox.getTimeSheetEntry().isPresent(),
				"There are no timesheet entries for this month");

		timeSheetDetailsCalenderBox.verifyTheTextOfTimeSheeTDisplayed();

	}

	// HPB_27 - [TASK APPROVAL] List of tasks to be approved by manager
	@Test(priority = 2)
	public void test_HPB27() throws InterruptedException, ParseException {

		TimesheetLandingPage timesheetLandingPage = managerLogin(R.TESTDATA.get("managerLogin.militaryIdTwo"),
				R.TESTDATA.get("managerLogin.password"));
		HeaderBox header = timesheetLandingPage.getHeaderBox();

		// Verify the user has manager privileges
		Assert.assertTrue(header.getManagerButton().isVisible(),
				" The logged in user '" + R.TESTDATA.get("managerLogin.militaryId") + "' does not have a manager role");

		ManagerLandingPage managerLandingPage = header.clickManagerButton();

		Thread.sleep(4000);

		TaskApprovalListBox taskApprovalListBox = managerLandingPage.getTaskApprovalListBox();
		EmployeeTaskListBox employeeTaskListBox = managerLandingPage.getEmployeeTaskList();

		// Verify the employees are listed in alphabetical order

		boolean isSorted = taskApprovalListBox.verifyEmployeeNamesAreSorted();
		Assert.assertTrue(isSorted, "The employees are not listed in Alphabetical Order");

		boolean isEmployeeAvailable = taskApprovalListBox
				.expandTheTaskDetailsOfParticularEmployee(R.TESTDATA.get("employee.employeeName"));

		Assert.assertTrue(isEmployeeAvailable, "The employee :" + R.TESTDATA.get("employee.employeeName")
				+ " is not available in the list of the manager user.");
		// Verify the task details
		ArrayList<String> headerElements = employeeTaskListBox.fetchEmployeeTaskDetailsHeaderElements();
		for (String headerElement : headerElements) {
			Assert.assertTrue(headerElement.contains("Task ID"), "Task ID is not available in the task details");
			Assert.assertTrue(headerElement.contains("Task Description"),
					"Task Description is not available in the task details");
			Assert.assertTrue(headerElement.contains("Time Taken"), "Time Taken is not available in the task details");

		}

		boolean isCountCorrectlyDisplayed = employeeTaskListBox.verifyCountOfTaskDisplayedForTheEmployee();

		Assert.assertTrue(isCountCorrectlyDisplayed,
				"The count of tasks displayed for the employee is not as expected.");

		employeeTaskListBox.verifyApproveAndRejectButtonsAreAvailableForEachTask();
		boolean areDatesInAscendingOrder = employeeTaskListBox.verifyTaskDatesAreSorted();
		Assert.assertTrue(areDatesInAscendingOrder, "The task dates displayed are not in ascending order");

	}

	// HPB_26 - [TASK APPROVAL] Approve / Reject the tasks in the manager calendar

	@Test
	public void test_HPB26_ApproveTask() throws InterruptedException {

		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
		TimeSheetDetailsCalenderBox timeSheetDetailsCalenderBox = timesheetLandingPage.getTimeSheetDetailsCalenderBox();

		// Toggle to calender view and verify timesheet entries are present for the
		// particular month
		timesheetLandingPage.toggleToCalender();
		Assert.assertTrue(timeSheetDetailsCalenderBox.getTimeSheetEntry().isPresent(),
				"There are no timesheet entries for this month");

		// Filter entries in pending status and verify entries are available in past
		// dates
		timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));
		Assert.assertTrue(timeSheetDetailsCalenderBox.getFirstEntryInPendingStatus().isPresent(),
				"There are no past date timesheet entries in pending status for this month");

		String task = timeSheetDetailsCalenderBox.getFirstEntryInPendingStatus().getText();

		// Submitting the task
		timeSheetDetailsCalenderBox.submitEntryFromCalenderMode();

		// Verifying the success alert
		Assert.assertTrue(timesheetLandingPage.getSuccessAlertOnSubmittingTimesheetInCalenderMode().isPresent(),
				"Success Alert is not displayed on submitting the timesheet. The timesheet has not been submitted");

		Assert.assertTrue(
				timesheetLandingPage.getSuccessAlertOnSubmittingTimesheetInCalenderMode().getText()
						.equals("Task Submitted successfully"),
				"The message in the success alert differs from the expected one");

		// Login as Manager
		TimesheetLandingPage timesheetLandingpage = managerLogin(R.TESTDATA.get("managerLogin.militaryId"),
				R.TESTDATA.get("managerLogin.password"));
		HeaderBox header = timesheetLandingpage.getHeaderBox();

		ManagerLandingPage managerLandingPage = header.clickManagerButton();
		ManagerCalenderBox managerCalenderBox = managerLandingPage.getManagerCalenderBox();

		// Toggle to calender View
		timesheetLandingpage.toggleToCalender();

		// Select the required employee from the list of employees mapped under the
		// manager
		boolean isEmployeeSelected = managerCalenderBox.selectEmployee(R.TESTDATA.get("selectEmployee.employeeName"));
		Assert.assertTrue(isEmployeeSelected, "The employee " + R.TESTDATA.get("selectEmployee.employeeName")
				+ " is not available for the manager user");
		Thread.sleep(2000);

		// Approve the task submitted by the employee in the previous steps
		boolean isTaskFound = managerCalenderBox.selectTimeSheetToApprove(task);
		Assert.assertTrue(isTaskFound,
				" The task with text '" + task + "' is not available in the list for the manager.");

		managerLandingPage.getTaskDetailsMenuBox().clickApproveTaskButton();

		// verify the success alert
		Assert.assertTrue(managerLandingPage.getSuccessAlertOnApprovingTimesheet().isVisible(),
				"Success Alert is not displayed on approving the timesheet.");

		Assert.assertTrue(managerLandingPage.getSuccessAlertOnApprovingTimesheet().getText().equals("Task Approved"),
				"Success Message on approving the timesheet is not as expected.");

		// Login as employee
		employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"), R.TESTDATA.get("employeeLogin.password"));
		timesheetLandingPage.toggleToCalender();

		// Filter approved entries and verify the task approved by the manager is
		// available in the list
		timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.approved"));
		Assert.assertTrue(timeSheetDetailsCalenderBox.getTimeSheetEntry().isPresent(),
				"There are no timesheet entries in approved status for this month.");

		boolean isTaskApproved = timeSheetDetailsCalenderBox.verifyApprovedTask(task);

		Assert.assertTrue(isTaskApproved,
				"The task approved by manager is not available in the list of approved tasks of the employee.");

		// Verify the colour of approved tasks
		ArrayList<String> classOfFilteredEntries = timeSheetDetailsCalenderBox.fetchClassOfFilteredTimeSheetEntries();

		for (String classOption : classOfFilteredEntries) {
			Assert.assertTrue(classOption.contains("green"), "The colour of filtered approved entries is not green");
		}
	}

	@Test
	public void test_HPB26_RejectTaskInCalenderView() throws InterruptedException {

		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
		TimeSheetDetailsCalenderBox timeSheetDetailsCalenderBox = timesheetLandingPage.getTimeSheetDetailsCalenderBox();

		// Toggle to calender view and verify timesheet entries are present for the
		// particular month
		timesheetLandingPage.toggleToCalender();
		Assert.assertTrue(timeSheetDetailsCalenderBox.getTimeSheetEntry().isPresent(),
				"There are no timesheet entries for this month");

		timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));

		Assert.assertTrue(timeSheetDetailsCalenderBox.getFirstEntryInPendingStatus().isVisible(),
				"There are no past date timesheet entries in pending status for this month");

		String task = timeSheetDetailsCalenderBox.getFirstEntryInPendingStatus().getText();

		// Submitting the task
		timeSheetDetailsCalenderBox.submitEntryFromCalenderMode();

		// Verifying the success alert
		Assert.assertTrue(timesheetLandingPage.getSuccessAlertOnSubmittingTimesheetInCalenderMode().isPresent(),
				"Success Alert is not displayed on submitting the timesheet. The timesheet has not been submitted");

		Assert.assertTrue(
				timesheetLandingPage.getSuccessAlertOnSubmittingTimesheetInCalenderMode().getText()
						.equals("Task Submitted successfully"),
				"The message in the success alert differs from the expected one");

		// Login as Manager
		TimesheetLandingPage timesheetLandingpage = managerLogin(R.TESTDATA.get("managerLogin.militaryId"),
				R.TESTDATA.get("managerLogin.password"));
		HeaderBox header = timesheetLandingpage.getHeaderBox();
		Assert.assertTrue(header.getManagerButton().isVisible(),
				" The logged in user '" + R.TESTDATA.get("managerLogin.militaryId") + "' does not have a manager role");

		ManagerLandingPage managerLandingPage = header.clickManagerButton();
		ManagerCalenderBox managerCalenderBox = managerLandingPage.getManagerCalenderBox();

		timesheetLandingpage.toggleToCalender();

		// Select the required employee from the list of employees mapped under the
		// manager
		boolean isEmployeeSelected = managerCalenderBox.selectEmployee(R.TESTDATA.get("selectEmployee.employeeName"));
		Assert.assertTrue(isEmployeeSelected, "The employee " + R.TESTDATA.get("selectEmployee.employeeName")
				+ " is not available for the manager user");
		Thread.sleep(2000);

		boolean isTaskFound = managerCalenderBox.selectTimeSheetToApprove(task);
		Assert.assertTrue(isTaskFound,
				" The task with text '" + task + "' is not available in the list for the manager");

		ManagerTaskDetailsMenuBox taskDetailsMenuBox = managerLandingPage.getTaskDetailsMenuBox();

		// Reject the task submitted by the employee in the previous steps

		taskDetailsMenuBox.clickRejectTaskButton();
		RejectTaskBox rejectTaskBox = taskDetailsMenuBox.clickYesButton();

		rejectTaskBox.clickSubmitButton();

		Assert.assertTrue(rejectTaskBox.getMandatoryAlert().isPresent(),
				"Manager is able to Reject the task without providing the reason");
		rejectTaskBox.provideReasontoRejectTheTask(R.TESTDATA.get("provideReasontoRejectTheTask.reason"));

		rejectTaskBox.clickSubmitButton();

		// Verify the success alert
		Assert.assertTrue(managerLandingPage.getSuccessAlertOnRejectingTimesheet().isPresent(),
				"Success Alert is not displayed on rejecting the timesheet");

		Assert.assertTrue(
				managerLandingPage.getSuccessAlertOnRejectingTimesheet().getText().equals("Task Rejected successfully"),
				"The success message on rejecting the timesheet is not as expected");

		// Login as employee
		employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"), R.TESTDATA.get("employeeLogin.password"));
		timesheetLandingPage.toggleToCalender();

		// Filter approved entries and verify the task approved by the manager is
		// available in the list
		timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.rejected"));
		Assert.assertTrue(timeSheetDetailsCalenderBox.getTimeSheetEntry().isPresent(),
				"There are no timesheet entries in rejected status for this month.");

		boolean isTaskRejected = timeSheetDetailsCalenderBox.verifyTaskIsPresentInCalenderView(task);

		// Verify the colour of rejected tasks

		Assert.assertTrue(isTaskRejected,
				"The task rejected by manager is not available in the list of approved tasks of the employee.");
		ArrayList<String> classOfFilteredEntries = timeSheetDetailsCalenderBox.fetchClassOfFilteredTimeSheetEntries();

		for (String classOption : classOfFilteredEntries) {
			Assert.assertTrue(classOption.contains("red"), "The colour of filtered rejected entries is not red");
		}
	}

	// HPB-29 - [TASK APPROVAL] Managers Approval Calendar

	@Test(priority = 5)
	public void test_HPB29() throws InterruptedException {

		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
		TimeSheetDetailsCalenderBox timeSheetDetailsCalenderBox = timesheetLandingPage.getTimeSheetDetailsCalenderBox();

		timesheetLandingPage.toggleToCalender();

		// Fetch the submitted tasks of the employee for the month
		timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.submitted"));
		Assert.assertTrue(timeSheetDetailsCalenderBox.getTimeSheetEntry().isPresent(),
				"There are no timesheet entries in submitted status for this month for the employee.");

		List<String> submittedTasksOfEmployee = timeSheetDetailsCalenderBox.fetchTimesheetOfEmployee();

		TimesheetLandingPage timesheetLandingpage = managerLogin(R.TESTDATA.get("managerLogin.militaryId"),
				R.TESTDATA.get("managersLogin.password"));
		HeaderBox header = timesheetLandingpage.getHeaderBox();

		Assert.assertTrue(header.getManagerButton().isVisible(),
				" The logged in user '" + R.TESTDATA.get("managerLogin.militaryId") + "' does not have a manager role");
		ManagerLandingPage managerLandingPage = header.clickManagerButton();
		ManagerCalenderBox managerCalenderBox = managerLandingPage.getManagerCalenderBox();

		timesheetLandingpage.toggleToCalender();

		// Verify the manager has the dropdown to select the employee
		Assert.assertTrue(managerCalenderBox.getEmployeeListDropDown().isPresent(),
				"Employee Dropdown is not available for the manager user");

		// Select any employee and fetch the submitted tasks of the employee displayed
		// for the manager
		boolean isEmployeeSelected = managerCalenderBox.selectEmployee(R.TESTDATA.get("selectEmployee.employeeName"));
		Assert.assertTrue(isEmployeeSelected, "The employee " + R.TESTDATA.get("selectEmployee.employeeName")
				+ " is not available for the manager user");

		List<String> submittedTasksDisplayedForManager = managerCalenderBox.fetchTheEmployeeTasksDisplayedForManager();

		if (!(submittedTasksOfEmployee.equals(submittedTasksDisplayedForManager))) {
			fail("All the tasks submitted by the employee is not displayed for the manager");
		}

		managerCalenderBox.VerifyTaskDetailsMenuBoxOfManager();

		List<String> classOfTasksDisplayed = managerCalenderBox.fetchClassOfTimeSheetEntriesDisplayedForManager();
		for (String classOption : classOfTasksDisplayed) {
			Assert.assertTrue(classOption.contains("blue"),
					"The colour of submitted tasks displayed for manager is not blue");
		}
	}

}