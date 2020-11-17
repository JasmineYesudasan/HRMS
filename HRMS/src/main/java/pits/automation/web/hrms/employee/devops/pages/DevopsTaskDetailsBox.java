package pits.automation.web.hrms.employee.devops.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

public class DevopsTaskDetailsBox extends AbstractUIObject {

	@FindBy(xpath = "(//tbody)[2]/tr")
	private List<ExtendedWebElement> taskList;

	@FindBy(xpath = ".//*[@data-test-id='btnSelect']")
	private ExtendedWebElement selectButton;

	@FindBy(xpath = "//*[@id='devopsTaskDiv']/div/div[4]/div/div/div[2]/div[1]/div/div/div/div[1]")
	private ExtendedWebElement rowsSelectOption;

	@FindBy(xpath = ".//div[@role='listbox']/div[@role='option']")
	private List<ExtendedWebElement> rowOptionsList;

	@FindBy(xpath = "(//tbody)[2]/tr/td[1]")
	private List<ExtendedWebElement> taskIdList;

	@FindBy(xpath = "(//table)[2]/thead/tr/th/span")
	private List<ExtendedWebElement> headerElementsList;

	@FindBy(xpath = "(//tbody)[3]/tr/td[1]")
	private List<ExtendedWebElement> taskIdListForEditing;

	@FindBy(xpath = "(//tbody)[2]/tr/td[2]")
	private List<ExtendedWebElement> projectNameList;

	@FindBy(xpath = "(//tbody)[3]/tr/td[2]")
	private List<ExtendedWebElement> projectNameListOnGridEdit;

	@FindBy(xpath = "(//tbody)[1]/tr/td[2]")
	private List<ExtendedWebElement> projectNameListOnCalenderEdit;

	@FindBy(xpath = "//*[@id='devopsTaskDiv']/div/div[3]")
	private ProjectSelectorBox projectselectorBox;

	public DevopsTaskDetailsBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);
	}

	public DevOpsAddTimeSheetBox selectTheRequiredTaskForAddingTimeSheet(String requiredtaskId) {
		int rowNumber;
		List<String> taskIDListFetched = new ArrayList<>();
		for (ExtendedWebElement taskId : taskIdList) {
			taskIDListFetched.add(taskId.getText());
		}
		rowNumber = taskIDListFetched.indexOf(requiredtaskId) + 1;
		WebElement selectButton = driver.findElement(By.xpath("(//tbody)[2]/tr[" + rowNumber + "]/td[6]"));
		selectButton.click();
		return new DevOpsAddTimeSheetBox(driver, driver);
	}

	public DevOpsEditTimeSheetBox selectTheRequiredTaskForEditingTimeSheet(String requiredtaskId) {
		int rowNumber;
		List<String> taskIDListFetched = new ArrayList<>();
		for (ExtendedWebElement taskId : taskIdListForEditing) {
			taskIDListFetched.add(taskId.getText());
		}
		rowNumber = taskIDListFetched.indexOf(requiredtaskId) + 1;
		WebElement selectButton = driver.findElement(By.xpath("(//tbody)[3]/tr[" + rowNumber + "]/td[5]"));
		selectButton.click();
		return new DevOpsEditTimeSheetBox(driver, driver);
	}

	public boolean verifyTaskDetailsHeaderElement(String requiredHeader) {

		boolean isHeaderFound = false;
		for (ExtendedWebElement header : headerElementsList) {
			if (header.getText().equalsIgnoreCase(requiredHeader)) {
				isHeaderFound = true;
			}
		}
		return isHeaderFound;
	}

	public String verifyTaskTitle(String requiredTaskId) {

		List<String> taskIdListFetched = new ArrayList<>();
		for (ExtendedWebElement taskId : taskIdList) {
			taskIdListFetched.add(taskId.getText());
		}

		int rowNumber = taskIdListFetched.indexOf(requiredTaskId) + 1;
		WebElement taskTitle = driver.findElement(By.xpath("(//table)[2]/tbody/tr[" + rowNumber + "]/td[3]"));
		return taskTitle.getText();

	}

	public String verifyProjectName(String requiredTaskId) {

		List<String> taskIdListFetched = new ArrayList<>();
		for (ExtendedWebElement taskId : taskIdList) {
			taskIdListFetched.add(taskId.getText());
		}

		int rowNumber = taskIdListFetched.indexOf(requiredTaskId) + 1;
		WebElement projectName = driver.findElement(By.xpath("(//table)[2]/tbody/tr[" + rowNumber + "]/td[2]"));
		return projectName.getText();

	}

	public int fetchCountOfTasksDisplayed() {
		return taskIdList.size();
	}

	public void displayAllRows(String paginationToSelect) {
		rowsSelectOption.scrollTo();
		rowsSelectOption.click();

		for (ExtendedWebElement options : rowOptionsList) {
			if (options.getText().equals(paginationToSelect)) {
				options.click();
				break;
			}
		}
	}

	public List<String> fetchProjectNames() {
		List<String> projectNamesFetched = new ArrayList<>();

		for (ExtendedWebElement project : projectNameList) {
			projectNamesFetched.add(project.getText());
		}
		return projectNamesFetched;
	}

	public List<String> fetchProjectNamesOnGridEdit() {
		List<String> projectNamesFetched = new ArrayList<>();

		for (ExtendedWebElement project : projectNameListOnGridEdit) {
			projectNamesFetched.add(project.getText());
		}
		return projectNamesFetched;
	}

	public List<String> fetchProjectNamesOnCalenderEdit() {
		List<String> projectNamesFetched = new ArrayList<>();

		for (ExtendedWebElement project : projectNameListOnCalenderEdit) {
			projectNamesFetched.add(project.getText());
		}
		return projectNamesFetched;
	}

	public ProjectSelectorBox getProjectselectorBox() {
		return projectselectorBox;
	}

	public boolean verifyTheProjectNamesDisplayed(List<String> projectsInDropdownList, List<String> projectsDisplayed) {

		boolean found = false;
		for (String projectInDropdown : projectsInDropdownList) {
			for (String project : projectsDisplayed) {
				if (project.equals(projectInDropdown)) {
					found = true;
					break;
				}
			}
		}

		return found;

	}
}
