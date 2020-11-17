package pits.automation.web.hrms.employee.pages;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

public class RemoveConfirmationPopUp extends AbstractUIObject {

	@FindBy(xpath = "//*[@id='app']/div[4]/div/div")
	private ExtendedWebElement removeConfirmationPopUp;

	@FindBy(xpath = "//*[@id='app']/div[4]/div/div/div[1]")
	private ExtendedWebElement removeConfirmationMessage;

	@FindBy(xpath = ".//*[@data-test-id='btnRemoveNo']")
	private ExtendedWebElement noButton;

	@FindBy(xpath = ".//*[@data-test-id='btnRemoveYes']")
	private ExtendedWebElement yesButton;

	public RemoveConfirmationPopUp(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);

	}

	public ExtendedWebElement getRemoveConfirmationMessage() {
		return removeConfirmationMessage;
	}

	public void clickYesButton() {
		yesButton.click();
	}

}
