package pits.automation.web.hrms.manager.pages;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

public class ManagerTaskDetailsMenuBox extends AbstractUIObject {

	@FindBy(xpath = "//div[4]/div/div[2]/button")
	private ExtendedWebElement approveTaskButton;

	@FindBy(xpath = ".//*[@data-test-id='btnReject']")
	private ExtendedWebElement rejectTaskButton;

	@FindBy(xpath = ".//*[@id='firstRow']/div/div[1]")
	private ExtendedWebElement rejectConfirmation;

	@FindBy(xpath = ".//*[@id='firstRow']/div/div/button[1]")
	private ExtendedWebElement noButton;

	@FindBy(xpath = ".//*[@id='firstRow']/div/div/button[2]")
	private ExtendedWebElement yesButton;

	@FindBy(xpath = ".//*[@class='pl-1']")
	private List<ExtendedWebElement> timeSheetEntriesList;

	@FindBy(xpath = ".//*[@class='v-card__text']/span/div")
	private ExtendedWebElement taskDetails;

	public ManagerTaskDetailsMenuBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);

	}

	public void clickApproveTaskButton() {
		if (approveTaskButton.isVisible()) {
			approveTaskButton.click();
		} else {
			fail("Approve button is not available for the particular task");
		}
	}

	public void clickRejectTaskButton() {
		if (rejectTaskButton.isVisible()) {
			rejectTaskButton.click();
		} else {
			fail("Reject button is not available for the particular task");
		}
	}

	public RejectTaskBox clickYesButton() {
		yesButton.click();
		return new RejectTaskBox(driver, driver);
	}

	public void verifytheTaskDetailsDisplayedForManager() {
		if (!taskDetails.isVisible()) {
			fail("Task Details are not displayed for the manager");
		}

		if (!approveTaskButton.isVisible()) {
			fail("Task Approve Button is not displayed for the manager");
		}

		if (!rejectTaskButton.isVisible()) {
			fail("Task Reject Button is not displayed for the manager");
		}

	}
}
