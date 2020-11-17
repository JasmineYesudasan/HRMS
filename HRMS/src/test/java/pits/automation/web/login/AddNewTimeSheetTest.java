package pits.automation.web.login;

import static org.testng.Assert.fail;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qaprosoft.carina.core.foundation.utils.R;

import pits.automation.web.hrms.employee.pages.DatePickerForAddingTimesheet;
import pits.automation.web.hrms.pages.TimesheetLandingPage;

public class AddNewTimeSheetTest extends BaseTest {

	@Test(priority = 1)
	public void addNewTimeSheet() throws InterruptedException {
		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));
		timesheetLandingPage.clickAddNewTimeSheetButton();

		DatePickerForAddingTimesheet datePicker = timesheetLandingPage.getAddNewTimeSheetBox().clickTaskDateField();
		datePicker.selectDate(R.TESTDATA.get("selectDate.requiredYear"), R.TESTDATA.get("selectDate.requiredMonth"),
				R.TESTDATA.get("selectDate.requiredDay"));
		timesheetLandingPage.getAddNewTimeSheetBox().enterTaskID(R.TESTDATA.get("enterTaskID.taskId"));

		timesheetLandingPage.getAddNewTimeSheetBox().selectTime(R.TESTDATA.get("selectTime.time"));
		timesheetLandingPage.getAddNewTimeSheetBox()
				.enterTaskDescription(R.TESTDATA.get("enterTaskDescription.description"));
		timesheetLandingPage.getAddNewTimeSheetBox().clickSaveButton();

		Assert.assertTrue(timesheetLandingPage.getsuccessAlertOnAddingTimesheet().isPresent(),
				"Success Message is not displayed");
		// System.out.println(timesheetLandingPage.getSuccessAlert().getText());
		// Assert.assertTrue(timesheetLandingPage.getSuccessAlert().getText().contains("Timesheet
		// saved successfully"), "Message in the success alert is not as expected");

		Thread.sleep(6000);
	}

	@Test(priority = 2)
	public void test() throws InterruptedException {
		TimesheetLandingPage timesheetLandingPage = employeeLogin(R.TESTDATA.get("employeeLogin.militaryId"),
				R.TESTDATA.get("employeeLogin.password"));

		timesheetLandingPage.getTimeSheetDetailsGridBox().displayAllRows();
		timesheetLandingPage.getTimeSheetDetailsGridBox()
				.expandTimesheetOfRequiredDate(R.TESTDATA.get("selectDate.selectedDate"));

		timesheetLandingPage.getTimeSheetDetailsGridBox().displayAllRows();
		List<String> detailsFetched = timesheetLandingPage.getTimeSheetDetailsGridBox().fetchTaskDetails();

		boolean isTimeSheetAdded = false;
		for (String details : detailsFetched) {
			if (details.contains(R.TESTDATA.get("enterTaskID.taskId"))
					&& details.contains(R.TESTDATA.get("enterTaskDescription.description"))) {
				isTimeSheetAdded = true;
				System.out.println("Timesheet has been added succesfully");
			}

		}
		if (isTimeSheetAdded == false) {
			fail("Timesheet is not added");
		}

	}

}
