package pits.automation.web.login;

import com.qaprosoft.carina.core.foundation.AbstractTest;

import pits.automation.web.hrms.pages.LoginPage;
import pits.automation.web.hrms.pages.TimesheetLandingPage;

public class BaseTest extends AbstractTest {

	public TimesheetLandingPage employeeLogin(String militaryId, String password) {
		LoginPage loginPage = new LoginPage(getDriver());
		loginPage.open();
		loginPage.refresh();
		return loginPage.login(militaryId, password);

	}

	public TimesheetLandingPage managerLogin(String requiredMilitaryId, String password) {
		LoginPage loginPage = new LoginPage(getDriver());
		loginPage.open();
		loginPage.refresh();
		return loginPage.login(requiredMilitaryId, password);

	}

	public void launchHRMS() {
		LoginPage loginPage = new LoginPage(getDriver());
		loginPage.open();
	}

	/*
	 * public TimesheetLandingPage adEmployeeLogin(String adUserName, String
	 * password) throws InterruptedException { LoginPage loginPage = new
	 * LoginPage(getDriver()); loginPage.open(); Thread.sleep(4000); return
	 * loginPage.adEmployeeLogin(adUserName, password);
	 * 
	 * }
	 */

	/*
	 * public TimesheetLandingPage adEmployeeLogin(String adUserName, String
	 * password) { String loginUrl = "http://" + adUserName + ":" + password + "@" +
	 * "10.10.100.147:5000/#/"; getDriver().get(loginUrl); Alert alert =
	 * getDriver().switchTo().alert(); alert.accept(); return new
	 * TimesheetLandingPage(getDriver()); }
	 */
}
