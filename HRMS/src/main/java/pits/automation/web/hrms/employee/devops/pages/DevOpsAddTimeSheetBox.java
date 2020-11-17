package pits.automation.web.hrms.employee.devops.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

import pits.automation.web.hrms.employee.pages.DatePickerForAddingTimesheet;

public class DevOpsAddTimeSheetBox extends AbstractUIObject {

	@FindBy(xpath = "(.//*[@data-test-id='txtTaskId'])[2]")
	private ExtendedWebElement taskIdField;

	@FindBy(xpath = ".//*[@data-test-id='txtnewTaskTitle']")
	private ExtendedWebElement taskTitle;

	@FindBy(xpath = ".//*[@data-test-id='txtnewProjectName']")
	private ExtendedWebElement projectNameField;

	@FindBy(xpath = ".//*[@data-test-id='txtoriginalEstimate']")
	private ExtendedWebElement originalEstimate;

	@FindBy(xpath = ".//*[@data-test-id='txtnewTaskCompleted']")
	private ExtendedWebElement completedTime;

	@FindBy(xpath = ".//*[@data-test-id='txtnewTaskRemaining']")
	private ExtendedWebElement remainingTime;

	@FindBy(xpath = "(.//*[@data-test-id='txtDate'])[2]")
	private ExtendedWebElement taskDateField;

	@FindBy(xpath = "//*[@id='addNewDevopsTimesheetDiv']/div/div[3]/div/form/div/div[8]/div/div/div[1]/div[1]")
	private ExtendedWebElement timeTakenField;

	@FindBy(xpath = "(.//*[@data-test-id='txaDescription'])[2]")
	private ExtendedWebElement taskDescriptionField;

	@FindBy(xpath = "(.//*[@data-test-id='btnSave'])[2]")
	private ExtendedWebElement saveButton;

	@FindBy(xpath = "//*[@id='inspire']/div[1]/main/div/div[2]/div[1]/div/div/div/div[4]/div/div[1]")
	private ExtendedWebElement successAlertOnAddingTimesheet;

	public DevOpsAddTimeSheetBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);
	}

	public DatePickerForAddingTimesheet clicktaskDateField() {
		taskDateField.click();
		return new DatePickerForAddingTimesheet(driver, driver);
	}

	public void selectTime(String timeToBeSelected) throws InterruptedException {
		timeTakenField.click();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollTop = arguments[1];",
				driver.findElement(By.xpath("//*[@id='inspire']/div[7]")), 1000);
		js.executeScript("arguments[0].scrollTop = arguments[1];",
				driver.findElement(By.xpath("//*[@id='inspire']/div[7]")), 5000);

		List<WebElement> timeListBox = driver
				.findElements(By.xpath(".//*[@role='listbox']/div[@role='option']/div/div"));
		for (WebElement time : timeListBox) {
			if (time.getText().equals(timeToBeSelected)) {
				Actions builder = new Actions(driver);
				builder.moveToElement(time).perform();
				time.click();
				break;
			}
		}
	}

	public void selectTimeForDevOps(String timeToBeSelected) throws InterruptedException {
		timeTakenField.click();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollTop = arguments[1];",
				driver.findElement(By.xpath("//*[@id='inspire']/div[8]")), 1000);
		js.executeScript("arguments[0].scrollTop = arguments[1];",
				driver.findElement(By.xpath("//*[@id='inspire']/div[8]")), 5000);

		List<WebElement> timeListBox = driver
				.findElements(By.xpath(".//*[@role='listbox']/div[@role='option']/div/div"));
		for (WebElement time : timeListBox) {
			if (time.getText().equals(timeToBeSelected)) {
				Actions builder = new Actions(driver);
				builder.moveToElement(time).perform();
				time.click();
				break;
			}
		}
	}

	public void enterTaskDescription(String taskDescription) {
		taskDescriptionField.type(taskDescription);
	}

	public void clickSaveButton() {
		Actions builder = new Actions(driver);
		builder.moveToElement(saveButton.getElement()).perform();
		saveButton.click();
	}

	public ExtendedWebElement getTaskIdField() {
		return taskIdField;
	}

	public ExtendedWebElement getTaskTitle() {
		return taskTitle;
	}

	public ExtendedWebElement getOriginalEstimate() {
		return originalEstimate;
	}

	public ExtendedWebElement getCompletedTime() {
		return completedTime;
	}

	public ExtendedWebElement getRemainingTime() {
		return remainingTime;
	}

	public ExtendedWebElement getTaskDateField() {
		return taskDateField;
	}

	public ExtendedWebElement getTimeTakenField() {
		return timeTakenField;
	}

	public ExtendedWebElement getTaskDescriptionField() {
		return taskDescriptionField;
	}

	public ExtendedWebElement getSuccessAlertOnAddingTimesheet() {
		return successAlertOnAddingTimesheet;
	}

	public ExtendedWebElement getProjectNameField() {
		return projectNameField;
	}

}
