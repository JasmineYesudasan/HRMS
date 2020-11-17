package pits.automation.web.S2004;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qaprosoft.carina.core.foundation.AbstractTest;
import com.qaprosoft.carina.core.foundation.utils.R;

import pits.automation.web.hrms.standAlonesite.LandingPage;
import pits.automation.web.hrms.standAlonesite.LoginPageStandAlone;

public class Test_S2004_02 extends AbstractTest {

	@Test(priority = 0)
	public void verifyValidLogin() {

		LoginPageStandAlone loginPageStandAlone = new LoginPageStandAlone(getDriver());
		LandingPage landingpage = loginPageStandAlone.employeeValidLogin(R.TESTDATA.get("standAloneLogin.url"),
				R.TESTDATA.get("employeeLogin.militaryIdTwo"), R.TESTDATA.get("standAloneLogin.password"));
		Assert.assertTrue(landingpage.getLogoutButton().isVisible(), "Login is not successfull for the user :"
				+ R.TESTDATA.get("employeeLogin.militaryIdTwo") + ". Please verify the credentials");
		landingpage.getLogoutButton().click();

		Assert.assertTrue(loginPageStandAlone.getLoginButton().getElement().isDisplayed(),
				"Logout action is not successfull for the user : " + R.TESTDATA.get("employeeLogin.militaryIdTwo"));

	}

	@Test(priority = 1)
	public void verifyLoginWithInvalidUserName() {
		LoginPageStandAlone loginPageStandAlone = new LoginPageStandAlone(getDriver());

		// Login with Invalid UserName
		LandingPage landingpage = loginPageStandAlone.employeeValidLogin(R.TESTDATA.get("standAloneLogin.url"), "12345",
				R.TESTDATA.get("standAloneLogin.password"));
		Assert.assertTrue(loginPageStandAlone.getErrorMessage().isVisible(),
				"The required error message on providing invalid username is not displayed.");

		Assert.assertTrue(loginPageStandAlone.getErrorMessage().isVisible(),
				"The required error message on providing invalid username is not displayed.");

		Assert.assertTrue(
				loginPageStandAlone.getErrorMessage().getText().equalsIgnoreCase("Invalid military id or password"),
				"The error message displayed on providing invalid username is not as expected. Expected Message : Invalid military id or password. Message Displayed : "
						+ loginPageStandAlone.getErrorMessage().getText());

		Assert.assertFalse(landingpage.getLogoutButton().isVisible(),
				"The login was successfull for the invalid user : 12345");

		// Login with invalid password

		loginPageStandAlone.refresh();
	}

	@Test(priority = 2)
	public void verifyLoginWithInvalidPassword() {
		LoginPageStandAlone loginPageStandAlone = new LoginPageStandAlone(getDriver());

		// Login with Invalid UserName
		LandingPage landingpage = loginPageStandAlone.employeeValidLogin(R.TESTDATA.get("standAloneLogin.url"),
				R.TESTDATA.get("employeeLogin.militaryIdTwo"), "12345");
		Assert.assertTrue(loginPageStandAlone.getErrorMessage().isVisible(),
				"The required error message on providing invalid password is not displayed.");

		Assert.assertTrue(loginPageStandAlone.getErrorMessage().isVisible(),
				"The required error message on providing invalid password is not displayed.");

		Assert.assertTrue(
				loginPageStandAlone.getErrorMessage().getText().equalsIgnoreCase("Invalid military id or password"),
				"The error message displayed on providing invalid password is not as expected. Expected Message : Invalid military id or password. Message Displayed : "
						+ loginPageStandAlone.getErrorMessage().getText());

		Assert.assertFalse(landingpage.getLogoutButton().isVisible(), "The login was successfull for the user : "
				+ R.TESTDATA.get("employeeLogin.militaryIdTwo") + " with invalid password.");

	}

	@Test(priority = 3)
	public void verifyLoginWithBlankSubmitOfUserNameAndPassword() {
		LoginPageStandAlone loginPageStandAlone = new LoginPageStandAlone(getDriver());

		// Login with Invalid UserName
		loginPageStandAlone.launchTheUrl(R.TESTDATA.get("standAloneLogin.url"));
		loginPageStandAlone.getLoginButton().click();

		Assert.assertTrue(loginPageStandAlone.getErrorMessage().isVisible(),
				"The required error message on blank submit is not displayed.");

		Assert.assertTrue(loginPageStandAlone.getErrorMessage().isVisible(),
				"The required error message on blank submit is not displayed.");

		Assert.assertTrue(
				loginPageStandAlone.getErrorMessage().getText().equalsIgnoreCase("Invalid military id or password"),
				"The error message displayed on blank submit is not as expected. Expected Message : Invalid military id or password. Message Displayed : "
						+ loginPageStandAlone.getErrorMessage().getText());

	}

}