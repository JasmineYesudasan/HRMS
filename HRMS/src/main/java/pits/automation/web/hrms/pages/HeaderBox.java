package pits.automation.web.hrms.pages;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

import pits.automation.web.hrms.manager.pages.ManagerLandingPage;

public class HeaderBox extends AbstractUIObject {

	@FindBy(xpath = "//*[@id='app']/div/header")
	private ExtendedWebElement headerBox;

	@FindBy(xpath = ".//*[@data-test-id='aTimesheet']")
	private ExtendedWebElement employeeButton;

	@FindBy(xpath = ".//*[@data-test-id='aTimesheetApproval']")
	private ExtendedWebElement managerButton;

	public HeaderBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);

	}

	public ManagerLandingPage clickManagerButton() {
		managerButton.click();
		return new ManagerLandingPage(driver);
	}

	public ExtendedWebElement getEmployeeButton() {
		return employeeButton;
	}

	public ExtendedWebElement getManagerButton() {
		return managerButton;
	}

}
