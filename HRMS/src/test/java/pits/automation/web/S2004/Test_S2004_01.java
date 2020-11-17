package pits.automation.web.S2004;

import static org.testng.Assert.fail;

import java.text.ParseException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;

import pits.automation.web.dbms.DataBaseQueries;
import pits.automation.web.devops.pages.DevOpsLandingPage;
import pits.automation.web.devops.pages.DevOpsLoginPage;
import pits.automation.web.devops.pages.DevOpsTaskDetailsBox;
import pits.automation.web.devops.pages.ProjectCardBox;
import pits.automation.web.devops.pages.TaskBoardLandingPage;
import pits.automation.web.devops.pages.WorkItemsBox;
import pits.automation.web.hrms.employee.devops.pages.DevOpsAddTimeSheetBox;
import pits.automation.web.hrms.employee.devops.pages.DevOpsEditTimeSheetBox;
import pits.automation.web.hrms.employee.devops.pages.DevopsTaskDetailsBox;
import pits.automation.web.hrms.employee.pages.AddNewTimeSheetBox;
import pits.automation.web.hrms.employee.pages.DatePickerForAddingTimesheet;
import pits.automation.web.hrms.employee.pages.DatePickerForEditingTimeSheet;
import pits.automation.web.hrms.employee.pages.EmployeeEditTimeSheetBox;
import pits.automation.web.hrms.employee.pages.EmployeeTaskDetailsMenuBox;
import pits.automation.web.hrms.employee.pages.TimeSheetDetailsCalenderBox;
import pits.automation.web.hrms.employee.pages.TimeSheetDetailsGridBox;
import pits.automation.web.hrms.pages.TimesheetLandingPage;
import pits.automation.web.login.BaseTest;

public class Test_S2004_01 extends BaseTest {

	// HPB- 42/43 - [DevOps Integration] - Add/Edit Task

	// The scope of this ticket is to add new tasks by extracting details from the
	// tasks that are assigned to an employee in devops.

	@Test(priority = 0)
	public void verifyAddandEditTimesheetThroughDevops() throws InterruptedException {

		// Login to Devops
		DevOpsLoginPage devOpsLoginPage = new DevOpsLoginPage(getDriver());
		DevOpsLandingPage devOpsLandingPage = devOpsLoginPage.devopsEmployeeLogin();

		Assert.assertTrue(devOpsLandingPage.getProjectCard().isVisible(),
				"Devops Login is not successfull for the user : " + R.TESTDATA.get("devopsEmployeeLogin.userName"));

		ProjectCardBox projectCardBox = devOpsLandingPage.getProjectCardBox();

		// Select the project and navigate to taskboard
		boolean isProjectSelected = projectCardBox.selectTheRequiredProject(R.TESTDATA.get("selectProject.project"));
		Assert.assertTrue(isProjectSelected, "The required project : " + R.TESTDATA.get("selectProject.project")
				+ " is not available for the logged in user.");
		TaskBoardLandingPage taskBoardLandingPage = devOpsLandingPage.navigateToTaskBoard();
		WorkItemsBox workItemsBox = taskBoardLandingPage.getWorkItemsBox();

		// Add a new task
		workItemsBox.clickAddWorkItemButton();
		workItemsBox.selectWorkItem(R.TESTDATA.get("selectWorkItem.workItem"));

		workItemsBox.enterTaskTitle(R.TESTDATA.get("enterTaskTitle.taskTitle"));

		// Open the task added and select the assignee and provide the estimates
		DevOpsTaskDetailsBox devOpsTaskDetailsBox = workItemsBox
				.selectTheRequiredTask(R.TESTDATA.get("enterTaskTitle.taskTitle"));

		String idofTheTaskAdded = devOpsTaskDetailsBox.getTaskIdField().getText();

		devOpsTaskDetailsBox.selectAssigneeForTheTask(R.TESTDATA.get("selectAssigneeForTheTask.assignee"));

		devOpsTaskDetailsBox.provideTheEstimates(R.TESTDATA.get("provideTheEstimates.originalEstimate"),
				R.TESTDATA.get("provideTheEstimates.remainingEstimate"), R.TESTDATA.get(""));

		devOpsTaskDetailsBox.clickSaveAndCloseButton();

		// Fetch the count of tasks assigned for the required user
		int countOfTasksAssignedInDeVops = workItemsBox.fetchTheCountOfTasksAssignedForTheEmployee(
				R.TESTDATA.get("applyFilterWithTheRequiredAssignee.assignee"));

		// Login to HRMS
		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryIdTwo"),
				R.TESTDATA.get("employeeLogin.password"));
		TimeSheetDetailsGridBox timesheetDetailsGridBox = timesheetLandingPage.getTimeSheetDetailsGridBox();

