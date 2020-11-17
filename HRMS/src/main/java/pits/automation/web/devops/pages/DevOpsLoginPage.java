package pits.automation.web.devops.pages;

import org.openqa.selenium.WebDriver;

import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.gui.AbstractPage;

public class DevOpsLoginPage extends AbstractPage {

	public DevOpsLoginPage(WebDriver driver) {
		super(driver);

	}

	public DevOpsLandingPage devopsEmployeeLogin() {
		String devOpsUrl = "http://" + R.TESTDATA.get("devopsEmployeeLogin.userName") + ":"
				+ R.TESTDATA.get("devopsEmployeeLogin.password") + "@" + "pitsdevops.pitsolutions.com";
		driver.get(devOpsUrl);
		return new DevOpsLandingPage(driver);
	}

}
