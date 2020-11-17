package pits.automation.web.S2003;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;

import pits.automation.web.hrms.employee.pages.AddNewTimeSheetBox;
import pits.automation.web.hrms.employee.pages.DatePickerForAddingTimesheet;
import pits.automation.web.hrms.employee.pages.DatePickerForEditingTimeSheet;
import pits.automation.web.hrms.employee.pages.EmployeeEditTimeSheetBox;
import pits.automation.web.hrms.employee.pages.EmployeeTaskDetailsMenuBox;
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

public class Test_S2003_02 extends BaseTest {

	// HPB- 17
	@Test(priority = 1)
	public void verifyEditingTimeSheetInGridView() throws InterruptedException {
		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
		TimeSheetDetailsGridBox timeSheetDetailsGridBox = timesheetLandingPage.getTimeSheetDetailsGridBox();
		boolean isFilterApplied;

		// Select All from pagination options
		timeSheetDetailsGridBox.displayAllRows();

		// Verify Edit button is available for all tasks in pending status and not
		// available for tasks other than in pending status
		timeSheetDetailsGridBox.verifyEditIconForEachTask();

		timesheetLandingPage.refresh();

		// Apply filter with status pending
		isFilterApplied = timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));
		Assert.assertTrue(isFilterApplied, "The required filter :" + R.TESTDATA.get("selectFilter.pending")
				+ " is not available in the filter options list.");

		// Fetch the details of the task to be removed
		int countOfTasksInFirstDate = timeSheetDetailsGridBox.fetchTheCountOfTasksInFirstDate();
		String dateOfTheTaskToBeEdited = timeSheetDetailsGridBox.getDateOfFirstEntry().getText();
		timeSheetDetailsGridBox.expandTheTaskDetails();

		// Edit the date, time, id and description
		EmployeeEditTimeSheetBox employeeEditTimeSheetBox = timeSheetDetailsGridBox.clickEditTaskButton();

		DatePickerForEditingTimeSheet datePicker = employeeEditTimeSheetBox.clickDateField();
		datePicker.selectDate(R.TESTDATA.get("selectDate.editedYear"), R.TESTDATA.get("selectDate.editedMonth"),
				R.TESTDATA.get("selectDate.editedDay"));
		employeeEditTimeSheetBox.editTaskID(R.TESTDATA.get("editTaskID.taskId"));

		employeeEditTimeSheetBox.editTimeField(R.TESTDATA.get("editTimeField.editedTime"));
		employeeEditTimeSheetBox.editTaskDescription(R.TESTDATA.get("editTaskDescription.editedDescription"));
		employeeEditTimeSheetBox.clickUpdateButton();

		// Verify the success alert
		ExtendedWebElement successAlertOnEdit = timesheetLandingPage.getSuccessAlertOnEditingTimesheet();

		Assert.assertTrue(successAlertOnEdit.isVisible(), "Success Alert is not displayed on editing the timesheet ");

		Assert.assertTrue(successAlertOnEdit.getText().equals("Task updated successfully"),
				"The success message on editing the timesheet is not as expected. The message displayed is :"
						+ successAlertOnEdit.getText());
		timesheetLandingPage.refresh();

		// Verify the date is no more available in pending list if there was only one
		// task
		if (countOfTasksInFirstDate == 1) {
			isFilterApplied = timesheetLandingPage
					.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));
			Assert.assertTrue(isFilterApplied, "The required filter :" + R.TESTDATA.get("selectFilter.pending")
					+ " is not available in the filter options list.");
			timeSheetDetailsGridBox.displayAllRows();

			List<ExtendedWebElement> taskDateList = timeSheetDetailsGridBox.getTaskDateList();
			for (ExtendedWebElement taskDate : taskDateList) {
				if (taskDate.getText().equals(dateOfTheTaskToBeEdited)) {
					fail("The  edited task is still displayed in the date :" + dateOfTheTaskToBeEdited);
				}
			}
			timesheetLandingPage.refresh();

		}

		// Verify the edited details are present in the new date
		timeSheetDetailsGridBox.displayAllRows();
		timeSheetDetailsGridBox
				.expandTimesheetOfRequiredDate(R.TESTDATA.get("expandTimesheetOfRequiredDate.editedDate"));
		timeSheetDetailsGridBox.displayAllRows();

		boolean isEditSuccessfull = false;
		List<String> taskDetailsList = timeSheetDetailsGridBox.fetchTaskDetails();

		for (String taskDetails : taskDetailsList) {
			if (taskDetails.contains(R.TESTDATA.get("editTaskID.taskId"))
					&& taskDetails.contains(R.TESTDATA.get("editTimeField.editedTime"))
					&& taskDetails.contains(R.TESTDATA.get("editTaskDescription.editedDescription"))) {
				isEditSuccessfull = true;
				System.out.println("The timesheet has been edited succesfully");
				break;

			}
			if (isEditSuccessfull == false) {
				fail("The time sheet edit is not succesfull. The edited details are not displayed in the task list for the date :"
						+ R.TESTDATA.get("expandTimesheetOfRequiredDate.editedDate"));
			}
		}
	}

	@Test(priority = 2)
	public void verifyEditingTimeSheetInClenderView() throws InterruptedException {
		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
		TimeSheetDetailsCalenderBox timeSheetDetailsCalenderBox = timesheetLandingPage.getTimeSheetDetailsCalenderBox();
		boolean isFilterApplied;
		boolean isEditButtonAvailable;

		timesheetLandingPage.toggleToCalender();

		// Verify remove button is not available for timesheet entries in submitted
		// status
		/*
		 * isFilterApplied =
		 * timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get(
		 * "selectFilter.submitted")); Assert.assertTrue(isFilterApplied,
		 * "The required filter :" + R.TESTDATA.get("selectFilter.submitted") +
		 * " is not available in the filter options list.");
		 * 
		 * isEditButtonAvailable = timeSheetDetailsCalenderBox.verifyTheEditButton();
		 * Assert.assertFalse(isEditButtonAvailable,
		 * "Edit Task Button is available for tasks in submitted status.");
		 * 
		 * // Verify remove button is not available for timesheet entries in approved //
		 * status isFilterApplied =
		 * timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get(
		 * "selectFilter.approved")); Assert.assertTrue(isFilterApplied,
		 * "The required filter :" + R.TESTDATA.get("selectFilter.approved") +
		 * " is not available in the filter options list.");
		 * 
		 * isEditButtonAvailable = timeSheetDetailsCalenderBox.verifyTheEditButton();
		 * Assert.assertFalse(isEditButtonAvailable,
		 * "Edit Task Button is available for tasks in approved status.");
		 * 
		 * // Verify remove button is not available for timesheet entries in approved //
		 * status isFilterApplied =
		 * timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get(
		 * "selectFilter.rejected")); Assert.assertTrue(isFilterApplied,
		 * "The required filter :" + R.TESTDATA.get("selectFilter.rejected") +
		 * " is not available in the filter options list.");
		 * 
		 * isEditButtonAvailable = timeSheetDetailsCalenderBox.verifyTheEditButton();
		 * Assert.assertFalse(isEditButtonAvailable,
		 * "Edit Task Button is available for tasks in rejected status.");
		 */

		// Verify remove button is available for timesheet entries in pending
		// status
		isFilterApplied = timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));
		Assert.assertTrue(isFilterApplied, "The required filter :" + R.TESTDATA.get("selectFilter.pending")
				+ " is not available in the filter options list.");

		isEditButtonAvailable = timeSheetDetailsCalenderBox.verifyTheEditButton();
		Assert.assertTrue(isEditButtonAvailable, "Edit Task Button is not available for tasks in pending status.");

		// String taskToBeRemoved =
		// timeSheetDetailsCalenderBox.getTimeSheetEntry().getText();

		// Remove the first timesheet entry in pending status
		EmployeeTaskDetailsMenuBox employeeTaskDetailsMenuBox = timeSheetDetailsCalenderBox
				.clickTheFirstTimeSheetEntry();
		EmployeeEditTimeSheetBox employeeEditTimeSheetBox = employeeTaskDetailsMenuBox.clickEditButton();

		DatePickerForEditingTimeSheet DatePickerForEditingTimeSheet = employeeEditTimeSheetBox.clickDateField();
		String currentDay = DatePickerForEditingTimeSheet.selectCurrentDate();
		employeeEditTimeSheetBox.editTaskID(R.TESTDATA.get("editTaskID.newId"));
		employeeEditTimeSheetBox.editTimeField(R.TESTDATA.get("editTimeField.newTime"));

		employeeEditTimeSheetBox.editTaskDescription(R.TESTDATA.get("editTaskDescription.newDescription"));
		employeeEditTimeSheetBox.clickUpdateButton();

		// Verify the success alert
		ExtendedWebElement successAlertOnEdit = timesheetLandingPage.getSuccessAlertOnEditingTimesheet();

		Assert.assertTrue(successAlertOnEdit.isVisible(), "Success Alert is not displayed on editing the timesheet ");

		Assert.assertTrue(successAlertOnEdit.getText().equals("Task updated successfully"),
				"The success message on editing the timesheet is not as expected. The message displayed is :"
						+ successAlertOnEdit.getText());

		timesheetLandingPage.refresh();
		timesheetLandingPage.toggleToCalender();

		Thread.sleep(3000);

		// Verify the edited values are reflected in the task details
		isFilterApplied = timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));
		Assert.assertTrue(isFilterApplied, "The required filter :" + R.TESTDATA.get("selectFilter.pending")
				+ " is not available in the filter options list.");

		timeSheetDetailsCalenderBox.selectRequiredTaskFromCurrentDayEntries(R.TESTDATA.get("editTaskID.newId"));

		String taskDetailsDisplayed = employeeTaskDetailsMenuBox.getTaskDetailsDisplayed().getText();

		Assert.assertTrue(taskDetailsDisplayed.contains(R.TESTDATA.get("editTaskID.newId")),
				"The task Id displayed is not the edited value. Expected :" + R.TESTDATA.get("editTaskID.newId")
						+ "Displayed :" + taskDetailsDisplayed);

		Assert.assertTrue(taskDetailsDisplayed.contains(R.TESTDATA.get("editTaskDescription.newDescription")),
				"The task description displayed is not as expected. Expected: "
						+ R.TESTDATA.get("editTaskDescription.newDescription") + "Displayed :" + taskDetailsDisplayed);

		Assert.assertTrue(taskDetailsDisplayed.contains(R.TESTDATA.get("editTimeField.newTime")),
				"The time displayed is not as expected. Expected :" + R.TESTDATA.get("editTimeField.newTime")
						+ "Displayed: " + taskDetailsDisplayed);

	}

	// HPB-26 - [CALENDAR REFINEMENTS] - Block the Weekends

	@Test(priority = 3)
	public void verifyWeekendsAreBlocked() throws InterruptedException {
		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
		TimeSheetDetailsGridBox timeSheetDetailsGridBox = timesheetLandingPage.getTimeSheetDetailsGridBox();
		boolean isFilterApplied;
		boolean sundaysOfTheCurrentMonthDisabled;
		boolean saturdaysOfTheCurrentMonthDisabled;
		boolean canSaturdaysBeSelected;
		boolean canSundaysBeSelected;

		// Verify the weekends are blocked for employee in Add timesheet popup

		AddNewTimeSheetBox AddNewTimeSheetBox = timesheetLandingPage.clickAddNewTimeSheetButton();
		DatePickerForAddingTimesheet datePickerForAddingTimesheet = AddNewTimeSheetBox.clickTaskDateField();

		sundaysOfTheCurrentMonthDisabled = datePickerForAddingTimesheet.verifySundaysAreDisabled();
		Assert.assertTrue(sundaysOfTheCurrentMonthDisabled,
				"The sundays of the current month are not disabled for the employee in add timesheet popup.");

		saturdaysOfTheCurrentMonthDisabled = datePickerForAddingTimesheet.verifySaturdaysAreDisabled();
		Assert.assertTrue(saturdaysOfTheCurrentMonthDisabled,
				"The saturdays of the current month are not disabled for the employee in add timesheet popup.");

		canSaturdaysBeSelected = datePickerForAddingTimesheet.verifySaturdaysCannotBeSelected();
		Assert.assertFalse(canSaturdaysBeSelected,
				"The saturdays are selectable for the employee in add timesheet popup.");

		canSundaysBeSelected = datePickerForAddingTimesheet.verifySundaysCannotBeSelected();
		Assert.assertFalse(canSundaysBeSelected, "The sundays are selectable for the employee in add timesheet popup.");

		// Verify the weekends are blocked for employee in Edit timesheet popup

		timesheetLandingPage.refresh();

		// Apply filter with status pending
		isFilterApplied = timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));
		Assert.assertTrue(isFilterApplied, "The required filter :" + R.TESTDATA.get("selectFilter.pending")
				+ " is not available in the filter options list.");

		// Open the edit timesheet pop up of first task in pending status
		timeSheetDetailsGridBox.expandTheTaskDetails();
		EmployeeEditTimeSheetBox employeeEditTimeSheetBox = timeSheetDetailsGridBox.clickEditTaskButton();

		DatePickerForEditingTimeSheet datePickerForEditingTimeSheet = employeeEditTimeSheetBox.clickDateField();

		sundaysOfTheCurrentMonthDisabled = datePickerForEditingTimeSheet.verifySundaysAreDisabled();
		Assert.assertTrue(sundaysOfTheCurrentMonthDisabled,
				"The sundays of the current month are not disabled for the employee in edit timesheet popup.");

		saturdaysOfTheCurrentMonthDisabled = datePickerForEditingTimeSheet.verifySaturdaysAreDisabled();
		Assert.assertTrue(saturdaysOfTheCurrentMonthDisabled,
				"The saturdays of the current month are not disabled for the employee in edit timesheet popup.");

		canSaturdaysBeSelected = datePickerForEditingTimeSheet.verifySaturdaysCannotBeSelected();
		Assert.assertFalse(canSaturdaysBeSelected,
				"The saturdays are selectable for the employee in edit timesheet popup.");

		canSundaysBeSelected = datePickerForEditingTimeSheet.verifySundaysCannotBeSelected();
		Assert.assertFalse(canSundaysBeSelected,
				"The sundays are selectable for the employee in edit timesheet popup.");

		TimesheetLandingPage timesheetLandingPageManager = managerLogin(R.TESTDATA.get("managerLogin.militaryId"),
				R.TESTDATA.get("managerLogin.password"));
		HeaderBox header = timesheetLandingPageManager.getHeaderBox();

		Assert.assertTrue(header.getManagerButton().isVisible(),
				" The logged in user '" + R.TESTDATA.get("managerLogin.militaryId") + "' does not have a manager role");

		AddNewTimeSheetBox AddNewTimesheetBox = timesheetLandingPage.clickAddNewTimeSheetButton();
		DatePickerForAddingTimesheet datePickerForaddingTimesheet = AddNewTimesheetBox.clickTaskDateField();

		sundaysOfTheCurrentMonthDisabled = datePickerForaddingTimesheet.verifySundaysAreDisabled();
		Assert.assertTrue(sundaysOfTheCurrentMonthDisabled,
				"The sundays of the current month are not disabled for the manager employee in add timesheet popup.");

		saturdaysOfTheCurrentMonthDisabled = datePickerForaddingTimesheet.verifySaturdaysAreDisabled();
		Assert.assertTrue(saturdaysOfTheCurrentMonthDisabled,
				"The saturdays of the current month are not disabled for the manager employee in add timesheet popup.");

		canSaturdaysBeSelected = datePickerForaddingTimesheet.verifySaturdaysCannotBeSelected();
		Assert.assertFalse(canSaturdaysBeSelected,
				"The saturdays are selectable for the employee in add timesheet popup.");

		canSundaysBeSelected = datePickerForaddingTimesheet.verifySundaysCannotBeSelected();
		Assert.assertFalse(canSundaysBeSelected, "The sundays are selectable for the employee in add timesheet popup.");

		timesheetLandingPageManager.refresh();

		isFilterApplied = timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));
		Assert.assertTrue(isFilterApplied, "The required filter :" + R.TESTDATA.get("selectFilter.pending")
				+ " is not available in the filter options list.");

		// Open the edit timesheet pop up of first task in pending status
		timeSheetDetailsGridBox.expandTheTaskDetails();
		EmployeeEditTimeSheetBox employeeEditTimeSheetbox = timeSheetDetailsGridBox.clickEditTaskButton();

		DatePickerForEditingTimeSheet datePickerForEditingTimesheet = employeeEditTimeSheetbox.clickDateField();

		sundaysOfTheCurrentMonthDisabled = datePickerForEditingTimesheet.verifySundaysAreDisabled();
		Assert.assertTrue(sundaysOfTheCurrentMonthDisabled,
				"The sundays of the current month are not disabled for the manager employee in edit timesheet popup.");

		saturdaysOfTheCurrentMonthDisabled = datePickerForEditingTimesheet.verifySaturdaysAreDisabled();
		Assert.assertTrue(saturdaysOfTheCurrentMonthDisabled,
				"The saturdays of the current month are not disabled for the manager employee in edit timesheet popup.");

		canSaturdaysBeSelected = datePickerForEditingTimesheet.verifySaturdaysCannotBeSelected();
		Assert.assertFalse(canSaturdaysBeSelected,
				"The saturdays are selectable for the manager employee in edit timesheet popup.");

		canSundaysBeSelected = datePickerForEditingTimesheet.verifySundaysCannotBeSelected();
		Assert.assertFalse(canSundaysBeSelected,
				"The sundays are selectable for the manager employee in edit timesheet popup.");

	}

	// HPB-46 - [TASK REJECTION DETAIL]Display the reason for the rejected tasks in
	// grid and calendar view

	@Test
	public void verifyTaskRejectReasonInGridView() throws InterruptedException {
		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
		Assert.assertTrue(timesheetLandingPage.getTimeSheetDetailsGridBox().getTimeSheetRow().isPresent(),
				"TimeSheet Details are empty. Please add a timesheet.");
		boolean isFilterApplied = true;

		String headerToVerify = "Reject Reason";

		TimeSheetDetailsGridBox timesheetDetailsGridBox = timesheetLandingPage.getTimeSheetDetailsGridBox();
		timesheetDetailsGridBox.displayAllRows();

		// To verify Reject Reason column is present in the main timesheet table

		timesheetDetailsGridBox.verifyRequiredHeaderIsPresentInTaskDetails(headerToVerify, "All");

		timesheetDetailsGridBox.refresh();
		timesheetDetailsGridBox.displayAllRows();
		isFilterApplied = timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));
		Assert.assertTrue(isFilterApplied, "The required filter :" + R.TESTDATA.get("selectFilter.pending")
				+ " is not available in the filter options list.");

		timesheetDetailsGridBox.verifyRequiredHeaderIsPresentInTaskDetails(headerToVerify,
				R.TESTDATA.get("selectFilter.pending"));

		timesheetDetailsGridBox.refresh();

		isFilterApplied = timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.submitted"));
		Assert.assertTrue(isFilterApplied, "The required filter :" + R.TESTDATA.get("selectFilter.submitted")
				+ " is not available in the filter options list.");
		timesheetDetailsGridBox.displayAllRows();

		timesheetDetailsGridBox.verifyRequiredHeaderIsPresentInTaskDetails(headerToVerify,
				R.TESTDATA.get("selectFilter.submitted"));

		timesheetDetailsGridBox.refresh();

		isFilterApplied = timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.approved"));
		Assert.assertTrue(isFilterApplied, "The required filter :" + R.TESTDATA.get("selectFilter.approved")
				+ " is not available in the filter options list.");
		timesheetDetailsGridBox.displayAllRows();

		timesheetDetailsGridBox.verifyRequiredHeaderIsPresentInTaskDetails(headerToVerify,
				R.TESTDATA.get("selectFilter.approved"));

		timesheetDetailsGridBox.refresh();

		isFilterApplied = timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.rejected"));
		Assert.assertTrue(isFilterApplied, "The required filter :" + R.TESTDATA.get("selectFilter.rejected")
				+ " is not available in the filter options list.");
		timesheetDetailsGridBox.displayAllRows();

		timesheetDetailsGridBox.verifyRequiredHeaderIsPresentInTaskDetails(headerToVerify,
				R.TESTDATA.get("selectFilter.rejected"));

	}

	@Test(priority = 4)
	public void verifyReasonToRejectIsDisplayedInGridView() throws InterruptedException {
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

		// Login as Manager
		TimesheetLandingPage timesheetLandingPage = managerLogin(R.TESTDATA.get("managerLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
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

		// Verify the count of tasks diaplayed for employee and manager is the same
		Assert.assertTrue(listOfSubmittedTaskIdOfEmployee.size() == (listOfSubmittedTaskIdDisplayedForManager.size()),
				"The count of tasks submitted by the employee and the count of tasks displayed for the manager does not match.");

		// Fetch the total count of tasks displayed for the employee
		int countOfTasksDisplayedBeforeRejecting = taskApprovalListBox
				.fetchTheEmployeeTaskCountDisplayedForManager(R.TESTDATA.get("selectEmployee.employeeName"));

		int expectedCountOfTasksAfterRejecting = countOfTasksDisplayedBeforeRejecting - 1;

		// Fetch the details of the task to be approved
		String dateOfEntryToBeRejected = taskApprovalListBox.getDateOfFirstEntryOfTheSelectedEmployee().getText();
		String taskIDOfTaskToBeRejected = taskApprovalListBox.getTaskIDOfFirstEntryOfTheSelectedEmployee().getText();
		String descriptionOfTaskToBeRejected = taskApprovalListBox.getDescriptionOfFirstEntryOfTheSelectedEmployee()
				.getText();
		String timeTakenOfTaskToBeRejected = taskApprovalListBox.getTimeTakenOfFirstEntryOfTheSelectedEmployee()
				.getText();

		// Approve the task
		taskApprovalListBox.getRejectIcon().click();

		ManagerTaskDetailsMenuBox taskDetailsMenuBox = managerLandingPage.getTaskDetailsMenuBox();

		RejectTaskBox rejectTaskBox = taskDetailsMenuBox.clickYesButton();

		rejectTaskBox.clickSubmitButton();

		Assert.assertTrue(rejectTaskBox.getMandatoryAlert().isPresent(),
				"Manager is able to Reject the task without providing the reason for rejection.");
		rejectTaskBox.provideReasontoRejectTheTask(R.TESTDATA.get("provideReasontoRejectTheTask.reason"));

		rejectTaskBox.clickSubmitButton();

		// Verify the success alert
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
		timesheetDetailsGridBox.expandTimesheetOfRequiredDate(dateOfEntryToBeRejected);

		// Selecting option "All" from pagination for task details
		timesheetDetailsGridBox.displayAllRows();

		// Verify the status of the approved timesheet in employee task list
		String rejectReasonDispalyed = timesheetDetailsGridBox.verifyRejectReasonIsDisplayedInTaskList(
				taskIDOfTaskToBeRejected, descriptionOfTaskToBeRejected, timeTakenOfTaskToBeRejected);
		Assert.assertTrue(rejectReasonDispalyed.equals(R.TESTDATA.get("provideReasontoRejectTheTask.reason")),
				"The reject reason provided by the manager is not displayed in the employee task list.");

	}

	@Test(priority = 4)
	public void verifyReasonToRejectIsDisplayedInCalenderView() throws InterruptedException {
		// Login as employee
		TimesheetLandingPage timesheetLandingPageEmployee = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
		Assert.assertTrue(timesheetLandingPageEmployee.isPageOpened(), "Login is not successfull for the emploee ' : "
				+ R.TESTDATA.get("employeeLogin.militaryId") + "'. The required landing page is not opened on login.");

		TimeSheetDetailsCalenderBox timeSheetDetailsCalenderBox = timesheetLandingPageEmployee
				.getTimeSheetDetailsCalenderBox();

		// Toggle to calender view and verify timesheet entries are present for the
		// particular month
		timesheetLandingPageEmployee.toggleToCalender();
		Assert.assertTrue(timeSheetDetailsCalenderBox.getTimeSheetEntry().isPresent(),
				"There are no timesheet entries for this month");

		timesheetLandingPageEmployee.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));

		Assert.assertTrue(timeSheetDetailsCalenderBox.getFirstEntryInPendingStatus().isVisible(),
				"There are no past date timesheet entries in pending status for this month");

		String task = timeSheetDetailsCalenderBox.getFirstEntryInPendingStatus().getText();

		// Submitting the task
		timeSheetDetailsCalenderBox.submitEntryFromCalenderMode();

		// Verifying the success alert
		Assert.assertTrue(timesheetLandingPageEmployee.getSuccessAlertOnSubmittingTimesheetInCalenderMode().isPresent(),
				"Success Alert is not displayed on submitting the timesheet. The timesheet has not been submitted");

		Assert.assertTrue(
				timesheetLandingPageEmployee.getSuccessAlertOnSubmittingTimesheetInCalenderMode().getText()
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
		timesheetLandingPageEmployee.toggleToCalender();

		// Filter approved entries and verify the task approved by the manager is
		// available in the list
		timesheetLandingPageEmployee.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.rejected"));
		Assert.assertTrue(timeSheetDetailsCalenderBox.getTimeSheetEntry().isPresent(),
				"There are no timesheet entries in rejected status for this month.");

		boolean isTaskRejected = timeSheetDetailsCalenderBox.verifyTaskIsPresentInCalenderView(task);

		Assert.assertTrue(isTaskRejected,
				"The task rejected by manager is not available in the list of approved tasks of the employee.");

		EmployeeTaskDetailsMenuBox employeeTaskDetailsMenuBox = timeSheetDetailsCalenderBox
				.clickTheRequiredTaskInCalenderView(task);

		Assert.assertTrue(
				employeeTaskDetailsMenuBox.getTaskDetailsDisplayed().getText()
						.contains(R.TESTDATA.get("provideReasontoRejectTheTask.reason")),
				"The reject reason is not displayed in the task details.");

	}

	// HPB-[EVENTS] - Add holiday and other events

	@Test(priority = 4)
	public void verifyAnEventDate() throws InterruptedException, Exception {
		// Login as employee
		TimesheetLandingPage timesheetLandingPageEmployee = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
		Assert.assertTrue(timesheetLandingPageEmployee.isPageOpened(), "Login is not successfull for the emploee ' : "
				+ R.TESTDATA.get("employeeLogin.militaryId") + "'. The required landing page is not opened on login.");
		boolean isFilterApplied;
		TimeSheetDetailsCalenderBox timeSheetDetailsCalenderBox = timesheetLandingPageEmployee
				.getTimeSheetDetailsCalenderBox();

		TimeSheetDetailsGridBox timeSheetDetailsGridBox = timesheetLandingPageEmployee.getTimeSheetDetailsGridBox();

		timesheetLandingPageEmployee.toggleToCalender();

		// Select the year and month of the required event
		int yearToBeSelected = timesheetLandingPageEmployee.getTimeSheetDetailsCalenderBox()
				.formatDateAndGetYear(R.TESTDATA.get("selectDate.eventDate"));

		String monthToBeselected = timesheetLandingPageEmployee.getTimeSheetDetailsCalenderBox()
				.formatDateAndGetMonth(R.TESTDATA.get("selectDate.eventDate"));

		timeSheetDetailsCalenderBox.selectTheRequiredYearInCalenderView(yearToBeSelected);

		timeSheetDetailsCalenderBox.selectTheRequiredMonthInCalenderView(monthToBeselected);

		// Verify the colour
		String backGroundColourOfEvent = timeSheetDetailsCalenderBox
				.verifyHolidayEvent(R.TESTDATA.get("verifyHolidayEvent.event"));

		Assert.assertTrue(backGroundColourOfEvent.equals(R.TESTDATA.get("verifyHolidayEvent.color")),
				"The color of the holiday event is not as expected.");

		// Verify the date is disabled in edit pop up from calender view

		isFilterApplied = timesheetLandingPageEmployee
				.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));
		Assert.assertTrue(isFilterApplied, "The required filter :" + R.TESTDATA.get("selectFilter.pending")
				+ " is not available in the filter options list.");

		EmployeeTaskDetailsMenuBox employeeTaskDetailsMenuBox = timeSheetDetailsCalenderBox
				.clickTheFirstTimeSheetEntry();
		EmployeeEditTimeSheetBox employeeEditTimeSheetBox = employeeTaskDetailsMenuBox.clickEditButton();

		DatePickerForEditingTimeSheet DatePickerForEditingTimeSheet = employeeEditTimeSheetBox.clickDateField();

		boolean IsDateEnabledInEditPopUpFromCalenderView = DatePickerForEditingTimeSheet
				.verifyTheRequiredDateIsEnabledOrNot(R.TESTDATA.get("verifyDate.requiredYear"),
						R.TESTDATA.get("verifyDate.requiredMonth"), R.TESTDATA.get("verifyDate.requiredDay"));
		Assert.assertFalse(IsDateEnabledInEditPopUpFromCalenderView,
				"The event with AllowTask Field False is selectable in edit timesheet pop up from calender view.");

		// Verify the date is disabled in edit pop up from grid view

		timesheetLandingPageEmployee.refresh();

		isFilterApplied = timesheetLandingPageEmployee
				.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));
		Assert.assertTrue(isFilterApplied, "The required filter :" + R.TESTDATA.get("selectFilter.pending")
				+ " is not available in the filter options list.");

		timeSheetDetailsGridBox.expandTheTaskDetails();

		// Edit the date, time, id and description
		EmployeeEditTimeSheetBox employeeEditTimeSheetBoxForGrid = timeSheetDetailsGridBox.clickEditTaskButton();

		DatePickerForEditingTimeSheet datePickerForGrid = employeeEditTimeSheetBoxForGrid.clickDateField();

		boolean IsDateEnabledInEditPopUpFromGridView = datePickerForGrid.verifyTheRequiredDateIsEnabledOrNot(
				R.TESTDATA.get("verifyDate.requiredYear"), R.TESTDATA.get("verifyDate.requiredMonth"),
				R.TESTDATA.get("verifyDate.requiredDay"));
		Assert.assertFalse(IsDateEnabledInEditPopUpFromGridView,
				"The event with AllowTask Field False is selectable in edit timesheet pop up from grid view.");

		timesheetLandingPageEmployee.refresh();

		AddNewTimeSheetBox addNewTimeSheetBox = timesheetLandingPageEmployee.clickAddNewTimeSheetButton();
		DatePickerForAddingTimesheet datePickerForAddingTimesheet = addNewTimeSheetBox.clickTaskDateField();

		boolean IsDateEnabledInAddPopUp = datePickerForAddingTimesheet.verifyTheRequiredDateIsEnabledOrNot(
				R.TESTDATA.get("verifyDate.requiredYear"), R.TESTDATA.get("verifyDate.requiredMonth"),
				R.TESTDATA.get("verifyDate.requiredDay"));

		Assert.assertFalse(IsDateEnabledInAddPopUp,
				"The event with AllowTask Field False is selectable in add timesheet pop up");
	}
}