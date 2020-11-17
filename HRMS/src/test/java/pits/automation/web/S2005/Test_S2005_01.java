package pits.automation.web.S2005;

import static org.testng.Assert.fail;

import java.text.ParseException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.qaprosoft.carina.core.foundation.utils.R;

import pits.automation.web.dbms.DataBaseQueries;
import pits.automation.web.devops.pages.DevOpsLandingPage;
import pits.automation.web.devops.pages.DevOpsLoginPage;
import pits.automation.web.devops.pages.DevOpsTaskDetailsBox;
import pits.automation.web.devops.pages.ProjectCardBox;
import pits.automation.web.devops.pages.TaskBoardLandingPage;
import pits.automation.web.devops.pages.WorkItemsBox;
import pits.automation.web.hrms.employee.devops.pages.DevOpsAddTimeSheetBox;
import pits.automation.web.hrms.employee.devops.pages.DevopsTaskDetailsBox;
import pits.automation.web.hrms.employee.pages.AddNewTimeSheetBox;
import pits.automation.web.hrms.employee.pages.DatePickerForAddingTimesheet;
import pits.automation.web.hrms.employee.pages.EmployeeEditTimeSheetBox;
import pits.automation.web.hrms.employee.pages.EmployeeTaskDetailsMenuBox;
import pits.automation.web.hrms.employee.pages.TimeSheetDetailsCalenderBox;
import pits.automation.web.hrms.employee.pages.TimeSheetDetailsGridBox;
import pits.automation.web.hrms.manager.pages.ManagerCalenderBox;
import pits.automation.web.hrms.manager.pages.ManagerLandingPage;
import pits.automation.web.hrms.manager.pages.ManagerTaskDetailsMenuBox;
import pits.automation.web.hrms.pages.HeaderBox;
import pits.automation.web.hrms.pages.TimesheetLandingPage;
import pits.automation.web.login.BaseTest;

public class Test_S2005_01 extends BaseTest {

	// HPB-55 - [Timesheet] - Add title field for the task and implementation of
	// mandatory validation

	@Test(priority = 1)
	public void verifyTaskTitleFieldInAddEditTimeSheetPopUpsForNormalTasks()
			throws InterruptedException, ParseException {

		// Login as employee
		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryIdTwo"),
				R.TESTDATA.get("employeeLogin.password"));
		Assert.assertTrue(timesheetLandingPage.isPageOpened(),
				"Login is not successfull for the employee : " + R.TESTDATA.get("employeeLogin.militaryIdTwo")
						+ ". The required landing page is not opened on login.");

		TimeSheetDetailsGridBox timesheetDetailsGridBox = timesheetLandingPage.getTimeSheetDetailsGridBox();
		AddNewTimeSheetBox addNewTimeSheetBox = timesheetLandingPage.clickAddNewTimeSheetButton();

		// Verify Task Title Field is available in the add new time sheet pop up.
		Assert.assertTrue(addNewTimeSheetBox.getTaskTitleField().getElement().isDisplayed(),
				"The task title field is not available in the add new timesheet pop up for the employee.");

		// verify Task title field is the first field in the add timesheet pop up.
		Assert.assertTrue(addNewTimeSheetBox.getlabelOfFirstFieldInAddPopUp().getText().equalsIgnoreCase("Task Title"),
				"The first field in the add new timesheet pop up is not Task Title. The field displayed is: "
						+ addNewTimeSheetBox.getlabelOfFirstFieldInAddPopUp().getText());

		// Verify Task Title field is mandatory
		addNewTimeSheetBox.clickSaveButton();
		addNewTimeSheetBox.verifyErrorInFieldLabels();

		// Add a new timesheet with task title
		DatePickerForAddingTimesheet datePickerForAddingTimesheet = addNewTimeSheetBox.clickTaskDateField();
		datePickerForAddingTimesheet.selectDate(R.TESTDATA.get("year.requiredYear"),
				R.TESTDATA.get("month.requiredMonth"), R.TESTDATA.get("day.requiredDay"));

