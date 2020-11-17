package pits.automation.web.hrms.employee.devops.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

import pits.automation.web.hrms.employee.pages.DatePickerForEditingTimeSheet;

public class DevOpsEditTimeSheetBox extends AbstractUIObject {

	@FindBy(xpath = ".//*[@data-test-id='txtDate']")
	private ExtendedWebElement taskDateField;

	@FindBy(xpath = "//div/form/div[4]/div/div[1]/div[1]/div[1]")
	private ExtendedWebElement timeTakenField;

	@FindBy(xpath = ".//*[@data-test-id='txaDescription']")
	private ExtendedWebElement timeDescriptionField;

	@FindBy(xpath = ".//*[@data-test-id='btnUpdate']")
	private ExtendedWebElement updateButton;

	@FindBy(xpath = ".//*[@role='listbox']/div[@role='option']")
	private List<ExtendedWebElement> timeOptionsList;

	public DevOpsEditTimeSheetBox(WebDriver driver, SearchContext searchContext) {
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
				driver.findElement(By.xpath(".//*[@id='app']/div[10]")), 5000);
		js.executeScript("arguments[0].scrollTop = arguments[1];",
				driver.findElement(By.xpath(".//*[@id='app']/div[10]")), 5000);

		for (ExtendedWebElement time : timeOptionsList) {
			if (time.getText().equals(timeToBeSelected)) {
				time.click();
				break;
			}
		}
	}

	public void editTaskDescription(String editedDescription) {
		timeDescriptionField.getElement().sendKeys(Keys.CONTROL + "a");
		timeDescriptionField.getElement().sendKeys(Keys.DELETE);
		timeDescriptionField.type(editedDescription);
	}

	public void clickUpdateButton() {
		updateButton.click();
	}

}
