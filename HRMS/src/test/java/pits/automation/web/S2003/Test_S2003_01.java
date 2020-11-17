package pits.automation.web.S2003;

import static org.junit.jupiter.api.Assertions.fail;

import java.text.ParseException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;

import pits.automation.web.hrms.employee.pages.EmployeeTaskDetailsMenuBox;
import pits.automation.web.hrms.employee.pages.RemoveConfirmationPopUp;
import pits.automation.web.hrms.employee.pages.TimeSheetDetailsCalenderBox;
import pits.automation.web.hrms.employee.pages.TimeSheetDetailsGridBox;
import pits.automation.web.hrms.manager.pages.ManagerCalenderBox;
import pits.automation.web.hrms.manager.pages.ManagerLandingPage;
import pits.automation.web.hrms.manager.pages.ManagerTaskDetailsMenuBox;
import pits.automation.web.hrms.manager.pages.RejectTaskBox;
import pits.automation.web.hrms.manager.pages.TaskApprovalListBox;
import pits.automation.web.hrms.pages.HeaderBox;
import pits.automation.web.hrms.pages.TimesheetLandingPage;
import pits.automation.web.login.BaseTest;

public class Test_S2003_01 extends BaseTest {

	// HPB_30 - [TASK APPROVAL] Approve / Reject the tasks in the approval list

	// Scope - The scope of this testcase is to approve a particular task
	// that has been submitted by an employee to the manager in the approval list
	// and verify the same.

