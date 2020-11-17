package pits.automation.web.devops.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.gui.AbstractPage;

public class TaskBoardLandingPage extends AbstractPage {

	@FindBy(xpath = ".//*[@class='taskboard-wrapper']")
	private WorkItemsBox workItemsBox;

	@FindBy(xpath = ".//*[@class='vss-PivotBar--bar tall']")
	private PivotBarBox pivotBarBox;

	public TaskBoardLandingPage(WebDriver driver) {
		super(driver);
	}

	public WorkItemsBox getWorkItemsBox() {
		return workItemsBox;
	}

	public PivotBarBox getPivotBarBox() {
		return pivotBarBox;
	}

}