		Assert.assertTrue(timesheetLandingPage.isPageOpened(), "Login is not successfull for the employee ' : "
				+ R.TESTDATA.get("employeeLogin.militaryId") + "'. The required landing page is not opened on login.");

		// Verify PAT token is not required and open the devops task list from the add
		// timesheet popup
		timesheetLandingPage.clickAddNewTimeSheetButton();
		DevopsTaskDetailsBox devopsTaskDetailsBox = timesheetLandingPage.getAddNewTimeSheetBox().clickDevOpsIcon();
		Assert.assertFalse(timesheetLandingPage.getPatTokenBox().getPatTokenField().isVisible(), "The user : "
				+ R.TESTDATA.get("employeeLogin.militaryIdTwo")
				+ " is required to enter the PAT Token. Either the existing token expired or there are no tasks assigned for the employee in DevOps.");
		devopsTaskDetailsBox.displayAllRows("All");

		// Verify the count of tasks assigned for the employee in devops and the count
		// of tasks displayed in HRMS
		int countOfTasksDisplayed = devopsTaskDetailsBox.fetchCountOfTasksDisplayed();
		if (!(countOfTasksAssignedInDeVops == countOfTasksDisplayed)) {
			fail("The count of tasks displayed for the employee in devops and in HRMS do not match. Expected : "
					+ countOfTasksAssignedInDeVops + " Displayed : " + countOfTasksDisplayed);
		}

		// Open the task added in the previous steps from the task list displayed for
		// the employee
		DevOpsAddTimeSheetBox devOpsAddTimeSheetBox = devopsTaskDetailsBox
				.selectTheRequiredTaskForAddingTimeSheet(idofTheTaskAdded);

		// Verify the fields Task Id, Task Title, Original Estimate, Completed Time and
		// Remaining Time is readonly
		Assert.assertTrue(
				devOpsAddTimeSheetBox.getTaskIdField().getAttribute(R.TESTDATA.get("getAttribute.attribute"))
						.equalsIgnoreCase(R.TESTDATA.get("getAttribute.value")),
				"The field Task Id is not read only in add timesheet through devops popup.");

		Assert.assertTrue(
				devOpsAddTimeSheetBox.getTaskTitle().getAttribute(R.TESTDATA.get("getAttribute.attribute"))
						.equalsIgnoreCase(R.TESTDATA.get("getAttribute.value")),
				"The field Task Title is not read only in add timesheet through devops popup.");

		Assert.assertTrue(
				devOpsAddTimeSheetBox.getOriginalEstimate().getAttribute(R.TESTDATA.get("getAttribute.attribute"))
						.equalsIgnoreCase(R.TESTDATA.get("getAttribute.value")),
				"The field Original Estimate is not read only in add timesheet through devops popup.");

		Assert.assertTrue(
				devOpsAddTimeSheetBox.getCompletedTime().getAttribute(R.TESTDATA.get("getAttribute.attribute"))
						.equalsIgnoreCase(R.TESTDATA.get("getAttribute.value")),
				"The field Completed Time is not read only in add timesheet through devops popup.");