	@Test(priority = 0)
	public void testVerifyApprovingATaskFromGridView() throws InterruptedException {

		// Login as employee
		TimesheetLandingPage timesheetLandingPageEmployee = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
		Assert.assertTrue(timesheetLandingPageEmployee.isPageOpened(), "Login is not successfull for the employee ' : "
				+ R.TESTDATA.get("employeeLogin.militaryId") + "'. The required landing page is not opened on login.");
		TimeSheetDetailsGridBox timesheetDetailsGridBox = timesheetLandingPageEmployee.getTimeSheetDetailsGridBox();

		// Fetch the TaskId list of the employee in submitted status
		boolean isFilterApplied = timesheetLandingPageEmployee
				.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.submitted"));

		Assert.assertTrue(isFilterApplied, "The required filter :" + R.TESTDATA.get("selectFilter.submitted")
				+ " is not available in the filter options list.");

		List<String> listOfSubmittedTaskIdOfEmployee = timesheetDetailsGridBox
				.fetchTaskIdOfEmployeeTasksDisplayedInGridView();

		if (listOfSubmittedTaskIdOfEmployee.size() == 0) {
			fail("There are no tasks for the employee in submitted status. Please add a timesheet and submit it.");
		}

		// Login as Manager
		TimesheetLandingPage timesheetLandingPage = managerLogin(R.TESTDATA.get("managerLogin.militaryId"),
				R.TESTDATA.get("managerLogin.password"));
		HeaderBox header = timesheetLandingPage.getHeaderBox();

		// Verify the user has manager privileges
		Assert.assertTrue(header.getManagerButton().isVisible(),
				" The user '" + R.TESTDATA.get("managerLogin.militaryId") + "' does not have a manager role.");

		ManagerLandingPage managerLandingPage = header.clickManagerButton();
		TaskApprovalListBox taskApprovalListBox = managerLandingPage.getTaskApprovalListBox();

		// Select the required employee and fetch the details of tasks displayed for
		// that employee
		boolean isEmployeeSelected = taskApprovalListBox
				.selectTheRequiredEmployee(R.TESTDATA.get("selectEmployee.employeeName"));
		Assert.assertTrue(isEmployeeSelected, "The required employee : " + R.TESTDATA.get("selectEmployee.employeeName")
				+ " is not available in the employee list of the manager user.");

		// Fetch the list of employee task id's displayed for the manager
		List<String> listOfSubmittedTaskIdDisplayedForManager = taskApprovalListBox
				.fetchTaskIdOfSubmittedTasksDisplayedForManager();

		// Verify the count of tasks displayed for employee and manager is the same
		Assert.assertTrue(listOfSubmittedTaskIdOfEmployee.size() == (listOfSubmittedTaskIdDisplayedForManager.size()),
				"The count of tasks submitted by the employee and the count of tasks displayed for the manager does not match.");

		// Fetch the total count of tasks displayed for the employee
		int countOfTasksDisplayedBeforeApproving = taskApprovalListBox
				.fetchTheEmployeeTaskCountDisplayedForManager(R.TESTDATA.get("selectEmployee.employeeName"));

		int expectedCountOfTasksAfterApproving = countOfTasksDisplayedBeforeApproving - 1;

		// Fetch the details of the task to be approved
		String dateOfTaskToApprove = taskApprovalListBox.getDateOfFirstEntryOfTheSelectedEmployee().getText();
		String iDOfTaskToApprove = taskApprovalListBox.getTaskIDOfFirstEntryOfTheSelectedEmployee().getText();
		String descriptionOfTaskToApprove = taskApprovalListBox.getDescriptionOfFirstEntryOfTheSelectedEmployee()
				.getText();
		String timeTakenOfTaskToBeApprove = taskApprovalListBox.getTimeTakenOfFirstEntryOfTheSelectedEmployee()
				.getText();

		// Approve the task
		taskApprovalListBox.getApproveIcon().click();

		// Verify the success alert displayed on manager approving the task
		Assert.assertTrue(managerLandingPage.getSuccessAlertOnApprovingTimesheet().isPresent(),
				"Expected Success Alert is not displayed on manager approving the task of the employee.");

		Assert.assertTrue(managerLandingPage.getSuccessAlertOnApprovingTimesheet().getText().equals("Task Approved"),
				" Success Message on manager approving the task differs from the expected one. The message displayed on the alert is : "
						+ managerLandingPage.getSuccessAlertOnApprovingTimesheet().getText());

		// Fetch the total count of task displayed after approving
		int countOfTasksDisplayedAfterApproving = taskApprovalListBox
				.fetchTheEmployeeTaskCountDisplayedForManager(R.TESTDATA.get("selectEmployee.employeeName"));

		// Verify the count of task displayed after approving is one less than the
		// initial count displayed
		if (!(countOfTasksDisplayedAfterApproving == expectedCountOfTasksAfterApproving)) {
			fail("The count of tasks of the employee after manager approving the task differs from the expected one. Expected Count : "
					+ expectedCountOfTasksAfterApproving + " Displayed Count : " + countOfTasksDisplayedAfterApproving);
		}

		// Login as Employee
		employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"), R.TESTDATA.get("employeeLogin.password"));

		timesheetLandingPageEmployee.refresh();

		// Selecting option "All" from pagination
		timesheetDetailsGridBox.displayAllRows();

		// Expanding the taskdetails of the date for which the timesheet was added
		boolean isDatePresentInTaskList = timesheetDetailsGridBox.expandTimesheetOfRequiredDate(dateOfTaskToApprove);
		Assert.assertTrue(isDatePresentInTaskList,
				"Task list is not available for the employee for the date of the approved task.");

		// Selecting option "All" from pagination for task details
		timesheetDetailsGridBox.displayAllRows();

		// Verify the status of the approved timesheet in employee task list
		String statusOfApprovedTimeSheet = timesheetDetailsGridBox.verifyTheStatusOFTaskInTheEmployeeTaskList(
				iDOfTaskToApprove, descriptionOfTaskToApprove, timeTakenOfTaskToBeApprove);
		Assert.assertTrue(statusOfApprovedTimeSheet.equals(R.TESTDATA.get("selectFilter.approved")),
				"The status of the task approved by the manager is not 'Approved' in the employee task list. The status displayed for employee is : "
						+ statusOfApprovedTimeSheet);

	}

	// Scope - The scope of this testcase is to reject a particular task
	// that has been submitted by an employee to the manager in the approval list
	// and verify the same.