		addNewTimeSheetBox.enterTaskID(R.TESTDATA.get("task.taskID"));

		addNewTimeSheetBox.selectTime(R.TESTDATA.get("time.requiredTime"));

		addNewTimeSheetBox.clickSaveButton();

		Assert.assertTrue(
				addNewTimeSheetBox.getlabelOfFirstFieldInAddPopUp().getAttribute("class").contains("error--text"),
				"The mandatory validation is not displayed for the task title field.");

		addNewTimeSheetBox.enterTaskTitle(R.TESTDATA.get("title.taskTitle"));

		addNewTimeSheetBox.clickSaveButton();

		// Verify the success alert
		Assert.assertTrue(timesheetLandingPage.getsuccessAlertOnAddingTimesheet().isVisible(),
				"Success Alert is not displayed on adding a timesheet through devops.");

		Assert.assertTrue(
				timesheetLandingPage.getsuccessAlertOnAddingTimesheet().getText()
						.equalsIgnoreCase("Timesheet saved successfully"),
				"The message displayed on the success alert on adding a timesheet through devops is not as expected. The message displayed is : "
						+ timesheetLandingPage.getsuccessAlertOnAddingTimesheet());

		timesheetLandingPage.refresh();

		// Select option "All" from pagination
		timesheetDetailsGridBox.displayAllRows();

		timesheetLandingPage.navigateToTopOfThePage();

		// Expand the task details of the date for which the timesheet was added
		timesheetDetailsGridBox.expandTimesheetOfRequiredDate(R.TESTDATA.get("date.requiredDate"));

		// Selecting option "All" from pagination for task details
		timesheetDetailsGridBox.displayAllRows();

		// Verify task title header is available on expanding the task grid.
		boolean isTitlePresentInHeader = timesheetDetailsGridBox
				.verifyTaskDetailsHeader(R.TESTDATA.get("header.taskTitle"));

		Assert.assertTrue(isTitlePresentInHeader,
				"Task Title is not available as a header option in task details table.");

		// Verfying the timesheet added is present and status is pending
		List<String> detailsFetched = timesheetDetailsGridBox.fetchTaskDetails();
		boolean isTimeSheetAdded = false;
		for (String details : detailsFetched) {
			if (details.contains(R.TESTDATA.get("task.taskID")) && details.contains(R.TESTDATA.get("title.taskTitle"))
					&& details.contains(R.TESTDATA.get("time.requiredTime"))) {
				isTimeSheetAdded = true;
				System.out.println("Timesheet has been added succesfully");
				break;
			}

		}
		if (isTimeSheetAdded == false) {
			fail("Timesheet added is not available in the task list of the employee.");
		}

		WebElement statusOfAddedTimeSheet = timesheetDetailsGridBox
				.verifyStatusOfAddedTimesheet(R.TESTDATA.get("title.taskTitle"));

		Assert.assertTrue(statusOfAddedTimeSheet.getText().equals("Pending"),
				"The status of the newly added timesheet is not Pending. The status displayed is :"
						+ statusOfAddedTimeSheet.getText());

		// Verify the newly added timesheet is updated in the database
		DataBaseQueries dataBaseQueries = new DataBaseQueries(getDriver(), getDriver());
		List<String> taskDetailsListFromDB = dataBaseQueries.fetchTaskDetailsFromDataBase(
				R.DATABASE.get("db.connectionURL"), R.TESTDATA.get("date.dbDate"), R.TESTDATA.get("employeeId.id"));
		boolean isTaskUpdatedInDB = false;
		for (String taskDetails : taskDetailsListFromDB) {
			if (taskDetails.contains(R.TESTDATA.get("task.taskID"))
					&& taskDetails.contains(R.TESTDATA.get("title.taskTitle"))
					&& taskDetails.contains(R.TESTDATA.get("time.requiredTime"))) {
				isTaskUpdatedInDB = true;
				System.out.println("Task Details Fetched from DataBase :" + taskDetails);
				break;
			}
		}
		if (isTaskUpdatedInDB == false) {
			fail("The task details of the timesheet added is not properly updated in the DataBase.");
		}

