package pits.automation.web.hrms.employee.pages;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

import pits.automation.web.hrms.employee.devops.pages.DevopsTaskDetailsBox;

public class AddNewTimeSheetBox extends AbstractUIObject {

	@FindBy(xpath = ".//*[@id='txtDate']")
	private ExtendedWebElement taskDateField;

	@FindBy(xpath = ".//*[@id='txtTaskId']")
	private ExtendedWebElement taskIdField;

	@FindBy(xpath = "//*[@id='inspire']/div[3]/div/div/div[3]/div/form/div[5]/div/div[1]/div[1]")
	private ExtendedWebElement timeTakenField;

	@FindBy(xpath = ".//*[@role='listbox']/div[@role='option']/div/div")
	private List<ExtendedWebElement> timeListBox;

	@FindBy(xpath = ".//*[@id='txaDescription']")
	private ExtendedWebElement taskDescriptionField;

	@FindBy(xpath = ".//*[@id='btnSave']")
	private ExtendedWebElement saveButton;

	@FindBy(xpath = ".//*[@data-test-id='btnDevops']")
	private ExtendedWebElement devOpsIcon;

	@FindBy(xpath = ".//*[@data-test-id='txtTaskTitle']")
	private ExtendedWebElement taskTitleField;

	@FindBy(xpath = "//*[@id='inspire']/div[3]/div/div/div[3]/div/form/div[1]/div/div/div/label")
	private ExtendedWebElement labelOfFirstFieldInAddPopUp;

	@FindBy(xpath = "//*[@id='inspire']/div[3]/div/div/div[3]/div/form/div/div/div/div/label")
	private List<ExtendedWebElement> labelsOfFieldsInAddPopUp;

	public AddNewTimeSheetBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);

	}

	public DevopsTaskDetailsBox clickDevOpsIcon() {
		if (devOpsIcon.isVisible()) {
			devOpsIcon.click();
		} else {
			fail("DevOps Icon is not available in the add timesheet pop up of the employee");
		}

		return new DevopsTaskDetailsBox(driver, driver);
	}

	public DatePickerForAddingTimesheet clickTaskDateField() {
		taskDateField.click();
		return new DatePickerForAddingTimesheet(driver, driver);
	}

	public void enterTaskID(String taskId) {
		taskIdField.type(taskId);
	}

	public void selectTime(String timeToBeSelected) throws InterruptedException {
		timeTakenField.click();
		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("arguments[0].scrollTop = arguments[1];",
				driver.findElement(By.xpath("//*[@id='inspire']/div[5]")), 10000);

		js.executeScript("arguments[0].scrollTop = arguments[1];",
				driver.findElement(By.xpath("//*[@id='inspire']/div[5]")), 10000);

		List<WebElement> timeListBox = driver
				.findElements(By.xpath(".//*[@role='listbox']/div[@role='option']/div/div"));

		for (WebElement time : timeListBox) {
			Actions builder1 = new Actions(driver);
			builder1.moveToElement(time).perform();
		}

		for (WebElement time : timeListBox) {
			System.out.println(timeListBox.size());
			if (time.getText().equals(timeToBeSelected)) {
				time.click();
				break;
			}
		}
	}

	public void verifyErrorInFieldLabels() {
		for (ExtendedWebElement label : labelsOfFieldsInAddPopUp) {
			if (!(label.getText().equals("Task Description") || label.getText().equals("Time Taken"))) {
				Assert.assertTrue(label.getAttribute("class").contains("error--text"),
						"The field : " + label + " is not a mandatory field.");
			}

		}
	}

	public void enterTaskTitle(String requiredTaskTitle) {
		taskTitleField.type(requiredTaskTitle);
	}

	public void enterTaskDescription(String taskDescription) {
		taskDescriptionField.type(taskDescription);
	}

	public void clickSaveButton() {
		saveButton.click();
	}

	public ExtendedWebElement getTaskTitleField() {
		return taskTitleField;
	}

	public ExtendedWebElement getlabelOfFirstFieldInAddPopUp() {
		return labelOfFirstFieldInAddPopUp;
	}
}
