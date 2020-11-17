package pits.automation.web.hrms.manager.pages;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

public class TaskRejectConfirmationPopUp extends AbstractUIObject {

	@FindBy(xpath = ".//*[@id='firstRow']/div/div/button[1]")
	private ExtendedWebElement noButton;

	@FindBy(xpath = ".//*[@id='firstRow']/div/div/button[2]")
	private ExtendedWebElement yesButton;

	public TaskRejectConfirmationPopUp(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);

	}

	public RejectTaskBox clickYesButton() {
		yesButton.click();
		return new RejectTaskBox(driver, driver);
	}

}