		timesheetLandingPage.refresh();

		timesheetDetailsGridBox.displayAllRows();

		timesheetLandingPage.navigateToTopOfThePage();

		// Expand the task details of the date for which the timesheet was added
		timesheetDetailsGridBox.expandTimesheetOfRequiredDate(R.TESTDATA.get("date.requiredDate"));

		// Selecting option "All" from pagination for task details
		timesheetDetailsGridBox.displayAllRows();

		// Edit the title of the newly added timesheet
		EmployeeEditTimeSheetBox employeeEditTimeSheetBox = timesheetDetailsGridBox
				.clickEditButtonOfRequiredTask(R.TESTDATA.get("task.taskID"));

		employeeEditTimeSheetBox.editTaskTitle(R.TESTDATA.get("title.editedTaskTitle"));

		employeeEditTimeSheetBox.clickUpdateButton();

		timesheetLandingPage.refresh();

		timesheetDetailsGridBox.displayAllRows();

		timesheetLandingPage.navigateToTopOfThePage();

		// Expand the task details of the date for which the timesheet was edited
		timesheetDetailsGridBox.expandTimesheetOfRequiredDate(R.TESTDATA.get("date.requiredDate"));

		// Selecting option "All" from pagination for task details
		timesheetDetailsGridBox.displayAllRows();

		// Verify the edited task title is properly reflected
		boolean isTaskTitleEdited = false;
		for (String details : detailsFetched) {
			if (details.contains(R.TESTDATA.get("task.taskID")) && details.contains(R.TESTDATA.get("title.taskTitle"))
					&& details.contains(R.TESTDATA.get("time.requiredTime"))) {
				isTaskTitleEdited = true;
				System.out.println("Task Title has been edited succesfully");
				break;
			}

		}
		if (isTaskTitleEdited == false) {
			fail("Task Title edit is not succesfull.");
		}

		// Verify the edited title is updated in Database
		List<String> taskTitleListFromDB = dataBaseQueries.fetchTaskTitleFromDatabase(
				R.DATABASE.get("db.connectionURL"), R.TESTDATA.get("date.dbDate"), R.TESTDATA.get("employeeId.id"));
		boolean isTaskTitleUpdatedInDB = false;
		for (String taskTitle : taskTitleListFromDB) {
			System.out.println("Task Title updated in DB on editing from grid view: " + taskTitle);
			if (taskTitle.equals(R.TESTDATA.get("title.editedTaskTitle"))) {
				isTaskTitleUpdatedInDB = true;
				break;
			}
		}
		if (isTaskTitleUpdatedInDB == false) {
			fail("The task title of the timesheet is not properly updated in the DataBase.");
		}

		// Toggle to calender view
		timesheetLandingPage.refresh();
		timesheetLandingPage.navigateToTopOfThePage();
		timesheetLandingPage.toggleToCalender();

		TimeSheetDetailsCalenderBox timeSheetDetailsCalenderBox = timesheetLandingPage.getTimeSheetDetailsCalenderBox();

		// Select the required month and year in calender view
		int year = timeSheetDetailsCalenderBox.formatDateAndGetYear(R.TESTDATA.get("date.requiredDate"));

		String month = timeSheetDetailsCalenderBox.formatDateAndGetMonth(R.TESTDATA.get("date.requiredDate"));
		timeSheetDetailsCalenderBox.selectTheRequiredYearInCalenderView(year);

		timeSheetDetailsCalenderBox.selectTheRequiredMonthInCalenderView(month);
		Thread.sleep(3000);

		// Verify the tooltip of the task contains the task title
		String toolTipTextOfTheTask = timeSheetDetailsCalenderBox.fetchToolTipTextOfTheTask(
				R.TESTDATA.get("task.taskID"), R.TESTDATA.get("time.requiredTime"), R.TESTDATA.get("date.dbDate"));

