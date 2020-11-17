package pits.automation.web.S2006;

import static org.testng.Assert.fail;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.qaprosoft.carina.core.foundation.utils.R;

import pits.automation.web.devops.pages.DevOpsLandingPage;
import pits.automation.web.devops.pages.DevOpsLoginPage;
import pits.automation.web.devops.pages.DevOpsTaskDetailsBox;
import pits.automation.web.devops.pages.ProjectCardBox;
import pits.automation.web.devops.pages.TaskBoardLandingPage;
import pits.automation.web.devops.pages.WorkItemsBox;
import pits.automation.web.hrms.employee.devops.pages.DevOpsAddTimeSheetBox;
import pits.automation.web.hrms.employee.devops.pages.DevopsTaskDetailsBox;
import pits.automation.web.hrms.employee.devops.pages.ProjectSelectorBox;
import pits.automation.web.hrms.employee.pages.DatePickerForAddingTimesheet;
import pits.automation.web.hrms.employee.pages.EmployeeEditTimeSheetBox;
import pits.automation.web.hrms.employee.pages.EmployeeTaskDetailsMenuBox;
import pits.automation.web.hrms.employee.pages.TaskDetailsExpandedGridBox;
import pits.automation.web.hrms.employee.pages.TimeSheetDetailsCalenderBox;
import pits.automation.web.hrms.employee.pages.TimeSheetDetailsGridBox;
import pits.automation.web.hrms.manager.pages.EmployeeTaskListBox;
import pits.automation.web.hrms.manager.pages.ManagerLandingPage;
import pits.automation.web.hrms.manager.pages.RejectTaskBox;
import pits.automation.web.hrms.manager.pages.TaskApprovalListBox;
import pits.automation.web.hrms.manager.pages.TaskRejectConfirmationPopUp;
import pits.automation.web.hrms.pages.HeaderBox;
import pits.automation.web.hrms.pages.TimesheetLandingPage;
import pits.automation.web.login.BaseTest;

public class S2006_01 extends BaseTest {

	// HPB-62 - [DevOps Integration] - The DevOps tasks should have a visual
	// indication in the grid view (Icon)