		Assert.assertTrue(
				devOpsAddTimeSheetBox.getRemainingTime().getAttribute(R.TESTDATA.get("getAttribute.attribute"))
						.equalsIgnoreCase(R.TESTDATA.get("getAttribute.value")),
				"The field Remaining Time is not read only in add timesheet through devops popup.");

		// Select the date and time and provide the description for the task and click
		// the save button
		DatePickerForAddingTimesheet datePickerForAddingTimesheet = devOpsAddTimeSheetBox.clicktaskDateField();

		datePickerForAddingTimesheet.selectDate(R.TESTDATA.get("selectDate.requiredYearfordevops"),
				R.TESTDATA.get("selectDate.requiredMonthfordevops"), R.TESTDATA.get("selectDate.requiredDayfordevops"));

		devOpsAddTimeSheetBox.selectTime(R.TESTDATA.get("selectTime.requiredTimefordevops"));

		devOpsAddTimeSheetBox.enterTaskDescription(R.TESTDATA.get("enterTaskDescription.devopsDescription"));

		devOpsAddTimeSheetBox.clickSaveButton();

		// Verify the success alert on adding the task
		Assert.assertTrue(devOpsAddTimeSheetBox.getSuccessAlertOnAddingTimesheet().isVisible(),
				"Success Alert is not displayed on adding a timesheet through devops.");

		Assert.assertTrue(
				devOpsAddTimeSheetBox.getSuccessAlertOnAddingTimesheet().getText()
						.equalsIgnoreCase("Timesheet saved successfully"),
				"The message displayed on the success alert on adding a timesheet through devops is not as expected. The message displayed is : "
						+ devOpsAddTimeSheetBox.getSuccessAlertOnAddingTimesheet().getText());

		timesheetLandingPage.refresh();

		// Select option "All" from pagination
		timesheetDetailsGridBox.displayAllRows();

		// Expand the task details of the date for which the timesheet was added
		timesheetDetailsGridBox.expandTimesheetOfRequiredDate(R.TESTDATA.get("selectDate.devOpsDate"));

		// Selecting option "All" from pagination for task details
		timesheetDetailsGridBox.displayAllRows();

		// Verfying the timesheet added is present and status is pending
		List<String> detailsFetched = timesheetDetailsGridBox.fetchTaskDetails();
		boolean isTimeSheetAdded = false;
		for (String details : detailsFetched) {
			if (details.contains(idofTheTaskAdded)
					&& details.contains(R.TESTDATA.get("selectTime.requiredTimefordevops"))
					&& details.contains(R.TESTDATA.get("enterTaskDescription.devopsDescription"))) {
				isTimeSheetAdded = true;
				System.out.println("Timesheet has been added succesfully");
				break;
			}

		}
		if (isTimeSheetAdded == false) {
			fail("Timesheet added is not available in the task list of the employee.");
		}

		WebElement statusOfAddedTimeSheet = timesheetDetailsGridBox
				.verifyStatusOfAddedTimesheet(R.TESTDATA.get("enterTaskDescription.devopsDescription"));

		Assert.assertTrue(statusOfAddedTimeSheet.getText().equals("Pending"),
				"The status of the newly added timesheet is not Pending. The status displayed is :"
						+ statusOfAddedTimeSheet.getText());

		// Verify the newly added timesheet is updated in the database
		DataBaseQueries dataBaseQueries = new DataBaseQueries(getDriver(), getDriver());
		List<String> taskDetailsListFromDB = dataBaseQueries.fetchTaskDetailsFromDataBase(
				R.DATABASE.get("db.connectionURL"), R.TESTDATA.get("selectDate.dbDate"),
				R.TESTDATA.get("employeeId.id"));
		boolean isTaskUpdatedInDB = false;
		for (String taskDetails : taskDetailsListFromDB) {
			System.out.println(taskDetails);
			if (taskDetails.contains(idofTheTaskAdded)
					&& taskDetails.contains(R.TESTDATA.get("enterTaskDescription.devopsDescription"))
					&& taskDetails.contains(R.TESTDATA.get("selectTime.requiredTimefordevops"))) {
				isTaskUpdatedInDB = true;
				break;
			}
		}
		if (isTaskUpdatedInDB == false) {
			fail("The task details of the timesheet added is not properly updated in the DataBase.");
		}

