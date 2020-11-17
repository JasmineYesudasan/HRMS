package pits.automation.web.devops.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

public class WorkItemsBox extends AbstractUIObject {

	@FindBy(xpath = ".//*[@class='add-new-item board-add-card']")
	private ExtendedWebElement addWorkItemButton;

	@FindBy(xpath = ".//span[@class='text'][@role='button']")
	private List<ExtendedWebElement> workItemMenuList;

	@FindBy(xpath = ".//*[@class='title ellipsis']/a/span")
	private List<ExtendedWebElement> workItemList;

	@FindBy(xpath = ".//*[@class='clickable-title']")
	private List<ExtendedWebElement> taskList;

	@FindBy(xpath = ".//*[@class='identity-picker-resolved-name']")
	private List<ExtendedWebElement> assignedToName;

	@FindBy(xpath = "(.//*[@class='witState'])[1]")
	private ExtendedWebElement workItemBar;

	public WorkItemsBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);
	}

	public void clickAddWorkItemButton() {
		addWorkItemButton.scrollTo();
		addWorkItemButton.click();
	}

	public void selectWorkItem(String requiredWorkItem) {

		List<WebElement> workItemMenuList = driver.findElements(By.xpath(".//span[@class='text'][@role='button']"));
		System.out.println(workItemMenuList.size());
		for (WebElement workItem : workItemMenuList) {
			if (workItem.getText().equalsIgnoreCase(requiredWorkItem)) {
				workItem.click();
				break;
			}
		}
	}

	public void enterTaskTitle(String requiredTaskTitle) throws InterruptedException {

		WebElement taskTitleField = driver.findElement(By.xpath(".//*[@class='editableTitle']/textarea"));

		taskTitleField.sendKeys(requiredTaskTitle);
		Thread.sleep(1000);
		workItemBar.click();
		Thread.sleep(2000);

	}

	public void fetchWorkItemsAssigned() {
		System.out.println(workItemList.size());
		for (ExtendedWebElement workItem : workItemList) {

			System.out.println(workItem.getText());
		}
	}

	public DevOpsTaskDetailsBox selectTheRequiredTask(String requiredTask) {
		for (ExtendedWebElement task : taskList) {
			if (task.getText().equals(requiredTask)) {
				task.click();
			}
		}
		return new DevOpsTaskDetailsBox(driver, driver);
	}

	public int fetchTheCountOfTasksAssignedForTheEmployee(String requiredAssignee) {
		List<String> assignedList = new ArrayList<>();
		int countOfTasksAssigned = 0;
		for (ExtendedWebElement assignee : assignedToName) {
			if (assignee.getText().equalsIgnoreCase(requiredAssignee)) {
				assignedList.add(assignee.getText());
			}
		}
		countOfTasksAssigned = assignedList.size();
		if (countOfTasksAssigned == 0) {
			System.out.println("There are no tasks assigned for the particular employee in devops.");
		}
		return countOfTasksAssigned;
	}

	public ExtendedWebElement getAddWorkItemButton() {
		return addWorkItemButton;
	}

}