	@Test(priority = 1)
	public void verifyDevopsIconInGridView() throws InterruptedException {

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
		Thread.sleep(3000);
		WorkItemsBox workItemsBox = taskBoardLandingPage.getWorkItemsBox();

		// Add a new task
		workItemsBox.clickAddWorkItemButton();
		workItemsBox.selectWorkItem(R.TESTDATA.get("selectWorkItem.workItem"));

		workItemsBox.enterTaskTitle(R.TESTDATA.get("verifyDevopsIconInGridView.taskTitle"));

		// Open the task added and select the assignee and provide the estimates
		DevOpsTaskDetailsBox devOpsTaskDetailsBox = workItemsBox
				.selectTheRequiredTask(R.TESTDATA.get("verifyDevopsIconInGridView.taskTitle"));

		String idofTheTaskAdded = devOpsTaskDetailsBox.getTaskIdField().getText();

		devOpsTaskDetailsBox.selectAssigneeForTheTask(R.TESTDATA.get("selectAssigneeForTheTask.assignee"));

		Thread.sleep(3000);

		devOpsTaskDetailsBox.provideTheEstimates(R.TESTDATA.get("verifyDevopsIconInGridView.originalEstimate"),
				R.TESTDATA.get("verifyDevopsIconInGridView.remainingEstimate"),
				R.TESTDATA.get("verifyDevopsIconInGridView.completedTime"));

		devOpsTaskDetailsBox.clickSaveAndCloseButton();

		Thread.sleep(3000);

		devOpsLandingPage.quitDriver();

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

		// Open the task added in the previous steps from the task list displayed for
		// the employee
		DevOpsAddTimeSheetBox devOpsAddTimeSheetBox = devopsTaskDetailsBox
				.selectTheRequiredTaskForAddingTimeSheet(idofTheTaskAdded);

		DatePickerForAddingTimesheet datePickerForAddingTimesheet = devOpsAddTimeSheetBox.clicktaskDateField();

		datePickerForAddingTimesheet.selectDate(R.TESTDATA.get("verifyDevopsIconInGridView.year"),
				R.TESTDATA.get("verifyDevopsIconInGridView.month"), R.TESTDATA.get("verifyDevopsIconInGridView.day"));

		devOpsAddTimeSheetBox.selectTime(R.TESTDATA.get("verifyDevopsIconInGridView.time"));

		devOpsAddTimeSheetBox.enterTaskDescription(R.TESTDATA.get("verifyDevopsIconInGridView.description"));

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

		Thread.sleep(3000);

		// Select option "All" from pagination
		timesheetDetailsGridBox.displayAllRows();

		// Expand the task details of the date for which the timesheet was added
		timesheetDetailsGridBox.expandTimesheetOfRequiredDate(R.TESTDATA.get("verifyDevopsIconInGridView.date"));

		// Selecting option "All" from pagination for task details
		timesheetDetailsGridBox.displayAllRows();

		// Verfying the timesheet added is present and status is pending
		List<String> detailsFetched = timesheetDetailsGridBox.fetchTaskDetails();
		boolean isTimeSheetAdded = false;
		for (String details : detailsFetched) {
			if (details.contains(idofTheTaskAdded)
					&& details.contains(R.TESTDATA.get("verifyDevopsIconInGridView.time"))
					&& details.contains(R.TESTDATA.get("verifyDevopsIconInGridView.taskTitle"))) {
				isTimeSheetAdded = true;
				System.out.println("Timesheet has been added succesfully");
				break;
			}

		}
		if (isTimeSheetAdded == false) {
			fail("Timesheet added is not available in the task list of the employee.");
		}

		TaskDetailsExpandedGridBox taskDetailsExpandedGridBox = timesheetLandingPage.getTaskDetailsExpandedGridBox();
		WebElement devopsIndicationIcon = taskDetailsExpandedGridBox.fetchDevOpsIconOfTheTask(
				R.TESTDATA.get("verifyDevopsIconInGridView.taskTitle"), idofTheTaskAdded,
				R.TESTDATA.get("verifyDevopsIconInGridView.time"));

		Assert.assertTrue(devopsIndicationIcon.isDisplayed(),
				"DevOps indication icon is not available for the task added in grid view.");

		System.out.println(devopsIndicationIcon.getAttribute("style"));

		Assert.assertFalse(devopsIndicationIcon.getAttribute("style").contains("display: none"),
				"Devops Icon is not available for the task added.");

		taskDetailsExpandedGridBox.clickSubmitButton(R.TESTDATA.get("verifyDevopsIconInGridView.taskTitle"),
				idofTheTaskAdded, R.TESTDATA.get("verifyDevopsIconInGridView.time"));

		Assert.assertTrue(timesheetLandingPage.getSuccessAlertOnSubmittingTimesheet().isVisible(),
				"Success Alert  is not displayed on submitting the timesheet.");

		Thread.sleep(3000);

		timesheetLandingPage.refresh();

		timesheetLandingPage.navigateToTopOfThePage();

		timesheetLandingPage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.submitted"));

		timesheetDetailsGridBox.displayAllRows();
		timesheetLandingPage.navigateToTopOfThePage();

		// Expand the task details of the date for which the timesheet was added
		timesheetDetailsGridBox.expandTimesheetOfRequiredDate(R.TESTDATA.get("verifyDevopsIconInGridView.date"));

		// Selecting option "All" from pagination for task details
		timesheetDetailsGridBox.displayAllRows();

		WebElement devopsIndicationIconOfSubmittedTask = taskDetailsExpandedGridBox.fetchDevOpsIconOfTheTask(
				R.TESTDATA.get("verifyDevopsIconInGridView.taskTitle"), idofTheTaskAdded,
				R.TESTDATA.get("verifyDevopsIconInGridView.time"));

		Assert.assertTrue(devopsIndicationIconOfSubmittedTask.isDisplayed(),
				"DevOps indication icon is not available for the task added in grid view.");

		Assert.assertFalse(devopsIndicationIconOfSubmittedTask.getAttribute("style").contains("display: none"),
				"Devops Icon is not available for the task added.");

		TimesheetLandingPage timesheetLandingpage = managerLogin(R.TESTDATA.get("managerLogin.militaryIdTwo"),
				R.TESTDATA.get("managerLogin.password"));
		HeaderBox header = timesheetLandingpage.getHeaderBox();

		Assert.assertTrue(header.getManagerButton().isVisible(), " The logged in user '"
				+ R.TESTDATA.get("managerLogin.militaryIdTwo") + "' does not have a manager role");
		ManagerLandingPage managerLandingPage = header.clickManagerButton();
		Thread.sleep(3000);

		TaskApprovalListBox taskApprovalListBox = managerLandingPage.getTaskApprovalListBox();
		EmployeeTaskListBox employeeTaskListBox = managerLandingPage.getEmployeeTaskList();

		boolean isEmployeeAvailable = taskApprovalListBox
				.expandTheTaskDetailsOfParticularEmployee(R.TESTDATA.get("employee.employeeName"));

		Assert.assertTrue(isEmployeeAvailable, "The employee :" + R.TESTDATA.get("employee.employeeName")
				+ " is not available in the list of the manager user.");

		WebElement devopsIconInManagerList = employeeTaskListBox.verifyDevOpsIconInManagerTaskList(
				R.TESTDATA.get("verifyDevopsIconInGridView.date"),
				R.TESTDATA.get("verifyDevopsIconInGridView.taskTitle"), idofTheTaskAdded,
				R.TESTDATA.get("verifyDevopsIconInGridView.time"));
		Assert.assertTrue(devopsIconInManagerList.isDisplayed(),
				"DevOps indication icon is not available for the task added in grid view.");

		Assert.assertFalse(devopsIconInManagerList.getAttribute("style").contains("display: none"),
				"Devops Icon is not available for the task in the manager task list.");

		TaskRejectConfirmationPopUp taskRejectConfirmationPopUp = employeeTaskListBox
				.clickRejectButtonOfTheRequiredTask(R.TESTDATA.get("verifyDevopsIconInGridView.date"),
						R.TESTDATA.get("verifyDevopsIconInGridView.taskTitle"), idofTheTaskAdded,
						R.TESTDATA.get("verifyDevopsIconInGridView.time"));

		RejectTaskBox rejectTaskBox = taskRejectConfirmationPopUp.clickYesButton();

		// Provide the reason to reject
		rejectTaskBox.provideReasontoRejectTheTask(R.TESTDATA.get("verifyDevopsIconInGridView.rejectReason"));

		rejectTaskBox.clickSubmitButton();

		// Verify the success alert displayed on manager rejecting the task
		Assert.assertTrue(managerLandingPage.getSuccessAlertOnRejectingTimesheet().isPresent(),
				"Expected Success Alert is not displayed on manager rejecting the timesheet.");

		Thread.sleep(3000);

		TimesheetLandingPage timesheetlandingpage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryIdTwo"),
				R.TESTDATA.get("employeeLogin.password"));

