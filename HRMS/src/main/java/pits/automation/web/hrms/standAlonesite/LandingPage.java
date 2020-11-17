package pits.automation.web.hrms.standAlonesite;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractPage;

public class LandingPage extends AbstractPage {

	@FindBy(xpath = ".//*[@data-test-id='aLogout']")
	private ExtendedWebElement logoutButton;

	public LandingPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public ExtendedWebElement getLogoutButton() {
		return logoutButton;
	}

}
