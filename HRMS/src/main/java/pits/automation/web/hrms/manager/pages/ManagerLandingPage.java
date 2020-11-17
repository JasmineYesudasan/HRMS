package pits.automation.web.hrms.manager.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractPage;

import pits.automation.web.hrms.pages.HeaderBox;

public class ManagerLandingPage extends AbstractPage {

	@FindBy(xpath = "//*[@id='inspire']/div/main/div/div[2]/div[2]/div/div/div")
	private TaskApprovalListBox taskApprovalListBox;

	@FindBy(xpath = "//table/tbody")
	private EmployeeTaskListBox employeeTaskList;

	@FindBy(xpath = ".//*[@class='row']")
	private ManagerCalenderBox managerCalenderBox;

	@FindBy(xpath = ".//*[@role='menu']")
	private ManagerTaskDetailsMenuBox taskDetailsMenuBox;

	@FindBy(xpath = ".//*[@data-test-id='aTimesheetApproval']")
	private ExtendedWebElement managerButton;

	@FindBy(xpath = ".//*[@class='v-snack__content']")
	private ExtendedWebElement successAlertOnApprovingTimesheet;

	@FindBy(xpath = ".//*[@class='v-snack__content']")
	private ExtendedWebElement successAlertOnRejectingTimesheet;

	@FindBy(xpath = "//*[@id='inspire']/div/main/div/div[1]/header")
	private HeaderBox headerBox;

	@FindBy(xpath = "//*[@id='firstRow']/div")
	private TaskRejectConfirmationPopUp taskRejectConfirmationPopUp;

	public ManagerLandingPage(WebDriver driver) {
		super(driver);

	}

	public TaskApprovalListBox getTaskApprovalListBox() {
		return taskApprovalListBox;
	}

	public EmployeeTaskListBox getEmployeeTaskList() {
		return employeeTaskList;
	}

	public ExtendedWebElement getManagerButton() {
		return managerButton;
	}

	public ManagerCalenderBox getManagerCalenderBox() {
		return managerCalenderBox;
	}

	public ManagerTaskDetailsMenuBox getTaskDetailsMenuBox() {
		return taskDetailsMenuBox;
	}

	public ExtendedWebElement getSuccessAlertOnApprovingTimesheet() {
		return successAlertOnApprovingTimesheet;
	}

	public ExtendedWebElement getSuccessAlertOnRejectingTimesheet() {
		return successAlertOnRejectingTimesheet;
	}

	public HeaderBox getHeaderBox() {
		return headerBox;
	}

	public TaskRejectConfirmationPopUp getTaskRejectConfirmationPopUp() {
		return taskRejectConfirmationPopUp;
	}

}
