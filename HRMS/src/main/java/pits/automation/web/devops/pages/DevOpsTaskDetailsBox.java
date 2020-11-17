package pits.automation.web.devops.pages;

import java.util.List;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

public class DevOpsTaskDetailsBox extends AbstractUIObject {

	@FindBy(xpath = ".//*[@class='identity-picker-watermark-name text-cursor']")
	private ExtendedWebElement assigneeDropDown;

	@FindBy(xpath = ".//*[@class='title large']/span")
	private List<ExtendedWebElement> assigneeDropDownOptions;

	@FindBy(xpath = ".//*[contains(text(),'Save & Close')]")
	private ExtendedWebElement saveAndCloseButton;

	@FindBy(xpath = ".//*[@class='work-item-form-headerContent']/div[2]/span")
	private ExtendedWebElement taskIdField;

	@FindBy(xpath = "(.//*[@class='tfs-collapsible-header wit-form-group-header'])[4]")
	private ExtendedWebElement effortDropDown;

	@FindBy(xpath = "(.//*[@class='control']/div/div/div/input[@type='text'])[3]")
	private ExtendedWebElement originalEstimateField;

	@FindBy(xpath = "(.//*[@class='control']/div/div/div/input[@type='text'])[4]")
	private ExtendedWebElement remainingEstimateField;

	@FindBy(xpath = "(.//*[@class='control']/div/div/div/input[@type='text'])[5]")
	private ExtendedWebElement completedTimeField;

	@FindBy(xpath = ".//*[@class='ui-button-icon-primary ui-icon ui-icon-closethick']")
	private ExtendedWebElement closeButton;

	public DevOpsTaskDetailsBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);
	}

	public ExtendedWebElement getOriginalEstimateField() {
		return originalEstimateField;
	}

	public ExtendedWebElement getRemainingEstimateField() {
		return remainingEstimateField;
	}

	public void selectAssigneeForTheTask(String requiredUser) throws InterruptedException {
		assigneeDropDown.click();
		Thread.sleep(3000);
		for (ExtendedWebElement option : assigneeDropDownOptions) {
			if (option.getText().equalsIgnoreCase(requiredUser)) {
				option.click();
			}
		}

	}

	public void clickEffortDropdown() {
		effortDropDown.click();
	}

	public void provideTheEstimates(String originalEstimate, String remainingEstimate, String completedTime) {
		originalEstimateField.type(originalEstimate);
		remainingEstimateField.type(remainingEstimate);
		completedTimeField.type(completedTime);
	}

	public String calculateRequiredRemainingTime(String timeAdded, String remainingTimeAsString) {
		int minutesOfTimeAdded = Integer.parseInt(timeAdded.substring(timeAdded.lastIndexOf(":") + 1));

		int minutesOfRemainingTime = Integer
				.parseInt(remainingTimeAsString.substring(remainingTimeAsString.lastIndexOf(".") + 1));

		int hoursOfTimeAdded = Integer.parseInt(timeAdded.substring(0, timeAdded.indexOf(":")));
		int hoursofRemainingTime = Integer
				.parseInt(remainingTimeAsString.substring(0, remainingTimeAsString.indexOf(".")));

		int finalHours = hoursofRemainingTime - hoursOfTimeAdded;

		int finalMinutes = minutesOfRemainingTime - minutesOfTimeAdded;

		if (finalMinutes < 0) {

			finalMinutes = finalMinutes + 60;
			finalHours = finalHours - 1;
		}

		String finalTime = finalHours + "." + finalMinutes;

		return finalTime;
	}

	public String calculateRequiredCompletedTime(String timeAdded, String completedTimeAsString) {

		int minutesOfTimeAdded = Integer.parseInt(timeAdded.substring(timeAdded.lastIndexOf(":") + 1));

		int minutesOfCompletedTime = Integer
				.parseInt(completedTimeAsString.substring(completedTimeAsString.lastIndexOf(".") + 1));

		int hoursOfTimeAdded = Integer.parseInt(timeAdded.substring(0, timeAdded.indexOf(":")));

		int hoursofCompletedTime = Integer
				.parseInt(completedTimeAsString.substring(0, completedTimeAsString.indexOf(".")));

		int finalHours = hoursofCompletedTime + hoursOfTimeAdded;

		int finalMinutes = minutesOfTimeAdded + minutesOfCompletedTime;

		if (finalMinutes > 60) {

			finalMinutes = finalMinutes - 60;
			finalHours = finalHours + 1;
		}

		String finalTime = finalHours + "." + finalMinutes;
		return finalTime;
	}

	public void clickSaveAndCloseButton() {
		saveAndCloseButton.click();
	}

	public ExtendedWebElement getTaskIdField() {
		return taskIdField;
	}

	public ExtendedWebElement getCompletedTimeField() {
		return completedTimeField;
	}

	public ExtendedWebElement getCloseButton() {
		return closeButton;
	}

	public ExtendedWebElement getEffortDropDown() {
		return effortDropDown;
	}

}
