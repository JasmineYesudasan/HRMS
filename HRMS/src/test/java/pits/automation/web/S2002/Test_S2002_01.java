package pits.automation.web.S2002;

import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;

import pits.automation.web.hrms.employee.pages.AddNewTimeSheetBox;
import pits.automation.web.hrms.employee.pages.DatePickerForAddingTimesheet;
import pits.automation.web.hrms.employee.pages.TimeSheetDetailsCalenderBox;
import pits.automation.web.hrms.employee.pages.TimeSheetDetailsGridBox;
import pits.automation.web.hrms.pages.TimesheetLandingPage;
import pits.automation.web.login.BaseTest;

public class Test_S2002_01 extends BaseTest {

	// HPB_31 - Modification of Tasks in the grid based on new task workflow
	// including status filter
	// Scope - to verify each task entered in the timesheet has status rather than a
	// timesheet as a whole.

	@Test(priority = 1)

	// Verify filter is available in task details
	public void test_HPB33() throws InterruptedException {

		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
		Assert.assertTrue(timesheetLandingPage.getTimeSheetDetailsGridBox().getTimeSheetRow().isPresent(),
				"TimeSheet Details are empty. Please add a timesheet.");

		TimeSheetDetailsGridBox timesheetDetailsGridBox = timesheetLandingPage.getTimeSheetDetailsGridBox();

		// To verify status column is not present in the main timesheet table
		boolean isStatusPresentInTimeSheetDetails = timesheetDetailsGridBox.verifyTimeSheetDetailsHeader("Status");
		Assert.assertFalse(isStatusPresentInTimeSheetDetails, "Status column is available in the main table");

		// To verify status option is available in the task details table
		// boolean isStatusPresent =
		// timesheetDetailsGridBox.verifyTaskDetailsColumnHeaders("Status");
		// Assert.assertTrue(isStatusPresent, "Status is not available in the task
		// details table");

	}

	// HPB_31 - [REFACTOR] Modification of Tasks in the grid based on new task
	// workflow including status filter