	@Test(priority = 1)
	public void testVerifyRejectingATaskFromGridView() throws InterruptedException, ParseException {

		// Login as employee
		TimesheetLandingPage timesheetLandingPageEmployee = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
		Assert.assertTrue(timesheetLandingPageEmployee.isPageOpened(), "Login is not successfull for the emploee ' : "
				+ R.TESTDATA.get("employeeLogin.militaryId") + "'. The required landing page is not opened on login.");

		TimeSheetDetailsGridBox timesheetDetailsGridBox = timesheetLandingPageEmployee.getTimeSheetDetailsGridBox();

		// Fetch the details of the tasks in submitted status
		boolean isFilterApplied = timesheetLandingPageEmployee
				.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.submitted"));
		Assert.assertTrue(isFilterApplied, "The required filter :" + R.TESTDATA.get("selectFilter.submitted")
				+ " is not available in the filter options list.");

		List<String> listOfSubmittedTaskIdOfEmployee = timesheetDetailsGridBox
				.fetchTaskIdOfEmployeeTasksDisplayedInGridView();

		if (listOfSubmittedTaskIdOfEmployee.size() == 0) {
			fail("There are no tasks for the employee in submitted status. Please add a timesheet and submit it.");
		}

		// Login as Manager
		TimesheetLandingPage timesheetLandingPage = managerLogin(R.TESTDATA.get("managerLogin.militaryId"),
				R.TESTDATA.get("managerLogin.password"));
		HeaderBox headerBox = timesheetLandingPage.getHeaderBox();

		// Verify the user has manager privileges
		Assert.assertTrue(headerBox.getManagerButton().isVisible(), " The logged in user '"
				+ R.TESTDATA.get("managerLogin.militaryId") + "' does not have a manager role.");

		ManagerLandingPage managerLandingPage = headerBox.clickManagerButton();
		TaskApprovalListBox taskApprovalListBox = managerLandingPage.getTaskApprovalListBox();

		// Select the required employee and fetch the details of tasks displayed for
		// that employee
		taskApprovalListBox.selectTheRequiredEmployee(R.TESTDATA.get("selectEmployee.employeeName"));
		List<String> listOfSubmittedTaskIdDisplayedForManager = taskApprovalListBox
				.fetchTaskIdOfSubmittedTasksDisplayedForManager();

		// Verify the count of tasks displayed for employee and manager is the same
		Assert.assertTrue(listOfSubmittedTaskIdOfEmployee.size() == (listOfSubmittedTaskIdDisplayedForManager.size()),
				"The count of tasks submitted by the employee and the count of tasks displayed for the manager does not match.");

		// Fetch the total count of tasks displayed for the employee
		int countOfTasksDisplayedBeforeRejecting = taskApprovalListBox
				.fetchTheEmployeeTaskCountDisplayedForManager(R.TESTDATA.get("selectEmployee.employeeName"));

		int expectedCountOfTasksAfterRejecting = countOfTasksDisplayedBeforeRejecting - 1;

		// Fetch the details of the task to be approved
		String dateOfEntryToReject = taskApprovalListBox.getDateOfFirstEntryOfTheSelectedEmployee().getText();
		String taskIDOfTaskToReject = taskApprovalListBox.getTaskIDOfFirstEntryOfTheSelectedEmployee().getText();
		String descriptionOfTaskToReject = taskApprovalListBox.getDescriptionOfFirstEntryOfTheSelectedEmployee()
				.getText();
		String timeTakenOfTaskToReject = taskApprovalListBox.getTimeTakenOfFirstEntryOfTheSelectedEmployee().getText();

		// Approve the task
		taskApprovalListBox.getRejectIcon().click();

		ManagerTaskDetailsMenuBox taskDetailsMenuBox = managerLandingPage.getTaskDetailsMenuBox();

		RejectTaskBox rejectTaskBox = taskDetailsMenuBox.clickYesButton();

		// Verify manager is unable to reject a task without providing the reason to
		// reject
		rejectTaskBox.clickSubmitButton();
		Assert.assertTrue(rejectTaskBox.getMandatoryAlert().isPresent(),
				"Manager is able to Reject the task without providing the reason for rejection.");

		// Provide the reason to reject
		rejectTaskBox.provideReasontoRejectTheTask(R.TESTDATA.get("provideReasontoRejectTheTask.reason"));

		rejectTaskBox.clickSubmitButton();

		// Verify the success alert displayed on manager rejecting the task
		Assert.assertTrue(managerLandingPage.getSuccessAlertOnRejectingTimesheet().isPresent(),
				"Expected Success Alert is not displayed on manager rejecting the timesheet.");

		Assert.assertTrue(
				managerLandingPage.getSuccessAlertOnRejectingTimesheet().getText().equals("Task Rejected successfully"),
				"The success message on manager rejecting the timesheet is not as expected. The message displayed on the alert is : "
						+ managerLandingPage.getSuccessAlertOnRejectingTimesheet().getText());

		// Fetch the total count of task displayed after rejecting
		int countOfTasksDisplayedAfterRejecting = taskApprovalListBox
				.fetchTheEmployeeTaskCountDisplayedForManager(R.TESTDATA.get("selectEmployee.employeeName"));
		System.out.println(countOfTasksDisplayedAfterRejecting);

		// Verify the count of task displayed after approving is one less than the
		// initial count displayed
		if (!(countOfTasksDisplayedAfterRejecting == expectedCountOfTasksAfterRejecting)) {
			fail("The count of tasks of the employee after manager rejecting the task differs from the expected one. Expected Count: "
					+ expectedCountOfTasksAfterRejecting + " Displayed Count: " + countOfTasksDisplayedAfterRejecting);
		}

		// Login as Employee
		employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"), R.TESTDATA.get("employeeLogin.password"));

		timesheetLandingPageEmployee.refresh();

		// Selecting option "All" from pagination
		timesheetDetailsGridBox.displayAllRows();

		// Expanding the taskdetails of the date for which the timesheet was added
		boolean isDatePresentInTaskList = timesheetDetailsGridBox.expandTimesheetOfRequiredDate(dateOfEntryToReject);

		Assert.assertTrue(isDatePresentInTaskList,
				"Task list is not available for the employee for the date of the rejected task.");

		// Selecting option "All" from pagination for task details
		timesheetDetailsGridBox.displayAllRows();

		// Verify the status of the approved timesheet in employee task list
		String statusOfApprovedTimeSheet = timesheetDetailsGridBox.verifyTheStatusOFTaskInTheEmployeeTaskList(
				taskIDOfTaskToReject, descriptionOfTaskToReject, timeTakenOfTaskToReject);
		Assert.assertTrue(statusOfApprovedTimeSheet.equals("Rejected"),
				"The status of the task rejected by the manager is not 'Rejected' in the employee task list. The status displayed for employee is : "
						+ statusOfApprovedTimeSheet);

		// Verify the rejected timesheet in calender view

		timesheetLandingPageEmployee.refresh();

		timesheetLandingPageEmployee.toggleToCalender();

		TimeSheetDetailsCalenderBox timeSheetDetailsCalenderBox = timesheetLandingPageEmployee
				.getTimeSheetDetailsCalenderBox();

		int yearToBeSelected = timeSheetDetailsCalenderBox.formatDateAndGetYear(dateOfEntryToReject);

		String monthToBeselected = timeSheetDetailsCalenderBox.formatDateAndGetMonth(dateOfEntryToReject);

		timeSheetDetailsCalenderBox.selectTheRequiredYearInCalenderView(yearToBeSelected);

		timeSheetDetailsCalenderBox.selectTheRequiredMonthInCalenderView(monthToBeselected);

		boolean isFilterAppliedOnCalenderView = timesheetLandingPageEmployee
				.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.rejected"));
		Assert.assertTrue(isFilterAppliedOnCalenderView, "The required filter :"
				+ R.TESTDATA.get("selectFilter.submitted") + " is not available in the filter options list.");

		EmployeeTaskDetailsMenuBox employeeTaskDetailsMenuBox = timeSheetDetailsCalenderBox
				.clickTheRequiredTaskInCalenderView(taskIDOfTaskToReject);

		String taskDetailsDisplayed = employeeTaskDetailsMenuBox.getTaskDetailsDisplayed().getText();

		if (!(taskDetailsDisplayed.contains(taskIDOfTaskToReject)
				&& taskDetailsDisplayed.contains(descriptionOfTaskToReject)
				&& taskDetailsDisplayed.contains(timeTakenOfTaskToReject)))

		{
			fail("The task rejected by the manager is not displayed in the calender view of the employee.");
		}

	}

