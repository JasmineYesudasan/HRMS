package pits.automation.web.hrms.employee.pages;

import static org.junit.jupiter.api.Assertions.fail;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

public class EmployeeTaskDetailsMenuBox extends AbstractUIObject {

	@FindBy(xpath = ".//*[@data-test-id='btnSubmit']")
	private ExtendedWebElement taskDetailsSubmitButton;

	@FindBy(xpath = ".//*[@data-test-id='btnRemove']")
	private ExtendedWebElement taskDetailsRemoveButton;

	@FindBy(xpath = ".//*[@data-test-id='btnEdit']")
	private ExtendedWebElement taskDetailsEditButton;

	@FindBy(xpath = ".//*[@data-test-id='btnClose']")
	private ExtendedWebElement taskDetailsCloseButton;

	@FindBy(xpath = "//*[@id='inspire']/div[3]/div/div[1]/span/div")
	private ExtendedWebElement taskDetailsDisplayed;

	@FindBy(xpath = "//*[@id='app']/div[4]/div/div[1]/span/div/text()[1]")
	private ExtendedWebElement taskIdValueDisplayed;

	@FindBy(xpath = "//*[@id='app']/div[4]/div/div[1]/span/div/text()[2]")
	private ExtendedWebElement taskDescriptionValueDisplayed;

	@FindBy(xpath = "//*[@id='app']/div[4]/div/div[1]/span/div/text()[3]")
	private ExtendedWebElement tasktimeTakenValueDisplayed;

	@FindBy(xpath = "//*[@id='app']/div[4]/div/div[1]/span/div/text()[4]")
	private ExtendedWebElement rejectReasonDisplayed;

	public EmployeeTaskDetailsMenuBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);

	}

	public void clickSubmitButton() {
		if (taskDetailsSubmitButton.isVisible()) {
			taskDetailsSubmitButton.click();
		} else {
			fail("The task does not have a submit button. Please verify if the task is in a future date.");
		}
	}

	public boolean verifyRemoveButton() {
		boolean isRemoveButtonFound = false;
		if (taskDetailsRemoveButton.isVisible()) {
			isRemoveButtonFound = true;
		}
		taskDetailsCloseButton.click();
		return isRemoveButtonFound;
	}

	public boolean verifyEditButton() {
		boolean isEditButtonFound = false;
		if (taskDetailsEditButton.isVisible()) {
			isEditButtonFound = true;
		}
		taskDetailsCloseButton.click();
		return isEditButtonFound;
	}

	public RemoveConfirmationPopUp clickRemoveButton() {
		taskDetailsRemoveButton.click();
		return new RemoveConfirmationPopUp(driver, driver);
	}

	public EmployeeEditTimeSheetBox clickEditButton() {
		taskDetailsEditButton.click();
		return new EmployeeEditTimeSheetBox(driver, driver);
	}

	public ExtendedWebElement getTaskIdValueDisplayed() {
		return taskIdValueDisplayed;
	}

	public ExtendedWebElement getTaskDescriptionValueDisplayed() {
		return taskDescriptionValueDisplayed;
	}

	public ExtendedWebElement getTasktimeTakenValueDisplayed() {
		return tasktimeTakenValueDisplayed;
	}

	public ExtendedWebElement getTaskDetailsDisplayed() {
		return taskDetailsDisplayed;
	}

	public ExtendedWebElement getRejectReasonDisplayed() {
		return rejectReasonDisplayed;
	}
}
