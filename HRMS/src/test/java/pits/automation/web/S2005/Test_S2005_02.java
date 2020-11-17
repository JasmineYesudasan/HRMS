package pits.automation.web.S2005;

import static org.junit.jupiter.api.Assertions.fail;

import java.text.ParseException;
import java.util.List;

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
import pits.automation.web.hrms.employee.pages.TimeSheetDetailsCalenderBox;
import pits.automation.web.hrms.employee.pages.TimeSheetDetailsGridBox;
import pits.automation.web.hrms.pages.TimesheetLandingPage;
import pits.automation.web.login.BaseTest;

public class Test_S2005_02 extends BaseTest {

	// HPB-57 - [DevOps Intergration] - Display and Filter the DevOps tasks by
	// Project Name
	@Test(priority = 1)
	public void verifyProjectNameFilterInAddAndEditTimesheetPopUps() throws InterruptedException, ParseException {

		// Login to Devops

		DevOpsLoginPage devOpsLoginPage = new DevOpsLoginPage(getDriver());
		DevOpsLandingPage devOpsLandingPage = devOpsLoginPage.devopsEmployeeLogin();

		Assert.assertTrue(devOpsLandingPage.getProjectCard().isVisible(),
				"Devops Login is not successfull for the user : " + R.TESTDATA.get("devopsEmployeeLogin.userName"));

		ProjectCardBox projectCardBox = devOpsLandingPage.getProjectCardBox();

		List<String> projectsAvailableForTheUserInDevops = projectCardBox.fetchTheProjectList();

		for (String project : projectsAvailableForTheUserInDevops) {
			System.out.println(project);
		}

		// Select the project and navigate to taskboard
		boolean isProjectSelected = projectCardBox.selectTheRequiredProject(R.TESTDATA.get("selectProject.project"));
		Assert.assertTrue(isProjectSelected, "The required project : " + R.TESTDATA.get("selectProject.project")
				+ " is not available for the logged in user.");
		TaskBoardLandingPage taskBoardLandingPage = devOpsLandingPage.navigateToTaskBoard();
		Thread.sleep(10000);
		WorkItemsBox workItemsBox = taskBoardLandingPage.getWorkItemsBox();

		// Add a new task
		workItemsBox.clickAddWorkItemButton();
		workItemsBox.selectWorkItem(R.TESTDATA.get("selectWorkItem.workItem"));

		workItemsBox.enterTaskTitle(R.TESTDATA.get("verifyProjectNameFilter.title"));

		// Open the task added and select the assignee and provide the estimates
		DevOpsTaskDetailsBox devOpsTaskDetailsBox = workItemsBox
				.selectTheRequiredTask(R.TESTDATA.get("verifyProjectNameFilter.title"));

		String idofTheTaskAdded = devOpsTaskDetailsBox.getTaskIdField().getText();

		devOpsTaskDetailsBox.selectAssigneeForTheTask(R.TESTDATA.get("selectAssigneeForTheTask.assignee"));

		devOpsTaskDetailsBox.provideTheEstimates(R.TESTDATA.get("provideTheEstimates.originalEstimate"),
				R.TESTDATA.get("provideTheEstimates.remainingEstimate"), R.TESTDATA.get(""));

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
		ProjectSelectorBox projectSelectorBox = devopsTaskDetailsBox.getProjectselectorBox();

		Assert.assertTrue(projectSelectorBox.getProjectDropDown().isVisible(),
				"Project Name filter is not available in the devops task list in add new timeshet pop up.");

		projectSelectorBox.clickSelectProjectDropDown();
		Thread.sleep(3000);
		List<String> projectsDisplayedInHRMS = projectSelectorBox.fetchTheProjectList();

		for (String project : projectsDisplayedInHRMS) {
			System.out.println(project);
		}
		// Assert.assertEquals(projectsAvailableForTheUserInDevops,
		// projectsDisplayedInHRMS,
		// "The projects available for the user in Devops and The Projects displayed in
		// HRMS do not match.");
		boolean isProjectAvailableInHeader;

		projectSelectorBox.selectTheRequiredProject(R.TESTDATA.get("selectProject.project"));

		devopsTaskDetailsBox.displayAllRows("All");
		isProjectAvailableInHeader = devopsTaskDetailsBox.verifyTaskDetailsHeaderElement(R.TESTDATA.get("Project"));

		Assert.assertTrue(isProjectAvailableInHeader,
				"Project Name column is not available in the devops task details list.");

		List<String> projectNamesDisplayedOnSecondFilter = devopsTaskDetailsBox.fetchProjectNames();

		for (String projectName : projectNamesDisplayedOnSecondFilter) {
			if (!(projectName.equals(R.TESTDATA.get("selectProject.project")))) {
				fail("The project names displayed consist of other projects than the filter applied.");
			}
		}
		String projectNameDisplayed = devopsTaskDetailsBox.verifyProjectName(idofTheTaskAdded);

		Assert.assertTrue(projectNameDisplayed.equalsIgnoreCase(R.TESTDATA.get("selectProject.project")),
				"The project name displayed is not as expected.");

		// Open the task added in the previous steps from the task list displayed for
		// the employee
		DevOpsAddTimeSheetBox devOpsAddTimeSheetBox = devopsTaskDetailsBox
				.selectTheRequiredTaskForAddingTimeSheet(idofTheTaskAdded);

		Assert.assertTrue(
				devOpsAddTimeSheetBox.getProjectNameField().getAttribute(R.TESTDATA.get("getAttribute.attribute"))
						.equalsIgnoreCase(R.TESTDATA.get("getAttribute.value")),
				"The project name field is not read only in the add new timesheet through devops popup.");

		DatePickerForAddingTimesheet datePickerForAddingTimesheet = devOpsAddTimeSheetBox.clicktaskDateField();

		datePickerForAddingTimesheet.selectDate(R.TESTDATA.get("selectDate.requiredYearfordevops"),
				R.TESTDATA.get("selectDate.requiredMonthfordevops"), R.TESTDATA.get("selectDate.requiredDayfordevops"));

		devOpsAddTimeSheetBox.selectTimeForDevOps(R.TESTDATA.get("selectTime.requiredTimefordevops"));

		devOpsAddTimeSheetBox.enterTaskDescription(R.TESTDATA.get("devops.description"));

		devOpsAddTimeSheetBox.clickSaveButton();

		timesheetLandingPage.refresh();

		// Select option "All" from pagination
		timesheetDetailsGridBox.displayAllRows();

		// Expand the task details of the date for which the timesheet was added
		timesheetDetailsGridBox.expandTimesheetOfRequiredDate(R.TESTDATA.get("selectDate.devOpsDate"));

		// Selecting option "All" from pagination for task details
		timesheetDetailsGridBox.displayAllRows();

		EmployeeEditTimeSheetBox employeeEditTimeSheetBox = timesheetDetailsGridBox
				.clickEditButtonOfRequiredTask(idofTheTaskAdded);

		Assert.assertTrue(employeeEditTimeSheetBox.getDevopsButton().isVisible(),
				"DevOps button is not available for the task.");

		DevopsTaskDetailsBox devopsEditTaskDetailsBox = employeeEditTimeSheetBox.clickDevOpsButton();

		Assert.assertTrue(projectSelectorBox.getProjectDropDown().isVisible(),
				"The project name filter is not available in th edit timesheet pop up.");

		projectSelectorBox.clickSelectProjectDropDown();

		projectSelectorBox.selectTheRequiredProject(R.TESTDATA.get("selectProject.project"));

		devopsTaskDetailsBox.displayAllRows("All");
		Thread.sleep(3000);
		isProjectAvailableInHeader = devopsTaskDetailsBox.verifyTaskDetailsHeaderElement(R.TESTDATA.get("Project"));

		Assert.assertTrue(isProjectAvailableInHeader,
				"Project Name column is not available in the devops task details list.");

		List<String> projectNamesDisplayedOnEditPopUp = devopsTaskDetailsBox.fetchProjectNamesOnGridEdit();

		for (String projectName : projectNamesDisplayedOnEditPopUp) {
			System.out.println(projectName);
			if (!(projectName.equals(R.TESTDATA.get("selectProject.project")))) {
				fail("The project names displayed consist of other projects than the filter applied.");
			}
		}

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
		EmployeeTaskDetailsMenuBox employeeTaskDetailsMenuBox = timeSheetDetailsCalenderBox
				.clickTheRequiredTask(idofTheTaskAdded, R.TESTDATA.get("selectTime.requiredTimefordevops"), "");

		EmployeeEditTimeSheetBox employeeEditTimeSheetBoxFromCalenderView = employeeTaskDetailsMenuBox
				.clickEditButton();

		employeeEditTimeSheetBoxFromCalenderView.getDevopsButton().click();

		Assert.assertTrue(projectSelectorBox.getProjectDropDown().isVisible(),
				"The project name filter is not available in th edit timesheet pop up.");

		Thread.sleep(4000);

	}

}