	// HPB-17 - [TASKFORM] Remove the task entered in the timesheet

	// HPB-24 - Remove the task entered in the timesheet in grid view

	@Test(priority = 3)
	public void verifyRemovingATaskFromGridView() throws ParseException, InterruptedException {
		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
		TimeSheetDetailsGridBox timeSheetDetailsGridBox = timesheetLandingPage.getTimeSheetDetailsGridBox();
		boolean isFilterApplied;

		// Select All from pagination options
		timeSheetDetailsGridBox.displayAllRows();

		// Verify Remove button is available for all tasks in pending status and not
		// available for tasks other than in pending status
		timeSheetDetailsGridBox.verifyRemoveIconForEachTask();

		timesheetLandingPage.refresh();

		// Apply filter with status pending
		isFilterApplied = timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));
		Assert.assertTrue(isFilterApplied, "The required filter :" + R.TESTDATA.get("selectFilter.pending")
				+ " is not available in the filter options list.");

		// Verify timesheets with status pending is available for the employee

		Assert.assertTrue(timeSheetDetailsGridBox.getDateOfFirstEntry().isVisible(),
				"There are no timesheets in pending status for the employee to remove.");

		// Fetch the details of the task to be removed
		int countOfTasksInFirstDate = timeSheetDetailsGridBox.fetchTheCountOfTasksInFirstDate();
		String dateOfTheTaskToBeRemoved = timeSheetDetailsGridBox.getDateOfFirstEntry().getText();
		timeSheetDetailsGridBox.expandTheTaskDetails();
		String detailsOfTaskToBeRemoved = timeSheetDetailsGridBox.getFirtTimeSheetEntry().getText();

		// Remove the task
		RemoveConfirmationPopUp removeConfirmationPopUp = timeSheetDetailsGridBox.clickRemoveIcon();
		Assert.assertEquals(removeConfirmationPopUp.getRemoveConfirmationMessage().getText(),
				"Are you sure you want to remove this task?",
				"The confirmation message displayed on the remove confirmation pop up is not as expected. The text displayed is : "
						+ removeConfirmationPopUp.getRemoveConfirmationMessage().getText());

		removeConfirmationPopUp.clickYesButton();

		// Verfiy the success alert on employee removing the
		Assert.assertTrue(timesheetLandingPage.getSuccessAlertOnRemovingTimesheet().isPresent(),
				"Success Alert is not displayed on employee removing the timesheet.");
		Assert.assertTrue(
				timesheetLandingPage.getSuccessAlertOnRemovingTimesheet().getText().equals("Task Removed Successfully"),
				"The success message on employee removing the timesheet is not as expected. The message displayed on the alert is :"
						+ timesheetLandingPage.getSuccessAlertOnRemovingTimesheet().getText());

		// Verify the task is removed from the task list of the employee
		timesheetLandingPage.refresh();
		timeSheetDetailsGridBox.displayAllRows();
		if (countOfTasksInFirstDate == 1) {
			isFilterApplied = timesheetLandingPage
					.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));
			Assert.assertTrue(isFilterApplied, "The required filter :" + R.TESTDATA.get("selectFilter.pending")
					+ " is not available in the filter options list.");
			List<ExtendedWebElement> taskDateList = timeSheetDetailsGridBox.getTaskDateList();
			for (ExtendedWebElement taskDate : taskDateList) {
				if (taskDate.getText().equals(dateOfTheTaskToBeRemoved)) {
					fail("The task dated : '" + dateOfTheTaskToBeRemoved + "' is not removed from the list.");
				}
			}
		}

		else {
			timeSheetDetailsGridBox.expandTimesheetOfRequiredDate(dateOfTheTaskToBeRemoved);

			timeSheetDetailsGridBox.displayAllRows();

			List<String> taskDetailsList = timeSheetDetailsGridBox.fetchTaskDetails();

			for (String taskDetails : taskDetailsList) {
				if (taskDetails.equals(detailsOfTaskToBeRemoved)) {
					fail("The task removed is still present in the task list of the employee.");
				}
			}
		}

		timesheetLandingPage.refresh();
		timesheetLandingPage.toggleToCalender();

		TimeSheetDetailsCalenderBox timeSheetDetailsCalenderBox = timesheetLandingPage.getTimeSheetDetailsCalenderBox();

		int yearToBeSelected = timeSheetDetailsCalenderBox.formatDateAndGetYear(dateOfTheTaskToBeRemoved);

		String monthToBeselected = timeSheetDetailsCalenderBox.formatDateAndGetMonth(dateOfTheTaskToBeRemoved);

		timeSheetDetailsCalenderBox.selectTheRequiredYearInCalenderView(yearToBeSelected);

		timeSheetDetailsCalenderBox.selectTheRequiredMonthInCalenderView(monthToBeselected);

		List<String> taskListForTheMonth = timeSheetDetailsCalenderBox.fetchTimesheetOfEmployee();

		for (String task : taskListForTheMonth) {
			if (detailsOfTaskToBeRemoved.contains(task)) {
				fail("The task removed is displayed in the calender view.");
			}
		}
	}

	// HPB-25 - Remove the task entered in the timesheet in calendar view
	@Test(priority = 4)
	public void verifyRemovingATaskFromCalenderView() throws ParseException, InterruptedException {

		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
		TimeSheetDetailsCalenderBox timeSheetDetailsCalenderBox = timesheetLandingPage.getTimeSheetDetailsCalenderBox();
		boolean isFilterApplied;
		boolean isRemoveButtonAvailable;

		timesheetLandingPage.toggleToCalender();

		// Verify remove button is not available for timesheet entries in submitted
		// status
		isFilterApplied = timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));
		Assert.assertTrue(isFilterApplied, "The required filter :" + R.TESTDATA.get("selectFilter.pending")
				+ " is not available in the filter options list.");

		isRemoveButtonAvailable = timeSheetDetailsCalenderBox.verifyTheRemoveButton();
		Assert.assertFalse(isRemoveButtonAvailable, "Remove Task Button is available for tasks in submitted status.");

		// Verify remove button is not available for timesheet entries in approved
		// status
		/*
		 * isFilterApplied =
		 * timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get(
		 * "selectFilter.approved")); Assert.assertTrue(isFilterApplied,
		 * "The required filter :" + R.TESTDATA.get("selectFilter.approved") +
		 * " is not available in the filter options list.");
		 * 
		 * isRemoveButtonAvailable =
		 * timeSheetDetailsCalenderBox.verifyTheRemoveButton();
		 * Assert.assertFalse(isRemoveButtonAvailable,
		 * "Remove Task Button is available for tasks in approved status.");
		 * 
		 * // Verify remove button is not available for timesheet entries in approved //
		 * status isFilterApplied =
		 * timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get(
		 * "selectFilter.rejected")); Assert.assertTrue(isFilterApplied,
		 * "The required filter :" + R.TESTDATA.get("selectFilter.rejected") +
		 * " is not available in the filter options list.");
		 * 
		 * isRemoveButtonAvailable =
		 * timeSheetDetailsCalenderBox.verifyTheRemoveButton();
		 * Assert.assertFalse(isRemoveButtonAvailable,
		 * "Remove Task Button is available for tasks in rejected status.");
		 * 
		 * // Verify remove button is available for timesheet entries in pending //
		 * status isFilterApplied =
		 * timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get(
		 * "selectFilter.pending")); Assert.assertTrue(isFilterApplied,
		 * "The required filter :" + R.TESTDATA.get("selectFilter.pending") +
		 * " is not available in the filter options list.");
		 * 
		 * isRemoveButtonAvailable =
		 * timeSheetDetailsCalenderBox.verifyTheRemoveButton();
		 * Assert.assertTrue(isRemoveButtonAvailable,
		 * "Remove Task Button is not available for tasks in pending status.");
		 */

		String taskToBeRemoved = timeSheetDetailsCalenderBox.getTimeSheetEntry().getText();

		// Remove the first timesheet entry in pending status
		EmployeeTaskDetailsMenuBox employeeTaskDetailsMenuBox = timeSheetDetailsCalenderBox
				.clickTheFirstTimeSheetEntry();
		RemoveConfirmationPopUp removeConfirmationPopUp = employeeTaskDetailsMenuBox.clickRemoveButton();

		removeConfirmationPopUp.clickYesButton();

		// Verfiy the success alert
		Assert.assertTrue(timesheetLandingPage.getSuccessAlertOnRemovingTimesheet().isPresent(),
				"Success Alert is not displayed on employee removing the timesheet.");
		Assert.assertTrue(
				timesheetLandingPage.getSuccessAlertOnRemovingTimesheet().getText().equals("Task Removed Successfully"),
				"The success message on employee removing the timesheet is not as expected. The message displayed on the alert is :"
						+ timesheetLandingPage.getSuccessAlertOnRemovingTimesheet().getText());

		timesheetLandingPage.refresh();
		timesheetLandingPage.toggleToCalender();

		Thread.sleep(6000);

		// Verify the removed timesheet entry is not available in the remaining
		// timesheet list
		List<String> tasksDisplayedInCalenderView = timeSheetDetailsCalenderBox.fetchTimesheetOfEmployee();

		for (String taskDisplayed : tasksDisplayedInCalenderView) {
			if (taskDisplayed.equals(taskToBeRemoved)) {
				fail("The removed task : " + taskToBeRemoved + " is displayed in the task list of the employee.");
			}
		}
	}

	// HPb-40 - Calendar view starting with weekdays

	@Test(priority = 5)
	public void verifyCalendarViewStartingWithWeekdays() throws InterruptedException {

		boolean isDaysDisplayedInOrder;
		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
		TimeSheetDetailsCalenderBox timeSheetDetailsCalenderBox = timesheetLandingPage.getTimeSheetDetailsCalenderBox();

		timesheetLandingPage.toggleToCalender();
		isDaysDisplayedInOrder = timeSheetDetailsCalenderBox.verifyTheDaysInCalenderViewOfEmployee();

		Assert.assertTrue(isDaysDisplayedInOrder,
				"The days are not starting with weekdays in the calender view of the employee");

		List<String> backGroundColorOfWeekends = timeSheetDetailsCalenderBox.fetchBackgroudColourOfWeekends();

		for (String backGroundColor : backGroundColorOfWeekends) {
			if (!backGroundColor.equalsIgnoreCase(R.TESTDATA.get("fetchBackgroudColourOfWeekends.backgroundColor")))

			{
				fail("The background colour of weekend is not as expected for the employee.");
			}
		}

		// Login as Manager
		TimesheetLandingPage timesheetLandingpage = managerLogin(R.TESTDATA.get("managerLogin.militaryId"),
				R.TESTDATA.get("managerLogin.password"));
		HeaderBox header = timesheetLandingpage.getHeaderBox();
		Assert.assertTrue(header.getManagerButton().isVisible(),
				" The logged in user '" + R.TESTDATA.get("managerLogin.militaryId") + "' does not have a manager role");

		ManagerLandingPage managerLandingPage = header.clickManagerButton();
		ManagerCalenderBox managerCalenderBox = managerLandingPage.getManagerCalenderBox();
		timesheetLandingpage.toggleToCalender();

		Thread.sleep(3000);
		isDaysDisplayedInOrder = managerCalenderBox.verifyTheDaysInEmployeeCalenderViewOfManager();

		Assert.assertTrue(isDaysDisplayedInOrder,
				"The days are not starting with weekdays in the calender view of the Manager User");

		List<String> backGroundColorOfWeekendsForManager = managerCalenderBox
				.fetchBackgroudColourOfWeekendsForManagerCalender();

		for (String backgroundColour : backGroundColorOfWeekendsForManager) {
			if (!backgroundColour.equalsIgnoreCase(R.TESTDATA.get("fetchBackgroudColourOfWeekends.backgroundColor"))) {
				fail("The background colour of weekend is not as expected for the employee calender displayed for manager.");

			}
		}

		timesheetLandingpage.refresh();
		timesheetLandingpage.toggleToCalender();
		Thread.sleep(3000);
		isDaysDisplayedInOrder = managerCalenderBox.verifyTheDaysInCalenderViewOfManagerEmployee();

		Assert.assertTrue(isDaysDisplayedInOrder,
				"The days are not starting with weekdays in the employee task calender view of the Manager User");

		List<String> backGroundColorOfWeekendsForManagerEmployee = managerCalenderBox
				.fetchBackgroudColourOfWeekendsOfManagerEmployee();

		for (String backgroundColour : backGroundColorOfWeekendsForManagerEmployee) {
			if (!backgroundColour.equalsIgnoreCase(R.TESTDATA.get("fetchBackgroudColourOfWeekends.backgroundColor"))) {
				fail("The background colour of weekend is not as expected for the  manager employee.");

			}
		}

	}
}