		// Login to HRMS again to verify editing the task added above

		TimesheetLandingPage timesheetLandingpage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryIdTwo"),
				R.TESTDATA.get("employeeLogin.password"));

		// Select option "All" from pagination
		timesheetDetailsGridBox.displayAllRows();

		// Expand the task details of the date for which the timesheet was added
		timesheetDetailsGridBox.expandTimesheetOfRequiredDate(R.TESTDATA.get("selectDate.devOpsDate"));

		// Selecting option "All" from pagination for task details
		timesheetDetailsGridBox.displayAllRows();

		int countOfTasksBeforeEditing = timesheetDetailsGridBox.fetchTheCountOfTasksForTheDate();

		EmployeeEditTimeSheetBox employeeEditTimeSheetBox = timesheetDetailsGridBox
				.clickEditButtonOfRequiredTask(idofTheTaskAdded);
		Assert.assertTrue(employeeEditTimeSheetBox.getDevopsButton().isVisible(),
				"DevOps button is not available for the task.");

		DevopsTaskDetailsBox devopsTaskDetailsbox = employeeEditTimeSheetBox.clickDevOpsButton();
		devopsTaskDetailsbox.displayAllRows("All");

		DevOpsEditTimeSheetBox devOpsEditTimeSheetBox = devopsTaskDetailsBox
				.selectTheRequiredTaskForEditingTimeSheet(idofTheTaskAdded);

		DatePickerForEditingTimeSheet datePickerForEditingTimeSheet = devOpsEditTimeSheetBox.clickDateField();

		datePickerForEditingTimeSheet.selectDate(R.TESTDATA.get("selectDate.editedYear"),
				R.TESTDATA.get("selectDate.editedMonth"), R.TESTDATA.get("selectDate.editedDay"));

		devOpsEditTimeSheetBox.editTimeField(R.TESTDATA.get("selectTime.editedTime"));
		devOpsEditTimeSheetBox.editTaskDescription(R.TESTDATA.get("enterTaskDescription.editeddevopsDescription"));
		devOpsEditTimeSheetBox.clickUpdateButton();
		timesheetLandingpage.refresh();

		timesheetDetailsGridBox
				.expandTimesheetOfRequiredDate(R.TESTDATA.get("expandTimesheetOfRequiredDate.editedDate"));

		timesheetDetailsGridBox.displayAllRows();

		// Verfying the timesheet added is present and status is pending
		List<String> detailsFetchedAfterEditingTimesheet = timesheetDetailsGridBox.fetchTaskDetails();
		boolean isTimeSheetEdited = false;
		for (String details : detailsFetchedAfterEditingTimesheet) {
			if (details.contains(idofTheTaskAdded) && details.contains(R.TESTDATA.get("selectTime.editedTime"))
					&& details.contains(R.TESTDATA.get("enterTaskDescription.editeddevopsDescription"))) {
				isTimeSheetEdited = true;
				System.out.println("Timesheet has been edited succesfully");
				break;
			}

		}
		if (isTimeSheetEdited == false) {
			fail("Timesheet edit was not successfull. The edited data is not displayed in the task details for the date :"
					+ R.TESTDATA.get("expandTimesheetOfRequiredDate.editedDate"));
		}

		timesheetLandingpage.refresh();
		timesheetDetailsGridBox.displayAllRows();

		if (countOfTasksBeforeEditing == 1) {
			List<ExtendedWebElement> dateList = timesheetDetailsGridBox.getTaskDateList();
			for (ExtendedWebElement date : dateList) {
				if (date.getText().equals(R.TESTDATA.get("selectDate.devOpsDate"))) {
					fail("The edited task is still available in the previous date.");
				}
			}
		}

		else if (countOfTasksBeforeEditing > 1) {
			timesheetDetailsGridBox.expandTimesheetOfRequiredDate(R.TESTDATA.get("selectDate.devOpsDate"));

			List<String> detailsFetchedAfterEditing = timesheetDetailsGridBox.fetchTaskDetails();
			for (String details : detailsFetchedAfterEditing) {
				if (details.contains(idofTheTaskAdded) && details.contains(R.TESTDATA.get("selectTime.editedTime"))
						&& details.contains(R.TESTDATA.get("enterTaskDescription.editeddevopsDescription"))) {
					System.out.println(
							"Timesheet edit is not successfull. The details are still displayed in the old date.");
					break;
				}

			}

		}

		List<String> taskDetailsListFromDBAfterEditing = dataBaseQueries.fetchTaskDetailsFromDataBase(
				R.DATABASE.get("db.connectionURL"), R.TESTDATA.get("editDate.dbDate"), R.TESTDATA.get("employeeId.id"));
		boolean isEditedTaskUpdatedInDB = false;
		for (String taskDetails : taskDetailsListFromDBAfterEditing) {
			System.out.println(taskDetails);
			if (taskDetails.contains(idofTheTaskAdded)
					&& taskDetails.contains(R.TESTDATA.get("enterTaskDescription.editeddevopsDescription"))
					&& taskDetails.contains(R.TESTDATA.get("selectTime.editedTime"))) {
				isEditedTaskUpdatedInDB = true;
				break;
			}
		}
		if (isEditedTaskUpdatedInDB == false) {
			fail("The task details of the timesheet edited is not properly updated in the DataBase.");
		}

	}

	@Test()
	public void verifyEditFromCalenderView() throws ParseException, InterruptedException {

		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryIdTwo"),
				R.TESTDATA.get("employeeLogin.password"));
		TimeSheetDetailsCalenderBox timeSheetDetailsCalenderBox = timesheetLandingPage.getTimeSheetDetailsCalenderBox();

		timesheetLandingPage.toggleToCalender();

		int yearToSelect = timeSheetDetailsCalenderBox
				.formatDateAndGetYear(R.TESTDATA.get("expandTimesheetOfRequiredDate.editedDate"));
		String monthToSelect = timeSheetDetailsCalenderBox
				.formatDateAndGetMonth(R.TESTDATA.get("expandTimesheetOfRequiredDate.editedDate"));
		timeSheetDetailsCalenderBox.selectTheRequiredYearInCalenderView(yearToSelect);

		timeSheetDetailsCalenderBox.selectTheRequiredMonthInCalenderView(monthToSelect);

		EmployeeTaskDetailsMenuBox employeeTaskDetailsMenuBox = timeSheetDetailsCalenderBox
				.clickTheRequiredTaskInCalenderView("80");

		String taskDetailsDisplayed = employeeTaskDetailsMenuBox.getTaskDetailsDisplayed().getText();

		Assert.assertTrue(taskDetailsDisplayed.contains(R.TESTDATA.get("enterTaskDescription.editeddevopsDescription")),
				"The description displayed in the task details is not the edited description.");

		Assert.assertTrue(taskDetailsDisplayed.contains(R.TESTDATA.get("selectTime.editedTime")),
				"The time displayed in the task details is not the edited time.");

		Thread.sleep(6000);

	}

	// HPB- 50[EVENTS] - Add holiday and other events

	@Test(priority = 1)
	public void verifyAnEventDate() throws InterruptedException, Exception {
		// Login as employee
		TimesheetLandingPage timesheetLandingPageEmployee = employeeLogin(R.TESTDATA.get("employeeLogin.militaryIdTwo"),
				R.TESTDATA.get("employeeLogin.password"));
		Assert.assertTrue(timesheetLandingPageEmployee.isPageOpened(),
				"Login is not successfull for the employee ' : " + R.TESTDATA.get("employeeLogin.militaryIdTwo")
						+ "'. The required landing page is not opened on login.");
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