		timesheetlandingpage.applyFilterWithRequiredStatus(R.TESTDATA.get("selectFilter.rejected"));

		timesheetDetailsGridBox.displayAllRows();
		timesheetLandingPage.navigateToTopOfThePage();

		// Expand the task details of the date for which the timesheet was added
		timesheetDetailsGridBox.expandTimesheetOfRequiredDate(R.TESTDATA.get("verifyDevopsIconInGridView.date"));

		// Selecting option "All" from pagination for task details
		timesheetDetailsGridBox.displayAllRows();

		WebElement devopsIndicationIconRejectedTask = taskDetailsExpandedGridBox.fetchDevOpsIconOfTheTask(
				R.TESTDATA.get("verifyDevopsIconInGridView.taskTitle"), idofTheTaskAdded,
				R.TESTDATA.get("verifyDevopsIconInGridView.time"));

		Assert.assertTrue(devopsIndicationIconRejectedTask.isDisplayed(),
				"DevOps indication icon is not available for the task added in grid view.");

		Assert.assertFalse(devopsIndicationIconRejectedTask.getAttribute("style").contains("display: none"),
				"Devops Icon is not available for the task added.");

	}

	// HPB-67 - [Devops Integration] Add ALL option in the project filter dropdown

	@Test(priority = 2)
	public void verifyAllOptionInprojectFilter() throws InterruptedException, ParseException {

		DevOpsLoginPage devOpsLoginPage = new DevOpsLoginPage(getDriver());
		DevOpsLandingPage devOpsLandingPage = devOpsLoginPage.devopsEmployeeLogin();

		Assert.assertTrue(devOpsLandingPage.getProjectCard().isVisible(),
				"Devops Login is not successfull for the user : " + R.TESTDATA.get("devopsEmployeeLogin.userName"));

		ProjectCardBox projectCardBox = devOpsLandingPage.getProjectCardBox();

		List<String> projectsAvailableForTheUserInDevops = projectCardBox.fetchTheProjectList();

		// Select the project and navigate to taskboard
		boolean isProjectSelected = projectCardBox.selectTheRequiredProject(R.TESTDATA.get("selectProject.project"));
		Assert.assertTrue(isProjectSelected, "The required project : " + R.TESTDATA.get("selectProject.project")
				+ " is not available for the logged in user.");
		TaskBoardLandingPage taskBoardLandingPage = devOpsLandingPage.navigateToTaskBoard();
		WorkItemsBox workItemsBox = taskBoardLandingPage.getWorkItemsBox();

		// Add a new task
		workItemsBox.clickAddWorkItemButton();
		workItemsBox.selectWorkItem(R.TESTDATA.get("selectWorkItem.workItem"));

		workItemsBox.enterTaskTitle(R.TESTDATA.get("verifyAllOptionInprojectFilter.title"));

		// Open the task added and select the assignee and provide the estimates
		DevOpsTaskDetailsBox devOpsTaskDetailsBox = workItemsBox
				.selectTheRequiredTask(R.TESTDATA.get("verifyAllOptionInprojectFilter.title"));

		String idofTheTaskAdded = devOpsTaskDetailsBox.getTaskIdField().getText();
		System.out.println(idofTheTaskAdded);

		devOpsTaskDetailsBox.selectAssigneeForTheTask(R.TESTDATA.get("selectAssigneeForTheTask.assignee"));

		devOpsTaskDetailsBox.provideTheEstimates(R.TESTDATA.get("provideTheEstimates.originalEstimate"),
				R.TESTDATA.get("provideTheEstimates.remainingEstimate"),
				R.TESTDATA.get("verifyRabbitMQ.completedTime"));

		devOpsTaskDetailsBox.clickSaveAndCloseButton();

		Thread.sleep(3000);

		devOpsLandingPage.quitDriver();
		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryIdTwo"),
				R.TESTDATA.get("employeeLogin.password"));
		TimeSheetDetailsGridBox timesheetDetailsGridBox = timesheetLandingPage.getTimeSheetDetailsGridBox();

		Assert.assertTrue(timesheetLandingPage.isPageOpened(), "Login is not successfull for the employee ' : "
				+ R.TESTDATA.get("employeeLogin.militaryId") + "'. The required landing page is not opened on login.");

		// Verify PAT token is not required and open the devops task list from the add
		// timesheet popup

		timesheetLandingPage.clickAddNewTimeSheetButton();
		DevopsTaskDetailsBox devopsTaskDetailsBox = timesheetLandingPage.getAddNewTimeSheetBox().clickDevOpsIcon();
		ProjectSelectorBox projectSelectorBox = devopsTaskDetailsBox.getProjectselectorBox();

		Assert.assertTrue(projectSelectorBox.getProjectDropDown().isVisible(),
				"Project Name filter is not available in the devops task list in add new timeshet pop up.");

		Assert.assertTrue(projectSelectorBox.getProjectSelected().getText().equalsIgnoreCase("All"),
				"The filter option 'All' is not selected by default for the project filter in the add timesheet popup.");

		projectSelectorBox.clickSelectProjectDropDown();
		Thread.sleep(3000);
		List<String> projectsDisplayedInHRMSFilterDropDown = projectSelectorBox.fetchTheProjectList();
		List<String> projectsDisplayedInHRMS = new ArrayList<>();

		for (String project : projectsDisplayedInHRMSFilterDropDown) {
			if (!project.equalsIgnoreCase("All")) {
				projectsDisplayedInHRMS.add(project);
			}
		}
		Assert.assertEquals(projectsAvailableForTheUserInDevops, projectsDisplayedInHRMS,
				"The projects available for the user in Devops and The Projects displayed in HRMS do not match.");
		devopsTaskDetailsBox.displayAllRows("All");
		List<String> projectNamesDisplayedWithAll = devopsTaskDetailsBox.fetchProjectNames();

		boolean isAllFilterApplied = devopsTaskDetailsBox.verifyTheProjectNamesDisplayed(projectsDisplayedInHRMS,
				projectNamesDisplayedWithAll);

		Assert.assertTrue(isAllFilterApplied,
				"The tasks of all the projects are not displayed with 'All' filter option in add new timesheet popup.");

		DevOpsAddTimeSheetBox devOpsAddTimeSheetBox = devopsTaskDetailsBox
				.selectTheRequiredTaskForAddingTimeSheet(idofTheTaskAdded);

		DatePickerForAddingTimesheet datePickerForAddingTimesheet = devOpsAddTimeSheetBox.clicktaskDateField();

		datePickerForAddingTimesheet.selectDate(R.TESTDATA.get("verifyAllOptionInprojectFilter.year"),
				R.TESTDATA.get("verifyAllOptionInprojectFilter.month"),
				R.TESTDATA.get("verifyAllOptionInprojectFilter.day"));

		devOpsAddTimeSheetBox.selectTimeForDevOps(R.TESTDATA.get("verifyAllOptionInprojectFilter.time"));

		devOpsAddTimeSheetBox.enterTaskDescription(R.TESTDATA.get("verifyAllOptionInprojectFilter.description"));

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

		Thread.sleep(3000);

		// Select option "All" from pagination
		timesheetDetailsGridBox.displayAllRows();

		// Expand the task details of the date for which the timesheet was added
		timesheetDetailsGridBox.expandTimesheetOfRequiredDate(R.TESTDATA.get("verifyAllOptionInprojectFilter.date"));

		// Selecting option "All" from pagination for task details
		timesheetDetailsGridBox.displayAllRows();

		// Verfying the timesheet added is present and status is pending
		List<String> detailsFetched = timesheetDetailsGridBox.fetchTaskDetails();
		boolean isTimeSheetAdded = false;
		for (String details : detailsFetched) {
			if (details.contains(idofTheTaskAdded)
					&& details.contains(R.TESTDATA.get("verifyAllOptionInprojectFilter.time"))
					&& details.contains(R.TESTDATA.get("verifyAllOptionInprojectFilter.title"))) {
				isTimeSheetAdded = true;
				System.out.println("Timesheet has been added succesfully");
				break;
			}

		}
		if (isTimeSheetAdded == false) {
			fail("Timesheet added is not available in the task list of the employee.");

		}

		EmployeeEditTimeSheetBox employeeEditTimeSheetBox = timesheetDetailsGridBox
				.clickEditButtonOfRequiredTask(idofTheTaskAdded);

		employeeEditTimeSheetBox.clickDevOpsButton();

		Assert.assertTrue(projectSelectorBox.getProjectDropDown().isVisible(),
				"Project Name filter is not available in the devops task list in edit timeshet pop up.");

		Assert.assertTrue(projectSelectorBox.getProjectSelected().getText().equalsIgnoreCase("All"),
				"The filter option 'All' is not selected by default for the project filter in the edit timesheet popup.");

		projectSelectorBox.clickSelectProjectDropDown();
		Thread.sleep(3000);
		List<String> projectsDisplayedInFilterInGridEdit = projectSelectorBox.fetchTheProjectList();

		List<String> projectsDisplayedInGridEdit = new ArrayList<>();
		for (String projectInEdit : projectsDisplayedInFilterInGridEdit) {
			if (!projectInEdit.equalsIgnoreCase("All")) {
				projectsDisplayedInGridEdit.add(projectInEdit);
			}
		}

		Assert.assertEquals(projectsAvailableForTheUserInDevops, projectsDisplayedInGridEdit,
				"The projects available for the user in Devops and The Projects displayed in HRMS do not match.");

		projectSelectorBox.selectTheRequiredProject("All");
		Thread.sleep(2000);
		devopsTaskDetailsBox.displayAllRows("All");
		List<String> projectNamesDisplayedWithAllInGridEdit = devopsTaskDetailsBox.fetchProjectNamesOnGridEdit();

		boolean isAllFilterAppliedInGridEdit = devopsTaskDetailsBox
				.verifyTheProjectNamesDisplayed(projectsDisplayedInGridEdit, projectNamesDisplayedWithAllInGridEdit);

		Assert.assertTrue(isAllFilterAppliedInGridEdit,
				"The tasks of all the projects are not displayed with 'All' filter option in edit timesheet popup.");

		timesheetLandingPage.refresh();
		timesheetLandingPage.navigateToTopOfThePage();
		timesheetLandingPage.toggleToCalender();
		Thread.sleep(3000);

		TimeSheetDetailsCalenderBox timeSheetDetailsCalenderBox = timesheetLandingPage.getTimeSheetDetailsCalenderBox();
		int year = timeSheetDetailsCalenderBox
				.formatDateAndGetYear(R.TESTDATA.get("verifyAllOptionInprojectFilter.date"));

		String month = timeSheetDetailsCalenderBox
				.formatDateAndGetMonth(R.TESTDATA.get("verifyAllOptionInprojectFilter.date"));
		timeSheetDetailsCalenderBox.selectTheRequiredYearInCalenderView(year);

		timeSheetDetailsCalenderBox.selectTheRequiredMonthInCalenderView(month);
		Thread.sleep(3000);
		EmployeeTaskDetailsMenuBox employeeTaskDetailsMenuBox = timeSheetDetailsCalenderBox.clickTheRequiredTask(
				idofTheTaskAdded, R.TESTDATA.get("verifyAllOptionInprojectFilter.time"),
				R.TESTDATA.get("verifyAllOptionInprojectFilter.Dbdate"));
		EmployeeEditTimeSheetBox employeeEditTimeSheetbox = employeeTaskDetailsMenuBox.clickEditButton();
		employeeEditTimeSheetbox.clickDevOpsButton();

		Assert.assertTrue(projectSelectorBox.getProjectDropDown().isVisible(),
				"Project Name filter is not available in the devops task list in edit timeshet pop up.");

		Assert.assertTrue(projectSelectorBox.getProjectSelected().getText().equalsIgnoreCase("All"),
				"The filter option 'All' is not selected by default for the project filter in the edit timesheet popup.");

		projectSelectorBox.clickSelectProjectDropDown();
		Thread.sleep(3000);
		List<String> projectsDisplayedInFilterInCalenderEdit = projectSelectorBox.fetchTheProjectList();

		List<String> projectsDisplayedInCalenderEdit = new ArrayList<>();
		for (String projectInEdit : projectsDisplayedInFilterInCalenderEdit) {
			if (!projectInEdit.equalsIgnoreCase("All")) {
				projectsDisplayedInCalenderEdit.add(projectInEdit);
			}
		}

		Assert.assertEquals(projectsAvailableForTheUserInDevops, projectsDisplayedInCalenderEdit,
				"The projects available for the user in Devops and The Projects displayed in HRMS do not match in edit timesheet pop up from calender view.");

		projectSelectorBox.selectTheRequiredProject("All");
		Thread.sleep(2000);
		devopsTaskDetailsBox.displayAllRows("All");
		List<String> projectNamesDisplayedWithAllInCalenderEdit = devopsTaskDetailsBox
				.fetchProjectNamesOnCalenderEdit();

		boolean isAllFilterAppliedCalenderEdit = devopsTaskDetailsBox.verifyTheProjectNamesDisplayed(
				projectsDisplayedInCalenderEdit, projectNamesDisplayedWithAllInCalenderEdit);

		Assert.assertTrue(isAllFilterAppliedCalenderEdit,
				"The tasks of all the projects are not displayed with 'All' filter option in edit timesheet popup in calender view.");

	}

	// HPB-66 [RabbitMQ Imp] Merge the MQ POC and test the full Approve Timesheet
	// Cycle

	@Test(priority = 3)
	public void verifyRabbitMQ() throws InterruptedException {

		// Login to Devops

		DevOpsLoginPage devOpsLoginPage = new DevOpsLoginPage(getDriver());
		DevOpsLandingPage devOpsLandingPage = devOpsLoginPage.devopsEmployeeLogin();

		Assert.assertTrue(devOpsLandingPage.getProjectCard().isVisible(),
				"Devops Login is not successfull for the user : " + R.TESTDATA.get("devopsEmployeeLogin.userName"));

		ProjectCardBox projectCardBox = devOpsLandingPage.getProjectCardBox();
		boolean isProjectSelected;
		// Select the project and navigate to taskboard
		isProjectSelected = projectCardBox.selectTheRequiredProject(R.TESTDATA.get("selectProject.project"));
		Assert.assertTrue(isProjectSelected, "The required project : " + R.TESTDATA.get("selectProject.project")
				+ " is not available for the logged in user.");
		TaskBoardLandingPage taskBoardLandingPage = devOpsLandingPage.navigateToTaskBoard();
		Thread.sleep(10000);
		WorkItemsBox workItemsBox = taskBoardLandingPage.getWorkItemsBox();

		// Add a new task
		workItemsBox.clickAddWorkItemButton();
		workItemsBox.selectWorkItem(R.TESTDATA.get("selectWorkItem.workItem"));

		workItemsBox.enterTaskTitle(R.TESTDATA.get("verifyRabbitMQ.tile"));

		// Open the task added and select the assignee and provide the estimates
		DevOpsTaskDetailsBox devOpsTaskDetailsBox = workItemsBox
				.selectTheRequiredTask(R.TESTDATA.get("verifyRabbitMQ.tile"));

		String idofTheTaskAdded = devOpsTaskDetailsBox.getTaskIdField().getText();

		devOpsTaskDetailsBox.selectAssigneeForTheTask(R.TESTDATA.get("selectAssigneeForTheTask.assignee"));

		devOpsTaskDetailsBox.provideTheEstimates(R.TESTDATA.get("verifyRabbitMQ.originalEstimate"),
				R.TESTDATA.get("verifyRabbitMQ.remainingEstimate"), R.TESTDATA.get("verifyRabbitMQ.completedTime"));

		devOpsTaskDetailsBox.clickSaveAndCloseButton();

		Thread.sleep(3000);

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

		// Open the task added in the previous steps from the task list displayed for
		// the employee
		DevOpsAddTimeSheetBox devOpsAddTimeSheetBox = devopsTaskDetailsBox
				.selectTheRequiredTaskForAddingTimeSheet(idofTheTaskAdded);

		DatePickerForAddingTimesheet datePickerForAddingTimesheet = devOpsAddTimeSheetBox.clicktaskDateField();

		datePickerForAddingTimesheet.selectDate(R.TESTDATA.get("verifyRabbitMQ.year"),
				R.TESTDATA.get("verifyRabbitMQ.month"), R.TESTDATA.get("verifyRabbitMQ.day"));

		devOpsAddTimeSheetBox.selectTime(R.TESTDATA.get("verifyRabbitMQ.time"));

		devOpsAddTimeSheetBox.enterTaskDescription(R.TESTDATA.get("verifyRabbitMQ.description"));

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

		Thread.sleep(3000);

		// Select option "All" from pagination
		timesheetDetailsGridBox.displayAllRows();

		// Expand the task details of the date for which the timesheet was added
		boolean isDateAvailableInTheList = timesheetDetailsGridBox
				.expandTimesheetOfRequiredDate(R.TESTDATA.get("verifyRabbitMQ.date"));

		Assert.assertTrue(isDateAvailableInTheList,
				"The date for which the task was added is not available in the task list of the employee.");

		// Selecting option "All" from pagination for task details
		timesheetDetailsGridBox.displayAllRows();

		// Verfying the timesheet added is present and status is pending
		List<String> detailsFetched = timesheetDetailsGridBox.fetchTaskDetails();
		boolean isTimeSheetAdded = false;
		for (String details : detailsFetched) {
			if (details.contains(idofTheTaskAdded) && details.contains(R.TESTDATA.get("verifyRabbitMQ.time"))
					&& details.contains(R.TESTDATA.get("verifyRabbitMQ.tile"))) {
				isTimeSheetAdded = true;
				System.out.println("Timesheet has been added succesfully");
				break;
			}

		}
		if (isTimeSheetAdded == false) {
			fail("Timesheet added is not available in the task list of the employee.");
		}

		TaskDetailsExpandedGridBox taskDetailsExpandedGridBox = new TaskDetailsExpandedGridBox(getDriver(),
				getDriver());
		taskDetailsExpandedGridBox.clickSubmitButton(R.TESTDATA.get("verifyRabbitMQ.tile"), idofTheTaskAdded,
				R.TESTDATA.get("verifyRabbitMQ.time"));

		Assert.assertTrue(timesheetLandingPage.getSuccessAlertOnSubmittingTimesheet().isVisible(),
				"Success Alert  is not displayed on submitting the timesheet.");

		Thread.sleep(3000);

		// Login to devops to verify the estimates are not changed
		DevOpsLoginPage devOpsLoginpage = new DevOpsLoginPage(getDriver());
		DevOpsLandingPage devOpsLandingpage = devOpsLoginpage.devopsEmployeeLogin();

		Assert.assertTrue(devOpsLandingpage.getProjectCard().isVisible(),
				"Devops Login is not successfull for the user : " + R.TESTDATA.get("devopsEmployeeLogin.userName"));
		// Select the project and navigate to taskboard
		isProjectSelected = projectCardBox.selectTheRequiredProject(R.TESTDATA.get("selectProject.project"));
		Assert.assertTrue(isProjectSelected, "The required project : " + R.TESTDATA.get("selectProject.project")
				+ " is not available for the logged in user.");

		TaskBoardLandingPage taskBoardLandingpage = devOpsLandingPage.navigateToTaskBoard();
		Thread.sleep(5000);

		WorkItemsBox workItemsbox = taskBoardLandingpage.getWorkItemsBox();

		// Open the task added
		workItemsbox.selectTheRequiredTask(R.TESTDATA.get("verifyRabbitMQ.tile"));
		Thread.sleep(4000);

		String originalEstimateAfterAddingTask = devOpsTaskDetailsBox.getOriginalEstimateField().getAttribute("value");

		String remainingEstimateAfterAddingTask = devOpsTaskDetailsBox.getRemainingEstimateField()
				.getAttribute("value");

		String completedTimeAfterAddingTask = devOpsTaskDetailsBox.getCompletedTimeField().getAttribute("value");

		Assert.assertEquals(originalEstimateAfterAddingTask, R.TESTDATA.get("verifyRabbitMQ.originalEstimate"),
				"The original estimates after adding timesheet do not match");

		Assert.assertEquals(remainingEstimateAfterAddingTask, R.TESTDATA.get("verifyRabbitMQ.remainingEstimate"),
				"The remaining estimates after adding timesheet do not match");

		Assert.assertEquals(completedTimeAfterAddingTask, R.TESTDATA.get("verifyRabbitMQ.completedTime"),
				"The completed time after adding timesheet do not match");

		devOpsTaskDetailsBox.getCloseButton().click();

		Thread.sleep(3000);

		TimesheetLandingPage timesheetLandingpage = managerLogin(R.TESTDATA.get("managerLogin.militaryIdTwo"),
				R.TESTDATA.get("managerLogin.password"));
		HeaderBox header = timesheetLandingpage.getHeaderBox();

		Assert.assertTrue(header.getManagerButton().isVisible(), " The logged in user '"
				+ R.TESTDATA.get("managerLogin.militaryIdTwo") + "' does not have a manager role");
		ManagerLandingPage managerLandingPage = header.clickManagerButton();

		TaskApprovalListBox taskApprovalListBox = managerLandingPage.getTaskApprovalListBox();
		EmployeeTaskListBox employeeTaskListBox = managerLandingPage.getEmployeeTaskList();

		boolean isEmployeeAvailable = taskApprovalListBox
				.expandTheTaskDetailsOfParticularEmployee(R.TESTDATA.get("employee.employeeName"));

		Assert.assertTrue(isEmployeeAvailable, "The employee :" + R.TESTDATA.get("employee.employeeName")
				+ " is not available in the list of the manager user.");

		employeeTaskListBox.clickSubmitButtonOfTheRequiredTask(R.TESTDATA.get("verifyRabbitMQ.date"),
				R.TESTDATA.get("verifyRabbitMQ.tile"), idofTheTaskAdded, R.TESTDATA.get("verifyRabbitMQ.time"));

		// Verify the success alert displayed on manager rejecting the task
		Assert.assertTrue(managerLandingPage.getSuccessAlertOnApprovingTimesheet().isPresent(),
				"Expected Success Alert is not displayed on manager approving the timesheet.");

		// Login to devops to verify the estimates are updated
		DevOpsLoginPage devOpsloginpage = new DevOpsLoginPage(getDriver());
		DevOpsLandingPage devOpslandingpage = devOpsloginpage.devopsEmployeeLogin();

		Assert.assertTrue(devOpslandingpage.getProjectCard().isVisible(),
				"Devops Login is not successfull for the user : " + R.TESTDATA.get("devopsEmployeeLogin.userName"));
		// Select the project and navigate to taskboard
		isProjectSelected = projectCardBox.selectTheRequiredProject(R.TESTDATA.get("selectProject.project"));
		Assert.assertTrue(isProjectSelected, "The required project : " + R.TESTDATA.get("selectProject.project")
				+ " is not available for the logged in user.");

		TaskBoardLandingPage taskBoardlandingpage = devOpsLandingPage.navigateToTaskBoard();
		Thread.sleep(5000);

		WorkItemsBox workitemsbox = taskBoardlandingpage.getWorkItemsBox();

		// Open the task added and select the assignee and provide the estimates
		workitemsbox.selectTheRequiredTask(R.TESTDATA.get("verifyRabbitMQ.tile"));

		String originalEstimateAfterApprovingTask = devOpsTaskDetailsBox.getOriginalEstimateField()
				.getAttribute("value");

		System.out.println("original estimate fetched after approving task : " + originalEstimateAfterApprovingTask);

		String remainingEstimateAfterApprovingTask = devOpsTaskDetailsBox.getRemainingEstimateField()
				.getAttribute("value");

		System.out.println("remaining estimate fetched after approving task: " + remainingEstimateAfterApprovingTask);

		String completedTimeAfterApprovingTask = devOpsTaskDetailsBox.getCompletedTimeField().getAttribute("value");

		System.out.println("completed time fetched after approving task : " + completedTimeAfterApprovingTask);
		String requiredRemainingTime = devOpsTaskDetailsBox.calculateRequiredRemainingTime(
				R.TESTDATA.get("verifyRabbitMQ.time"), R.TESTDATA.get("verifyRabbitMQ.remainingEstimate"));

		System.out.println("required Remaining Time calculated : " + requiredRemainingTime);

		String requiredCompletedTime = devOpsTaskDetailsBox.calculateRequiredCompletedTime(
				R.TESTDATA.get("verifyRabbitMQ.time"), R.TESTDATA.get("verifyRabbitMQ.completedTime"));

		System.out.println("required Completed Time Calculated : " + requiredCompletedTime);

		Assert.assertEquals(originalEstimateAfterApprovingTask, R.TESTDATA.get("verifyRabbitMQ.originalEstimate"),
				"The original estimates after submitting timesheet do not match");

		Assert.assertEquals(remainingEstimateAfterApprovingTask, requiredRemainingTime,
				"The remaining estimate after submitting timesheet is not as expected.");

		Assert.assertEquals(completedTimeAfterApprovingTask, requiredCompletedTime,
				"The completed time after submitting timesheet is not as expected");

		Thread.sleep(3000);

	}
}