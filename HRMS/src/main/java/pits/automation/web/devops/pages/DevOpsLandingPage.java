package pits.automation.web.devops.pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractPage;

public class DevOpsLandingPage extends AbstractPage {

	@FindBy(xpath = "/html/body/div/div/div/div/div/div/div/div/div/div/div/ul/li/a")
	private ExtendedWebElement projectCard;

	@FindBy(xpath = "/html/body/div/div/div/div/div/div/div/div/div/div/div/ul/li/a")
	private List<ExtendedWebElement> projectCardList;

	@FindBy(xpath = ".//a[@role='menuitem']")
	private List<ExtendedWebElement> menuItems;

	@FindBy(xpath = "//span[contains(text(),'Boards')]")
	private ExtendedWebElement boardsLink;

	@FindBy(xpath = "//span[contains(text(),'Sprints')]")
	private ExtendedWebElement sprintsLink;

	@FindBy(xpath = ".//*[@class='ms-FocusZone']")
	private ProjectCardBox projectCardBox;

	public DevOpsLandingPage(WebDriver driver) {
		super(driver);
		setPageURL("http://pitsdevops.pitsolutions.com/DefaultCollection");
	}

	public ExtendedWebElement getProjectCard() {
		return projectCard;
	}

	public TaskBoardLandingPage navigateToTaskBoard() {
		boardsLink.click();
		sprintsLink.click();

		return new TaskBoardLandingPage(driver);

	}

	public ProjectCardBox getProjectCardBox() {
		return projectCardBox;
	}

}