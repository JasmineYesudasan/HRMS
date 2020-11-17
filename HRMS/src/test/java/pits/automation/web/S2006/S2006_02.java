package pits.automation.web.S2006;

import java.awt.AWTException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qaprosoft.carina.core.foundation.utils.R;

import pits.automation.web.hrms.pages.LoginPage;
import pits.automation.web.login.BaseTest;

public class S2006_02 extends BaseTest {

	@Test(priority = 1)
	public void verifyLoginWithMilitaryIdAndPassword() throws InterruptedException {
		LoginPage loginPage = new LoginPage(getDriver());
		launchHRMS();
		loginPage.refresh();
		loginPage.clickLoginButton();

		Assert.assertTrue(loginPage.getErrorMsg().isVisible(), "Error Message is not displayed on blank submit.");

		Assert.assertTrue(loginPage.getErrorMsg().getText().equals("* Enter mandatory fields"),
				"The error message on blank submit is not as expected. The text displayed is :"
						+ loginPage.getErrorMsg().getText());

		loginPage.refresh();

		loginPage.LoginWithOnlyMilitaryId(R.TESTDATA.get("employeeLogin.militaryIdTwo"));

		Assert.assertTrue(loginPage.getErrorMsg().isVisible(), "Error Message is not displayed on blank submit.");

		Assert.assertTrue(loginPage.getErrorMsg().getText().equals("* Enter mandatory fields"),
				"The error message on blank password is not as expected. The text displayed is :"
						+ loginPage.getErrorMsg().getText());

		loginPage.refresh();

		loginPage.loginWithInvalidMilitaryID("12345", R.TESTDATA.get("employeeLogin.password"));
		Assert.assertTrue(loginPage.getErrorMsg().isVisible(), "Error Message is not displayed invalid military id.");

		Assert.assertTrue(loginPage.getErrorMsg().getText().equals("Invalid military id or password"),
				"The error message with invalid military id is not as expected. The text displayed is :"
						+ loginPage.getErrorMsg().getText());

		loginPage.refresh();

		loginPage.loginWithInvalidPassword(R.TESTDATA.get("employeeLogin.militaryIdTwo"), "12345");
		Assert.assertTrue(loginPage.getErrorMsg().isVisible(), "Error Message is not displayed for invalid password");

		Assert.assertTrue(loginPage.getErrorMsg().getText().equals("Invalid military id or password"),
				"The error message with invalid password is not as expected. The text displayed is :"
						+ loginPage.getErrorMsg().getText());

		Thread.sleep(3000);
	}

	@Test()
	public void verifyAdLogin() throws InterruptedException, AWTException {
		LoginPage loginPage = new LoginPage(getDriver());
		launchHRMS();
		Thread.sleep(3000);
		loginPage.loginAsAdUser();

	}
}
