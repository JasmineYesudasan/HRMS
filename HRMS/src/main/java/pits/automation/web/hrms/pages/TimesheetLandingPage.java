package pits.automation.web.hrms.pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractPage;

import pits.automation.web.hrms.employee.devops.pages.PatTokenBox;
import pits.automation.web.hrms.employee.pages.AddNewTimeSheetBox;
import pits.automation.web.hrms.employee.pages.EmployeeEditTimeSheetBox;
import pits.automation.web.hrms.employee.pages.EmployeeTaskDetailsMenuBox;
import pits.automation.web.hrms.employee.pages.RemoveConfirmationPopUp;
import pits.automation.web.hrms.employee.pages.TaskDetailsExpandedGridBox;
import pits.automation.web.hrms.employee.pages.TimeSheetDetailsCalenderBox;
import pits.automation.web.hrms.employee.pages.TimeSheetDetailsGridBox;
import pits.automation.web.hrms.manager.pages.ManagerTaskDetailsMenuBox;

public class TimesheetLandingPage extends AbstractPage {

	@FindBy(xpath = ".//*[@data-test-id='btnAddNewTimeSheet']")
	private ExtendedWebElement addNewTimeSheetButton;

	@FindBy(xpath = "//*[@id='app']/div[3]/div/div/div[3]")
	private AddNewTimeSheetBox addNewTimeSheetBox;

	@FindBy(xpath = "(//tbody)[1]")
	private TimeSheetDetailsGridBox timeSheetDetailsGridBox;

	@FindBy(xpath = "//*[@id='app']/div/div[2]/div/div[1]")
	private TimeSheetDetailsCalenderBox timeSheetDetailsCalenderBox;

	@FindBy(xpath = "//*[@id='inspire']/div[1]/main/div/div[2]/div[1]/div/div/div/div[4]/div/div[1]")
	private ExtendedWebElement successAlertOnAddingTimesheet;

	@FindBy(xpath = "//*[@id='inspire']/div[1]/main/div/div[2]/div[1]/div/div/div/div[4]/div/div[1]")
	private ExtendedWebElement successAlertOnSubmittingTimesheet;

	@FindBy(xpath = "//*[@id='inspire']/div[1]/main/div/div[2]/div[1]/div/div/div/div[4]/div/div[1]")
	private ExtendedWebElement successAlertOnSubmittingTimesheetInCalenderMode;

	@FindBy(xpath = "//*[@id='app']/div/div[3]/div/div[1]")
	private ExtendedWebElement successAlertOnEditingTimesheet;

	@FindBy(xpath = "//div[1][@role='button']/div/div[@class='v-input__append-inner']")
	private ExtendedWebElement statusDropdown;

	@FindBy(xpath = "//*[@role='listbox']/div[@role='option']")
	private List<ExtendedWebElement> statusDropdownOptions;

	@FindBy(xpath = ".//*[@class='v-label theme--light']")
	private ExtendedWebElement grid_CalenderLabel;

	@FindBy(xpath = "//*[@role='switch']")
	private ExtendedWebElement grid_CalenderToggleButton;

	@FindBy(xpath = ".//*[@role='menu']")
	private ManagerTaskDetailsMenuBox taskDetailsMenuBox;

	@FindBy(xpath = "//*[@id='inspire']/div/main/div/div[1]/header")
	private HeaderBox headerBox;

	@FindBy(xpath = "//*[@id='app']/div[4]")
	private EmployeeTaskDetailsMenuBox employeeTaskDetailsMenuBox;

	@FindBy(xpath = "//*[@id='app']/div[4]/div/div")
	private RemoveConfirmationPopUp removeConfirmationPopUp;

	@FindBy(xpath = "//*[@id='app']/div/div[3]/div/div[1]")
	private ExtendedWebElement successAlertOnRemovingTimesheet;

	@FindBy(xpath = "//*[@id='app']/div/div[3]/div/div[1]")
	private EmployeeEditTimeSheetBox employeeEditTimeSheetBox;

	@FindBy(xpath = ".//*[@id='patIdDiv']")
	private PatTokenBox patTokenBox;

	@FindBy(xpath = "(//tbody)[2]")
	private TaskDetailsExpandedGridBox taskDetailsExpandedGridBox;

	public TimesheetLandingPage(WebDriver driver) {
		super(driver);
		setPageURL("Timesheet?redirect=%2FTimesheet");

	}

	public AddNewTimeSheetBox clickAddNewTimeSheetButton() {
		addNewTimeSheetButton.click();
		return new AddNewTimeSheetBox(driver, driver);
	}

	public AddNewTimeSheetBox getAddNewTimeSheetBox() {
		return addNewTimeSheetBox;
	}

	public ExtendedWebElement getsuccessAlertOnAddingTimesheet() {
		return successAlertOnAddingTimesheet;
	}

	public TimeSheetDetailsGridBox getTimeSheetDetailsGridBox() {
		return timeSheetDetailsGridBox;
	}

	public boolean applyFilterWithRequiredStatus(String statusToBeSelected) {
		statusDropdown.click();
		boolean isStatusFound = false;
		for (ExtendedWebElement statusOption : statusDropdownOptions) {
			if (statusOption.getText().equals(statusToBeSelected)) {
				isStatusFound = true;
				statusOption.click();
				break;
			}

		}
		return isStatusFound;
	}

	public void toggleToCalender() {

		if (grid_CalenderLabel.getText().equals("Calender")) {
			grid_CalenderToggleButton.click();
		}

		else {
			System.out.println("Already in Calender Mode");
		}

	}

	public void navigateToTopOfThePage() {
		Actions builder = new Actions(driver);
		builder.moveToElement(addNewTimeSheetButton.getElement()).perform();
	}

	public TimeSheetDetailsCalenderBox getTimeSheetDetailsCalenderBox() {
		return timeSheetDetailsCalenderBox;
	}

	public ExtendedWebElement getSuccessAlertOnSubmittingTimesheet() {
		return successAlertOnSubmittingTimesheet;
	}

	public ManagerTaskDetailsMenuBox getTaskDetailsMenuBox() {
		return taskDetailsMenuBox;
	}

	public ExtendedWebElement getSuccessAlertOnSubmittingTimesheetInCalenderMode() {
		return successAlertOnSubmittingTimesheetInCalenderMode;
	}

	public HeaderBox getHeaderBox() {
		return headerBox;
	}

	public RemoveConfirmationPopUp getRemoveConfirmationPopUp() {
		return removeConfirmationPopUp;
	}

	public ExtendedWebElement getSuccessAlertOnRemovingTimesheet() {
		return successAlertOnRemovingTimesheet;
	}

	public EmployeeTaskDetailsMenuBox getEmployeeTaskDetailsMenuBox() {
		return employeeTaskDetailsMenuBox;
	}

	public EmployeeEditTimeSheetBox getEmployeeEditTimeSheetBox() {
		return employeeEditTimeSheetBox;
	}

	public ExtendedWebElement getSuccessAlertOnEditingTimesheet() {
		return successAlertOnEditingTimesheet;
	}

	public PatTokenBox getPatTokenBox() {
		return patTokenBox;
	}

	public TaskDetailsExpandedGridBox getTaskDetailsExpandedGridBox() {
		return taskDetailsExpandedGridBox;
	}

}