		Assert.assertTrue(
				toolTipTextOfTheTask.equals(R.TESTDATA.get("task.taskID") + " - " + R.TESTDATA.get("time.requiredTime")
						+ " - " + R.TESTDATA.get("title.editedTaskTitle")),
				"The tooltip of the displayed does not contain the expected Task title. The text displayed is : "
						+ toolTipTextOfTheTask);

		// Verify the task details contains the task title
		EmployeeTaskDetailsMenuBox employeeTaskDetailsMenuBox = timeSheetDetailsCalenderBox.clickTheRequiredTask(
				R.TESTDATA.get("task.taskID"), R.TESTDATA.get("time.requiredTime"), R.TESTDATA.get("date.dbDate"));

		String taskDetailsDisplayed = employeeTaskDetailsMenuBox.getTaskDetailsDisplayed().getText();

		Assert.assertTrue(taskDetailsDisplayed.contains(R.TESTDATA.get("title.editedTaskTitle")),
				"The task title displayed in the calender view task details is not as expected.");

		// Verify the task title field in the edit timesheet pop up
		employeeTaskDetailsMenuBox.clickEditButton();

		Assert.assertTrue(employeeEditTimeSheetBox.getTaskTitleField().isVisible(),
				"Task title field is not available in the edit pop up from calender view.");
		employeeEditTimeSheetBox.editTaskTitle(R.TESTDATA.get("calender.editedTitle"));
		employeeEditTimeSheetBox.editTaskDescription(R.TESTDATA.get("calender.editedDescription"));
		employeeEditTimeSheetBox.clickUpdateButton();

