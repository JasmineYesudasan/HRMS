package pits.automation.web.hrms.manager.pages;

import static org.junit.jupiter.api.Assertions.fail;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

public class RejectTaskBox extends AbstractUIObject {

	@FindBy(xpath = ".//*[@data-test-id='txtReason']")
	private ExtendedWebElement reasonToRejectField;

	@FindBy(xpath = ".//*[@id='btnSave']")
	private ExtendedWebElement submitButton;

	@FindBy(xpath = ".//*[@id='btnClose']")
	private ExtendedWebElement closeButton;

	@FindBy(xpath = ".//*[@role='alert']")
	private ExtendedWebElement mandatoryAlert;

	public RejectTaskBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);

	}

	public void provideReasontoRejectTheTask(String reasonToReject) {
		if (reasonToRejectField.isVisible()) {
			reasonToRejectField.type(reasonToReject);
		} else {
			fail("The field to provide the reason to reject is not visible.");
		}

	}

	public void clickSubmitButton() {
		submitButton.click();
	}

	public ExtendedWebElement getMandatoryAlert() {
		return mandatoryAlert;
	}
}