	@Test(priority = 2)
	public void test_HPB31() throws InterruptedException {
		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));

		// Verify timesheet table is not empty
		Assert.assertTrue(timesheetLandingPage.getTimeSheetDetailsGridBox().getTimeSheetRow().isPresent(),
				"TimeSheet Details are empty. Please add a timesheet.");
		TimeSheetDetailsGridBox timesheetDetailsGridBox = timesheetLandingPage.getTimeSheetDetailsGridBox();

		// Apply filter with status Pending and select All from pagination
		timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));
		timesheetDetailsGridBox.displayAllRows();

		// Verify the status of all the filtered timesheet is pending
		boolean isStatusPending = timesheetDetailsGridBox
				.verifyTheStatusInTaskDetails(R.TESTDATA.get("selectFilter.pending"));
		Assert.assertTrue(isStatusPending, "Status other than Pending is found.Filter is not applied");

		timesheetLandingPage.refresh();

		// Apply filter with status Submitted and select All from pagination
		timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.submitted"));
		timesheetDetailsGridBox.displayAllRows();

		// Verify the status of all the filtered timesheet is submitted
		boolean isStatusSubmitted = timesheetDetailsGridBox
				.verifyTheStatusInTaskDetails(R.TESTDATA.get("selectFilter.submitted"));
		Assert.assertTrue(isStatusSubmitted, "Status other than Submitted is found.Filter is not applied");

	}

	// HPB_32 - [REFACTOR] Modification of Tasks in the calendar based on new task
	// workflow including status filter

	@Test(priority = 3)
	public void test_HPB32() throws InterruptedException {

		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
		TimeSheetDetailsCalenderBox timeSheetDetailsCalenderBox = timesheetLandingPage.getTimeSheetDetailsCalenderBox();

		// Toggle to calender view and verify timesheet entries are present for the
		// particular month
		timesheetLandingPage.toggleToCalender();
		Assert.assertTrue(timeSheetDetailsCalenderBox.getTimeSheetEntry().isPresent(),
				"There are no timesheet entries for this month");

		// Apply filter Submitted and verify filtered entries are present
		timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.submitted"));
		Assert.assertTrue(timeSheetDetailsCalenderBox.getTimeSheetEntry().isPresent(),
				"There are no timesheet entries with status Submitted for this month");

		// Fetch the class of all the filtered elements and verify it contains the text
		// "blue"
		ArrayList<String> classSubmitted = timeSheetDetailsCalenderBox.fetchClassOfFilteredTimeSheetEntries();

		for (String classOption : classSubmitted) {
			Assert.assertTrue(classOption.contains("blue"), "Filter contains timesheet other than in submitted status");
		}

		// Refresh the page and toggle to calender mode.
		timesheetLandingPage.refresh();
		timesheetLandingPage.toggleToCalender();

		// Apply filter Pending and verify filtered entries are present
		timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));
		Assert.assertTrue(timeSheetDetailsCalenderBox.getTimeSheetEntry().isPresent(),
				"There are no timesheet entries with status Pending for this month");

		// Fetch the class of all the filtered elements and verify it does not contain
		// the text "blue"
		ArrayList<String> classPending = timeSheetDetailsCalenderBox.fetchClassOfFilteredTimeSheetEntries();

		for (String classOption : classPending) {
			System.out.println(classOption);
			Assert.assertFalse(classOption.contains("blue"), "Filter contains timesheet other than in pending status.");

		}

	}

	// HPB_22 - Submit the timesheet entries in grid view

	// Scope - Add a timesheet;Verify the added timesheet;Verify submit button
	@Test(priority = 4)
	public void test_HPB22() throws InterruptedException, Exception {

		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));

		AddNewTimeSheetBox addNewTimeSheetBox = timesheetLandingPage.getAddNewTimeSheetBox();
		TimeSheetDetailsGridBox timesheetDetailsGridBox = timesheetLandingPage.getTimeSheetDetailsGridBox();

		// Add a new timesheet
		timesheetLandingPage.clickAddNewTimeSheetButton();

		DatePickerForAddingTimesheet datePicker = addNewTimeSheetBox.clickTaskDateField();
		datePicker.selectDate(R.TESTDATA.get("selectDate.requiredYear"), R.TESTDATA.get("selectDate.requiredMonth"),
				R.TESTDATA.get("selectDate.requiredDay"));

		addNewTimeSheetBox.enterTaskID(R.TESTDATA.get("enterTaskID.taskId"));
		addNewTimeSheetBox.selectTime(R.TESTDATA.get("selectTime.time"));
		addNewTimeSheetBox.enterTaskDescription(R.TESTDATA.get("enterTaskDescription.description"));
		addNewTimeSheetBox.clickSaveButton();

		// Verifying the success alert is displayed
		Assert.assertTrue(timesheetLandingPage.getsuccessAlertOnAddingTimesheet().isPresent(),
				"Success Message is not displayed");

		// Selecting option "All" from pagination
		timesheetDetailsGridBox.displayAllRows();

		// Expanding the taskdetails of the date for which the timesheet was added
		timesheetDetailsGridBox.expandTimesheetOfRequiredDate(R.TESTDATA.get("selectDate.selectedDate"));

		// Selecting option "All" from pagination for task details
		timesheetDetailsGridBox.displayAllRows();

		// Verfying the timesheet added is present and status is pending
		List<String> detailsFetched = timesheetDetailsGridBox.fetchTaskDetails();
		boolean isTimeSheetAdded = false;
		for (String details : detailsFetched) {
			if (details.contains(R.TESTDATA.get("enterTaskID.taskId"))
					&& details.contains(R.TESTDATA.get("enterTaskDescription.description"))) {
				isTimeSheetAdded = true;
				System.out.println("Timesheet has been added succesfully");
				break;
			}

		}
		if (isTimeSheetAdded == false) {
			fail("Timesheet is not added");
		}

		WebElement statusOfAddedTimeSheet = timesheetDetailsGridBox
				.verifyStatusOfAddedTimesheet(R.TESTDATA.get("enterTaskDescription.description"));

		Assert.assertTrue(statusOfAddedTimeSheet.getText().equals("Pending"),
				"The status of added timesheet is not Pending");
		// Verfying submit button is available if the date is a past date or current
		// date
		WebElement submitButton = timesheetDetailsGridBox.verifySubmitButton();
		boolean isFutureDate = timesheetDetailsGridBox.verifyIfFutureDate();
		if (isFutureDate == false) {
			Assert.assertTrue(submitButton.isDisplayed(), "Submit Button is not available for the added timesheet");
		} else if (isFutureDate == true) {
			Assert.assertFalse(submitButton.isDisplayed(),
					"Submit Button is available for timesheet entries in future date");
		}
		// Submit the task
		submitButton.click();

		// Verify success alert is displayed

		Assert.assertTrue(timesheetLandingPage.getSuccessAlertOnSubmittingTimesheet().isPresent(),
				"Success Alert is not present");

		Assert.assertEquals(timesheetLandingPage.getSuccessAlertOnSubmittingTimesheet().getText(),
				"Task Submitted successfully",
				"Success Message on submitting the added timesheet differs from the expected one");

		timesheetLandingPage.refresh();
		timesheetDetailsGridBox.displayAllRows();
		// Expanding the taskdetails of the date for which the timesheet was added
		timesheetDetailsGridBox.expandTimesheetOfRequiredDate(R.TESTDATA.get("selectDate.selectedDate"));

		// Selecting option "All" from pagination for task details
		timesheetDetailsGridBox.displayAllRows();

		WebElement statusOfSubmittedTimeSheet = timesheetDetailsGridBox
				.verifyStatusOfAddedTimesheet(R.TESTDATA.get("enterTaskDescription.description"));

		Assert.assertTrue(statusOfSubmittedTimeSheet.getText().equals("Submitted"),
				"The status of Submitted timesheet is not Submitted");
	}

	// HPB_23 - Submit the timesheet entries in calendar view

	@Test(priority = 5)
	public void test_HPB23() throws InterruptedException {
		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
		TimeSheetDetailsCalenderBox timeSheetDetailsCalenderBox = timesheetLandingPage.getTimeSheetDetailsCalenderBox();

		// Toggle to calender view and verify timesheet entries are present for the
		// particular month
		timesheetLandingPage.toggleToCalender();
		Assert.assertTrue(timeSheetDetailsCalenderBox.getTimeSheetEntry().isPresent(),
				"There are no timesheet entries for this month");

		// Apply filter Submitted and verify filtered entries are present
		timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));
		Assert.assertTrue(timeSheetDetailsCalenderBox.getTimeSheetEntry().isPresent(),
				"There are no timesheet entries with status Pending for this month");

		// Verify past date entries in pending status have submit button

		if (timeSheetDetailsCalenderBox.getPastDateEntries().isEmpty()) {
			System.out.println("There are no past date time sheet entries in Pending status  for the month");
		} else {
			boolean isSubmitButtonPresentInPastEntries = timeSheetDetailsCalenderBox
					.verifySubmitButtonOfEntriesFromPastDate();
			Assert.assertTrue(isSubmitButtonPresentInPastEntries,
					"Submit Button is not available for entries from paste date");
		}

		// Verify present day entries in pending status have submit button.
		if (timeSheetDetailsCalenderBox.getPresentDayEntries().isEmpty()) {
			System.out.println("There are no time sheet entries in pending status for the current day");
		} else {
			boolean isSubmitButtonPresentInPresentDayEntries = timeSheetDetailsCalenderBox
					.verifySubmitButtonOfEntriesFromPresentDate();
			Assert.assertTrue(isSubmitButtonPresentInPresentDayEntries,
					"Submit Button is not available for present day entries.");
		}

		// Verify future date entries in pending status doesnot have submit button
		if (timeSheetDetailsCalenderBox.getFutureDateEntries().isEmpty()) {
			System.out.println("There are no future date time sheet entries for the month");
		} else {
			boolean isSubmitButtonPresentInFutureEntries = timeSheetDetailsCalenderBox
					.verifySubmitButtonOfEntriesInFuturetDate();
			Assert.assertFalse(isSubmitButtonPresentInFutureEntries,
					"Submit Button is available for future date entries.");
		}

		timesheetLandingPage.refresh();
		timesheetLandingPage.toggleToCalender();
		timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));

		String firstEntryText = timeSheetDetailsCalenderBox.getFirstEntryInPendingStatus().getText();

		// Submitting the task
		timeSheetDetailsCalenderBox.submitEntryFromCalenderMode();

		// Verifying the success alert
		Assert.assertTrue(timesheetLandingPage.getSuccessAlertOnSubmittingTimesheetInCalenderMode().isPresent(),
				"Success Alert is not displayed on submitting the timesheet. The timesheet has not been submitted");

		Assert.assertTrue(
				timesheetLandingPage.getSuccessAlertOnSubmittingTimesheetInCalenderMode().getText()
						.equals("Task Submitted successfully"),
				"The message in the success alert differs from the expected one");

		timesheetLandingPage.refresh();
		timesheetLandingPage.toggleToCalender();

		// Verifying that the submitted task is not available in pending list
		timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.pending"));

		List<ExtendedWebElement> pastDateEntries = timeSheetDetailsCalenderBox.getPastDateEntries();
		for (ExtendedWebElement entry : pastDateEntries) {
			if (entry.getText().equals(firstEntryText)) {
				fail("The submitted task is still available in the pending list");
				break;
			}
		}

		timesheetLandingPage.refresh();
		timesheetLandingPage.toggleToCalender();

		// Verifying that the submitted task is available in submitted list
		timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.submitted"));
		boolean taskFound = false;

		for (ExtendedWebElement entry : pastDateEntries) {
			if (entry.getText().equals(firstEntryText)) {
				taskFound = true;
				System.out.println("The submitted task is available in the submitted task list");
				break;
			}
		}
		if (taskFound == false) {
			fail("The submitted task is not available in the submitted task list");
		}

	}
}