		// Verify the edited title is updated in Database
		List<String> taskTitleListFromDBAfterEditingFromCalenderView = dataBaseQueries.fetchTaskTitleFromDatabase(
				R.DATABASE.get("db.connectionURL"), R.TESTDATA.get("date.dbDate"), R.TESTDATA.get("employeeId.id"));
		boolean isTaskTitleUpdatedInDBAfterEditing = false;
		for (String taskTitle : taskTitleListFromDBAfterEditingFromCalenderView) {
			if (taskTitle.equals(R.TESTDATA.get("calender.editedTitle"))) {
				isTaskTitleUpdatedInDBAfterEditing = true;
				System.out.println("Task Title updated in DB on editing from calender view: " + taskTitle);

				break;
			}
		}
		if (isTaskTitleUpdatedInDBAfterEditing == false) {
			fail("The task title of the timesheet edited from calender view is not properly updated in the DataBase.");
		}
	}

	// HPB-58 - [DevOps Integration] - For a DevOps task, always display Task Name
	// in the calendar (tooltip and popup)

	@Test(priority = 2)
	public void verifyTaskTitleFieldOfDevOpsTasksInAddAndEditPopUps() throws InterruptedException, ParseException {

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

		workItemsBox.enterTaskTitle(R.TESTDATA.get("devops.taskTitle"));

		// Open the task added and select the assignee and provide the estimates
		DevOpsTaskDetailsBox devOpsTaskDetailsBox = workItemsBox
				.selectTheRequiredTask(R.TESTDATA.get("devops.taskTitle"));

		String idofTheTaskAdded = devOpsTaskDetailsBox.getTaskIdField().getText();

		devOpsTaskDetailsBox.selectAssigneeForTheTask(R.TESTDATA.get("selectAssigneeForTheTask.assignee"));

		devOpsTaskDetailsBox.provideTheEstimates(R.TESTDATA.get("provideTheEstimates.originalEstimate"),
				R.TESTDATA.get("provideTheEstimates.remainingEstimate"), R.TESTDATA.get(""));

		devOpsTaskDetailsBox.clickSaveAndCloseButton();

		Thread.sleep(6000);

		devOpsLoginPage.quitDriver();

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

		Thread.sleep(3000);
		/*
		 * Assert.assertFalse(timesheetLandingPage.getPatTokenBox().getPatTokenField().
		 * isVisible(), "The user : " + R.TESTDATA.get("employeeLogin.militaryIdTwo") +
		 * " is required to enter the PAT Token. Either the existing token expired or there are no tasks assigned for the employee in DevOps."
		 * );
		 */

		devopsTaskDetailsBox.displayAllRows("All");

		boolean isTitleAvailableInHeader = devopsTaskDetailsBox
				.verifyTaskDetailsHeaderElement(R.TESTDATA.get("devopsHeader.tile"));

		Assert.assertTrue(isTitleAvailableInHeader,
				"Task Title column is not available in the devops task list table.");

		String taskTitleDisplayed = devopsTaskDetailsBox.verifyTaskTitle(idofTheTaskAdded);

		Assert.assertTrue(taskTitleDisplayed.equalsIgnoreCase(R.TESTDATA.get("devops.taskTitle")),
				"The task title displayed is not as expected.");

		// Open the task added in the previous steps from the task list displayed for
		// the employee
		DevOpsAddTimeSheetBox devOpsAddTimeSheetBox = devopsTaskDetailsBox
				.selectTheRequiredTaskForAddingTimeSheet(idofTheTaskAdded);

		Assert.assertTrue(
				devOpsAddTimeSheetBox.getTaskTitle().getAttribute(R.TESTDATA.get("getAttribute.attribute"))
						.equalsIgnoreCase(R.TESTDATA.get("getAttribute.value")),
				"The task title field is not read only in the add new timesheet through devops popup.");

		DatePickerForAddingTimesheet datePickerForAddingTimesheet = devOpsAddTimeSheetBox.clicktaskDateField();

		datePickerForAddingTimesheet.selectDate(R.TESTDATA.get("selectDate.requiredYearfordevops"),
				R.TESTDATA.get("selectDate.requiredMonthfordevops"), R.TESTDATA.get("selectDate.requiredDayfordevops"));

		devOpsAddTimeSheetBox.selectTime(R.TESTDATA.get("selectTime.requiredTimefordevops"));

		devOpsAddTimeSheetBox.enterTaskDescription(R.TESTDATA.get("devops.description"));

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
					&& details.contains(R.TESTDATA.get("devops.taskTitle"))) {
				isTimeSheetAdded = true;
				System.out.println("Timesheet has been added succesfully");
				break;
			}

		}
		if (isTimeSheetAdded == false) {
			fail("Timesheet added is not available in the task list of the employee.");
		}

		WebElement statusOfAddedTimeSheet = timesheetDetailsGridBox
				.verifyStatusOfAddedTimesheet(R.TESTDATA.get("devops.taskTitle"));

		Assert.assertTrue(statusOfAddedTimeSheet.getText().equals("Pending"),
				"The status of the newly added timesheet is not Pending. The status displayed is :"
						+ statusOfAddedTimeSheet.getText());

		EmployeeEditTimeSheetBox employeeEditTimeSheetBox = timesheetDetailsGridBox
				.clickEditButtonOfRequiredTask(idofTheTaskAdded);

		Assert.assertTrue(employeeEditTimeSheetBox.getDevopsButton().isVisible(),
				"DevOps button is not available for the task.");

		Assert.assertTrue(
				employeeEditTimeSheetBox.getTaskTitleField().getAttribute(R.TESTDATA.get("getAttribute.attribute"))
						.equalsIgnoreCase(R.TESTDATA.get("getAttribute.value")),
				"The task title field is editable in the edit timesheet pop up.");

		employeeEditTimeSheetBox.clickUpdateButton();

		timesheetLandingPage.refresh();

		timesheetLandingPage.navigateToTopOfThePage();

		timesheetLandingPage.toggleToCalender();

		TimeSheetDetailsCalenderBox timeSheetDetailsCalenderBox = timesheetLandingPage.getTimeSheetDetailsCalenderBox();

		Thread.sleep(3000);

		int year = timeSheetDetailsCalenderBox.formatDateAndGetYear(R.TESTDATA.get("selectDate.devOpsDate"));

		String month = timeSheetDetailsCalenderBox.formatDateAndGetMonth(R.TESTDATA.get("selectDate.devOpsDate"));

		timeSheetDetailsCalenderBox.selectTheRequiredYearInCalenderView(year);
		timeSheetDetailsCalenderBox.selectTheRequiredMonthInCalenderView(month);

		Thread.sleep(3000);

		// Verify the tooltip of the task contains the task title
		String toolTipTextOfTheTask = timeSheetDetailsCalenderBox.fetchToolTipTextOfTheTask(idofTheTaskAdded,
				R.TESTDATA.get("selectTime.requiredTimefordevops"), R.TESTDATA.get("selectDate.dbDate"));

		System.out.println(toolTipTextOfTheTask);
		Assert.assertTrue(
				toolTipTextOfTheTask
						.equals(idofTheTaskAdded + " - " + R.TESTDATA.get("selectTime.requiredTimefordevops") + " - "
								+ R.TESTDATA.get("devops.taskTitle")),
				"The tooltip of the displayed does not contain the expected Task title. The text displayed is : "
						+ toolTipTextOfTheTask);

		// Verify the task details contains the task title
		EmployeeTaskDetailsMenuBox employeeTaskDetailsMenuBox = timeSheetDetailsCalenderBox.clickTheRequiredTask(
				idofTheTaskAdded, R.TESTDATA.get("selectTime.requiredTimefordevops"),
				R.TESTDATA.get("selectDate.dbDate"));

		String taskDetailsDisplayed = employeeTaskDetailsMenuBox.getTaskDetailsDisplayed().getText();

		System.out.println(taskDetailsDisplayed);
		Assert.assertTrue(taskDetailsDisplayed.contains(R.TESTDATA.get("devops.taskTitle")),
				"The task title displayed in the calender view task details is not as expected.");

		// Verify the task title field is not editable in the edit timesheet pop up from
		// calender view.
		employeeTaskDetailsMenuBox.clickEditButton();

		Assert.assertTrue(
				employeeEditTimeSheetBox.getTaskTitleField().getAttribute(R.TESTDATA.get("getAttribute.attribute"))
						.equalsIgnoreCase(R.TESTDATA.get("getAttribute.value")),
				"The task title field is editable in the edit timesheet pop up.");

		Thread.sleep(3000);
	}

	// HPB-59 - [DevOps Integration] - The DevOps tasks should have a visual
	// indication on the calendar (Icon)
	@Test(priority = 3)

	public void verifyDevOpsIconForTasksInCalenderView() throws ParseException, InterruptedException {

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

		workItemsBox.enterTaskTitle(R.TESTDATA.get("verifyDevOpsIcon.taskTitle"));

		// Open the task added and select the assignee and provide the estimates
		DevOpsTaskDetailsBox devOpsTaskDetailsBox = workItemsBox
				.selectTheRequiredTask(R.TESTDATA.get("verifyDevOpsIcon.taskTitle"));

		String idofTheTaskAdded = devOpsTaskDetailsBox.getTaskIdField().getText();

		devOpsTaskDetailsBox.selectAssigneeForTheTask(R.TESTDATA.get("selectAssigneeForTheTask.assignee"));

		devOpsTaskDetailsBox.provideTheEstimates(R.TESTDATA.get("provideTheEstimates.originalEstimate"),
				R.TESTDATA.get("provideTheEstimates.remainingEstimate"), R.TESTDATA.get(""));

		devOpsTaskDetailsBox.clickSaveAndCloseButton();

		Thread.sleep(3000);

		devOpsLoginPage.quitDriver();

		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryIdTwo"),
				R.TESTDATA.get("employeeLogin.password"));

		Assert.assertTrue(timesheetLandingPage.isPageOpened(), "Login is not successfull for the employee ' : "
				+ R.TESTDATA.get("employeeLogin.militaryId") + "'. The required landing page is not opened on login.");

		timesheetLandingPage.clickAddNewTimeSheetButton();
		DevopsTaskDetailsBox devopsTaskDetailsBox = timesheetLandingPage.getAddNewTimeSheetBox().clickDevOpsIcon();

		/*
		 * Assert.assertFalse(timesheetLandingPage.getPatTokenBox().getPatTokenField().
		 * isVisible(), "The user : " + R.TESTDATA.get("employeeLogin.militaryIdTwo") +
		 * " is required to enter the PAT Token. Either the existing token expired or there are no tasks assigned for the employee in DevOps."
		 * );
		 */

		devopsTaskDetailsBox.displayAllRows("All");

		// Open the task added in the previous steps from the task list displayed for
		// the employee
		DevOpsAddTimeSheetBox devOpsAddTimeSheetBox = devopsTaskDetailsBox
				.selectTheRequiredTaskForAddingTimeSheet(idofTheTaskAdded);

		DatePickerForAddingTimesheet datePickerForAddingTimesheet = devOpsAddTimeSheetBox.clicktaskDateField();

		datePickerForAddingTimesheet.selectDate(R.TESTDATA.get("verifyDevOpsIcon.year"),
				R.TESTDATA.get("verifyDevOpsIcon.month"), R.TESTDATA.get("verifyDevOpsIcon.day"));

		devOpsAddTimeSheetBox.selectTime(R.TESTDATA.get("verifyDevOpsIcon.time"));

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
		timesheetLandingPage.navigateToTopOfThePage();

		timesheetLandingPage.toggleToCalender();

		TimeSheetDetailsCalenderBox timeSheetDetailsCalenderBox = timesheetLandingPage.getTimeSheetDetailsCalenderBox();

		Thread.sleep(3000);

		int year = timeSheetDetailsCalenderBox.formatDateAndGetYear(R.TESTDATA.get("verifyDevOpsIcon.date"));

		String month = timeSheetDetailsCalenderBox.formatDateAndGetMonth(R.TESTDATA.get("verifyDevOpsIcon.date"));

		timeSheetDetailsCalenderBox.selectTheRequiredYearInCalenderView(year);
		timeSheetDetailsCalenderBox.selectTheRequiredMonthInCalenderView(month);

		Thread.sleep(3000);

		timesheetLandingPage.applyFilterWithRequiredStatus("Pending");
		Thread.sleep(3000);

		WebElement devopsIndicationIconForPendingTask = timeSheetDetailsCalenderBox.verifyDevOpsIconInCalenderView(
				idofTheTaskAdded, R.TESTDATA.get("verifyDevOpsIcon.time"), R.TESTDATA.get("verifyDevOpsIcon.dbDate"));

		Assert.assertTrue(devopsIndicationIconForPendingTask.isDisplayed(),
				"DevOps Indication icon is not displayed for the task in calender view.");

		EmployeeTaskDetailsMenuBox employeeTaskDetailsMenuBox = timeSheetDetailsCalenderBox.clickTheRequiredTask(
				idofTheTaskAdded, R.TESTDATA.get("verifyDevOpsIcon.time"), R.TESTDATA.get("verifyDevOpsIcon.dbDate"));

		employeeTaskDetailsMenuBox.clickSubmitButton();

		Assert.assertTrue(timesheetLandingPage.getSuccessAlertOnSubmittingTimesheetInCalenderMode().isVisible(),
				"Success Alert is not displayed on submitting the task from calender view.");

		timesheetLandingPage.applyFilterWithRequiredStatus("Submitted");
		Thread.sleep(3000);

		WebElement devopsIndicationIconForSubmittedTaskOfEmployee = timeSheetDetailsCalenderBox
				.verifyDevOpsIconInCalenderView(idofTheTaskAdded, R.TESTDATA.get("verifyDevOpsIcon.time"),
						R.TESTDATA.get("verifyDevOpsIcon.dbDate"));

		Assert.assertTrue(devopsIndicationIconForSubmittedTaskOfEmployee.isDisplayed(),
				"DevOps Indication icon is not displayed for the task in calender view.");

		TimesheetLandingPage timesheetLandingpage = managerLogin(R.TESTDATA.get("managerLogin.militaryIdTwo"),
				R.TESTDATA.get("managerLogin.password"));
		HeaderBox header = timesheetLandingpage.getHeaderBox();

		Assert.assertTrue(header.getManagerButton().isVisible(), " The logged in user '"
				+ R.TESTDATA.get("managerLogin.militaryIdTwo") + "' does not have a manager role");
		ManagerLandingPage managerLandingPage = header.clickManagerButton();
		ManagerCalenderBox managerCalenderBox = managerLandingPage.getManagerCalenderBox();

		timesheetLandingpage.toggleToCalender();

		// Verify the manager has the dropdown to select the employee
		Assert.assertTrue(managerCalenderBox.getEmployeeListDropDown().isPresent(),
				"Employee Dropdown is not available for the manager user");

		// Select any employee

		boolean isEmployeeSelected = managerCalenderBox.selectEmployee(R.TESTDATA.get("employee.employeeName"));
		Assert.assertTrue(isEmployeeSelected,
				"The employee " + R.TESTDATA.get("employee.employeeName") + " is not available for the manager user");

		managerCalenderBox.selectTheRequiredYearInCalenderView(year);
		managerCalenderBox.selectTheRequiredMonthInCalenderView(month);

		Thread.sleep(3000);

		WebElement devopsIndicationIconForManager = managerCalenderBox.verifyDevOpsIconInManagerCalenderView(
				idofTheTaskAdded, R.TESTDATA.get("verifyDevOpsIcon.time"), R.TESTDATA.get("verifyDevOpsIcon.dbDate"));

		Assert.assertTrue(devopsIndicationIconForManager.isDisplayed(),
				"DevOps Indication icon is not displayed for the task in calender view for Manager.");

		ManagerTaskDetailsMenuBox managerTaskDetailsMenuBox = managerCalenderBox.clickTheRequiredTask(idofTheTaskAdded,
				R.TESTDATA.get("verifyDevOpsIcon.time"));

		managerTaskDetailsMenuBox.clickApproveTaskButton();

		Assert.assertTrue(managerLandingPage.getSuccessAlertOnApprovingTimesheet().isVisible(),
				"Success Alert is not displayed on approving the timesheet.");

		Assert.assertTrue(managerLandingPage.getSuccessAlertOnApprovingTimesheet().getText().equals("Task Approved"),
				"Success Message on approving the timesheet is not as expected.");

		employeeLogin(R.TESTDATA.get("employeeLogin.militaryIdTwo"), R.TESTDATA.get("managerLogin.password"));

		Assert.assertTrue(timesheetLandingPage.isPageOpened(), "Login is not successfull for the employee ' : "
				+ R.TESTDATA.get("employeeLogin.militaryId") + "'. The required landing page is not opened on login.");

		timesheetLandingPage.toggleToCalender();

		Thread.sleep(3000);

		timeSheetDetailsCalenderBox.selectTheRequiredYearInCalenderView(year);
		timeSheetDetailsCalenderBox.selectTheRequiredMonthInCalenderView(month);

		Thread.sleep(3000);

		timesheetLandingPage.applyFilterWithRequiredStatus("Approved");

		Thread.sleep(3000);

		WebElement devOpsIconInCalenderViewForApprovedTask = timeSheetDetailsCalenderBox
				.verifyDevOpsIconInCalenderViewForApprovedTasks(idofTheTaskAdded,
						R.TESTDATA.get("verifyDevOpsIcon.time"), R.TESTDATA.get("verifyDevOpsIcon.dbDate"));

		Assert.assertTrue(devOpsIconInCalenderViewForApprovedTask.isDisplayed(),
				"DevOps Icon is not displayed for the approved task.");

		Thread.sleep(4000);

	}

}