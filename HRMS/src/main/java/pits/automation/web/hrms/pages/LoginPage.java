package pits.automation.web.hrms.pages;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractPage;

public class LoginPage extends AbstractPage {

	@FindBy(xpath = ".//*[@data-test-id='txtMilitaryId']")
	private ExtendedWebElement militaryIdField;

	@FindBy(xpath = ".//*[@data-test-id='txtPassword']")
	private ExtendedWebElement passwordField;

	@FindBy(xpath = ".//*[@data-test-id='btnLogin']")
	private ExtendedWebElement loginButton;

	@FindBy(xpath = ".//*[@class='lblColor']")
	private ExtendedWebElement errorMessage;

	public LoginPage(WebDriver driver) {
		super(driver);

	}

	public TimesheetLandingPage login(String militaryId, String password) {
		militaryIdField.scrollTo();
		militaryIdField.type(militaryId);
		passwordField.type(password);
		loginButton.click();
		return new TimesheetLandingPage(getDriver());

	}

	public void loginWithInvalidMilitaryID(String militaryId, String password) {
		militaryIdField.scrollTo();
		militaryIdField.type(militaryId);
		passwordField.type(password);
		loginButton.click();
	}

	public void loginWithInvalidPassword(String militaryId, String password) {
		militaryIdField.scrollTo();
		militaryIdField.type(militaryId);
		passwordField.type(password);
		loginButton.click();
	}

	public void clickLoginButton() {
		loginButton.click();

	}

	public void LoginWithOnlyMilitaryId(String militaryId) {
		militaryIdField.scrollTo();
		militaryIdField.type(militaryId);
		loginButton.click();
	}

	public TimesheetLandingPage adEmployeeLogin() throws InterruptedException {
		String loginUrl = "http://" + R.TESTDATA.get("adLogin.employeeUserName") + ":"
				+ R.TESTDATA.get("adLogin.employeePassword") + "@" + "10.10.100.147:5000/#/";
		driver.get(loginUrl);
		return new TimesheetLandingPage(driver);

	}

	public void loginAsAdUser() throws AWTException, InterruptedException {

		Robot robot = new Robot();
		setClipboardData(R.TESTDATA.get("adLogin.employeeUserName"));
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.delay(3000);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);

		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);

		setClipboardData(R.TESTDATA.get("adLogin.employeePassword"));
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.delay(3000);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);

		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);

		Thread.sleep(5000);

	}

	private static void setClipboardData(String str) {
		StringSelection ss = new StringSelection(str);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
	}

	public ExtendedWebElement getErrorMsg() {
		return errorMessage;
	}

}
