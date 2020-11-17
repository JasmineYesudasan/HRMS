package pits.automation.web.hrms.employee.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

import pits.automation.web.hrms.employee.devops.pages.DevopsTaskDetailsBox;

public class EmployeeEditTimeSheetBox extends AbstractUIObject {

	@FindBy(xpath = ".//*[@class='headline']")
	private ExtendedWebElement headingOfEditBox;

	@FindBy(xpath = ".//*[@data-test-id='txtTaskTitle']")
	private ExtendedWebElement taskTitleField;

	@FindBy(xpath = ".//*[@data-test-id='txtDate']")
	private ExtendedWebElement taskDateField;

	@FindBy(xpath = ".//*[@data-test-id='txtTaskId']")
	private ExtendedWebElement taskIdField;

	@FindBy(xpath = "//div/form/div[4]/div/div[1]/div[1]/div[1]")
	private ExtendedWebElement timeTakenField;

	@FindBy(xpath = ".//*[@data-test-id='txaDescription']")
	private ExtendedWebElement timeDescriptionField;

	@FindBy(xpath = ".//*[@data-test-id='btnUpdate']")
	private ExtendedWebElement updateButton;

	@FindBy(xpath = ".//*[@role='listbox']/div[@role='option']")
	private List<ExtendedWebElement> timeOptionsList;

	@FindBy(xpath = ".//*[@data-test-id='btnDevops']")
	private ExtendedWebElement devopsButton;

	public EmployeeEditTimeSheetBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);

	}

	public DatePickerForEditingTimeSheet clickDateField() {
		taskDateField.click();
		return new DatePickerForEditingTimeSheet(driver, driver);
	}

	public void editTimeField(String timeToBeSelected) throws InterruptedException {
		timeTakenField.click();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollTop = arguments[1];",
				driver.findElement(By.xpath(".//*[@id='app']/div[6]")), 5000);
		js.executeScript("arguments[0].scrollTop = arguments[1];",
				driver.findElement(By.xpath(".//*[@id='app']/div[6]")), 5000);

		for (ExtendedWebElement time : timeOptionsList) {
			if (time.getText().equals(timeToBeSelected)) {
				time.click();
				break;
			}
		}
	}

	public void editTaskID(String editedTaskID) {
		taskIdField.getElement().sendKeys(Keys.CONTROL + "a");
		taskIdField.getElement().sendKeys(Keys.DELETE);

		taskIdField.type(editedTaskID);
	}

	public void editTaskDescription(String editedDescription) {
		timeDescriptionField.getElement().sendKeys(Keys.CONTROL + "a");
		timeDescriptionField.getElement().sendKeys(Keys.DELETE);
		timeDescriptionField.type(editedDescription);
	}

	public void editTaskTitle(String editedTaskTitle) {
		taskTitleField.getElement().sendKeys(Keys.CONTROL + "a");
		taskTitleField.getElement().sendKeys(Keys.DELETE);
		taskTitleField.type(editedTaskTitle);
	}

	public DevopsTaskDetailsBox clickDevOpsButton() {
		devopsButton.click();
		return new DevopsTaskDetailsBox(driver, driver);

	}

	public void clickUpdateButton() {
		updateButton.click();
	}

	public ExtendedWebElement getDevopsButton() {
		return devopsButton;
	}

	public ExtendedWebElement getTaskTitleField() {
		return taskTitleField;
	}
}
