package pits.automation.web.hrms.standAlonesite;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractPage;

public class LoginPageStandAlone extends AbstractPage {

	@FindBy(xpath = ".//*[@name='militaryId']")
	private ExtendedWebElement militaryIdField;

	@FindBy(xpath = ".//*[@name='password']")
	private ExtendedWebElement passwordField;

	@FindBy(xpath = ".//*[@type='button']/span")
	private ExtendedWebElement loginButton;

	@FindBy(xpath = ".//*[@class='lblColor']")
	private ExtendedWebElement errorMessage;

	public LoginPageStandAlone(WebDriver driver) {
		super(driver);
	}

	public LandingPage employeeValidLogin(String url, String requiredMilitaryId, String requiredPassword) {
		driver.get(url);
		militaryIdField.type(requiredMilitaryId);
		passwordField.type(requiredPassword);
		loginButton.click();
		return new LandingPage(driver);
	}

	public void launchTheUrl(String url) {
		driver.get(url);
	}

	public ExtendedWebElement getErrorMessage() {
		return errorMessage;
	}

	public ExtendedWebElement getLoginButton() {
		return loginButton;
	}

}
