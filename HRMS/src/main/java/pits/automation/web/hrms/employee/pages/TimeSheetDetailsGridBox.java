package pits.automation.web.hrms.employee.pages;

import static org.junit.jupiter.api.Assertions.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

public class TimeSheetDetailsGridBox extends AbstractUIObject {

	@FindBy(xpath = "//tbody/tr")
	private List<ExtendedWebElement> timeSheetTable;

	@FindBy(xpath = "//tbody/tr/td[1]")
	private List<ExtendedWebElement> timesheetTableDateColumn;

	@FindBy(xpath = "//tbody/tr/td[5]")
	private ExtendedWebElement expandButton;

	@FindBy(xpath = "(.//*[@class='text-start'])[3]")
	private ExtendedWebElement tableElement;

	@FindBy(xpath = "//thead/tr/th[1]")
	private ExtendedWebElement dateHeader;

	@FindBy(xpath = "//tbody/tr")
	private ExtendedWebElement timeSheetRow;

	@FindBy(xpath = "(//tbody)[2]/tr/td[7]/button")
	private ExtendedWebElement submitButton;

	@FindBy(xpath = "(//thead)[2]/tr/th[1]")
	private ExtendedWebElement taskTitleHeader;

	@FindBy(xpath = ".//*[@role='option']")
	private List<ExtendedWebElement> rowOptions;

	@FindBy(xpath = "//thead/tr/th")
	private List<ExtendedWebElement> headerElements;

	@FindBy(xpath = "(//thead/tr)[2]")
	private List<ExtendedWebElement> secondaryHeaderElementsList;

	@FindBy(xpath = "(//thead/tr)[2]/th")
	private List<ExtendedWebElement> taskDetailsHeaderList;

	@FindBy(xpath = "(//tbody)[2]/tr/td[5]")
	private List<ExtendedWebElement> taskStatusList;

	@FindBy(xpath = "(//tbody)[2]/tr")
	private List<ExtendedWebElement> taskDetailsRow;

	@FindBy(xpath = "(//tbody)[2]/tr/td[2]")
	private List<ExtendedWebElement> taskIdList;

	@FindBy(xpath = "//tbody/tr/td[1]")
	private List<ExtendedWebElement> taskDateList;

	@FindBy(xpath = "(.//*[@data-test-id='btnRemove'])[1]")
	private ExtendedWebElement firstRemoveTaskIcon;

	@FindBy(xpath = "//tbody/tr[1]/td[4]")
	private ExtendedWebElement noOfTasksOfFirstDate;

	@FindBy(xpath = "//tbody/tr[1]/td[1]")
	private ExtendedWebElement dateOfFirstEntry;

	@FindBy(xpath = "//div[@class='v-data-footer__select']/div/div/div[@role='button']")
	private ExtendedWebElement rowsSelectOption;

	@FindBy(xpath = "(//tbody)[2]/tr[1]")
	private ExtendedWebElement firtTimeSheetEntry;

	@FindBy(xpath = ".//*[@data-test-id='btnEdit']")
	private ExtendedWebElement editTaskButton;

	private List<WebElement> mainHeaderElementsList = driver.findElements(By.xpath("//thead/tr/th/span"));

	public TimeSheetDetailsGridBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);

	}

	public void displayAllRows() {

		rowsSelectOption.scrollTo();
		rowsSelectOption.click();
		List<WebElement> rowOptionsList = driver.findElements(By.xpath(".//div[@role='listbox']/div[@role='option']"));

		for (WebElement options : rowOptionsList) {
			if (options.getText().equals("All")) {
				options.click();
				break;
			}
		}
	}

	public boolean expandTimesheetOfRequiredDate(String dateToBeSelected) {

		List<WebElement> dateList = driver.findElements(By.xpath("//tbody/tr/td[1]"));
		ArrayList<String> dateTextList = new ArrayList<String>();
		boolean taskDatePresent = false;
		int rowNumber;
		for (WebElement date : dateList) {
			dateTextList.add(date.getText());
		}
		rowNumber = dateTextList.indexOf(dateToBeSelected);
		int index;
		for (index = 1; index <= dateTextList.size(); index++) {
			if (index == rowNumber + 1) {
				WebElement expandButton = driver.findElement(By.xpath("(//tbody/tr/td[5]/button)[" + index + "]"));
				if (expandButton.isDisplayed()) {
					taskDatePresent = true;
					Actions builder = new Actions(driver);
					builder.moveToElement(expandButton).perform();
					expandButton.click();
					break;
				}
			}
		}

		if (taskDatePresent == false) {
			fail("The date for which the timesheet was added/edited is not available in the task list of the employee");
		}
		return taskDatePresent;
	}

	public List<String> fetchTaskDetails() {

		List<WebElement> taskDetailsList = driver.findElements(By.xpath("(//tbody)[2]/tr"));
		List<String> taskDetailsFetched = new ArrayList<>();
		for (WebElement taskdetails : taskDetailsList) {
			taskDetailsFetched.add(taskdetails.getText());
		}

		return taskDetailsFetched;
	}

	public int fetchTheCountOfTasksForTheDate() {
		List<WebElement> taskDetailsList = driver.findElements(By.xpath("(//tbody)[2]/tr"));
		return taskDetailsList.size();

	}

	public boolean verifyIfFutureDate() throws ParseException {
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = new Date();
		Date selectedDate = sdformat.parse(R.TESTDATA.get("selectDate.dateselected"));
		System.out.println(currentDate);
		System.out.println(selectedDate);
		boolean isfutureDate = false;
		if (selectedDate.compareTo(currentDate) > 0) {
			isfutureDate = true;
		}
		return isfutureDate;
	}

	public WebElement verifyStatusOfAddedTimesheet(String requiredTitle) {
		List<WebElement> taskTitleList = driver.findElements(By.xpath("(//tbody)[2]/tr/td[1]"));
		List<String> titleFetched = new ArrayList<>();

		for (WebElement title : taskTitleList) {
			titleFetched.add(title.getText());
		}

		int rowNumber = titleFetched.indexOf(requiredTitle) + 1;
		WebElement statusOfTimesheetAdded = driver.findElement(By.xpath("(//tbody)[2]/tr[" + rowNumber + "]/td[5]"));

		return statusOfTimesheetAdded;

	}

	public boolean verifyTaskDetailsHeader(String requiredHeader) {
		boolean isRequiredHeaderFound = false;
		for (ExtendedWebElement title : taskDetailsHeaderList) {
			if (title.getText().equalsIgnoreCase(requiredHeader)) {
				isRequiredHeaderFound = true;
				break;
			}
		}
		return isRequiredHeaderFound;
	}

	public WebElement verifySubmitButton() {
		List<WebElement> taskDescriptionList = driver.findElements(By.xpath("(//tbody)[2]/tr/td[3]"));
		List<String> descriptionFetched = new ArrayList<>();

		for (WebElement description : taskDescriptionList) {
			descriptionFetched.add(description.getText());
		}

		int rowNumber = descriptionFetched.indexOf(R.TESTDATA.get("enterTaskDescription.description")) + 1;
		WebElement submitButton = driver.findElement(By.xpath("(//tbody)[2]/tr[" + rowNumber + "]/td[7]/button"));
		Actions builder = new Actions(driver);
		builder.moveToElement(submitButton).perform();
		return submitButton;
	}

	public ExtendedWebElement getTimeSheetRow() {
		return timeSheetRow;
	}

	public boolean verifyTimeSheetDetailsHeader(String requiredHeaderElement) {

		boolean requiredHeaderFound = false;
		for (WebElement headerOptions : mainHeaderElementsList) {
			if (headerOptions.getText().equals(requiredHeaderElement)) {
				requiredHeaderFound = true;
			}
		}
		return requiredHeaderFound;
	}

	public void verifyRequiredHeaderIsPresentInTaskDetails(String requiredHeader, String filterApplied)
			throws InterruptedException {
		List<WebElement> expandButtonList = driver.findElements(By.xpath("//tbody/tr/td[5]/button"));

		List<String> headerOptionsList = new ArrayList<>();
		for (WebElement expand : expandButtonList) {
			Actions builder = new Actions(driver);
			builder.moveToElement(expand).perform();
			expand.click();
			headerOptionsList.clear();
			for (ExtendedWebElement headerOptions : secondaryHeaderElementsList) {
				System.out.println(headerOptions.getText());
				if (filterApplied.equalsIgnoreCase("All") || filterApplied.equalsIgnoreCase("Rejected")) {
					Assert.assertTrue(headerOptions.getText().contains(requiredHeader), " The header " + requiredHeader
							+ " is not available on applying filter with " + filterApplied);
				} else {

					Assert.assertFalse(headerOptions.getText().contains(requiredHeader),
							"The header " + requiredHeader + " is available on applying filter with " + filterApplied);
				}
			}
		}
	}

	public int fetchStatusBeforeFilter(String requiredStatus) {
		List<WebElement> expandButtonList = driver.findElements(By.xpath("//tbody/tr/td[5]/button"));

		ArrayList<String> statusList = new ArrayList<String>();

		for (WebElement expand : expandButtonList) {
			Actions builder = new Actions(driver);
			builder.moveToElement(expand).perform();
			expand.click();
			for (ExtendedWebElement status : taskStatusList) {

				if (status.getText().equals(requiredStatus)) {
					statusList.add(status.getText());
				}

			}
		}

		int count = statusList.size();
		return count;
	}

	public boolean verifyTheStatusInTaskDetails(String requiredStatus) throws InterruptedException {
		List<WebElement> expandButtonList = driver.findElements(By.xpath("//tbody/tr/td[5]/button"));

		boolean requiredStatusFound = false;
		for (WebElement expand : expandButtonList) {
			Actions builder = new Actions(driver);
			builder.moveToElement(expand).perform();
			expand.click();
			displayAllRows();
			for (ExtendedWebElement status : taskStatusList) {
				System.out.println(status.getText());
				if (status.getText().equals(requiredStatus)) {
					requiredStatusFound = true;
				}
			}
		}
		return requiredStatusFound;

	}

	public String verifyTheStatusOFTaskInTheEmployeeTaskList(String taskId, String description, String timeTaken) {

		int rowNumber = 0;
		List<String> taskDetailsFetched = new ArrayList<>();
		for (ExtendedWebElement taskDetails : taskDetailsRow) {
			taskDetailsFetched.add(taskDetails.getText());
		}
		for (String taskDetails : taskDetailsFetched) {
			if (taskDetails.contains(taskId) && taskDetails.contains(description) && taskDetails.contains(timeTaken)) {
				rowNumber = taskDetailsFetched.indexOf(taskDetails) + 1;
				break;
			}
		}
		String statusOfTimesheetAdded = driver.findElement(By.xpath("(//tbody)[2]/tr[" + rowNumber + "]/td[5]"))
				.getText();
		return statusOfTimesheetAdded;
	}

	public String verifyRejectReasonIsDisplayedInTaskList(String taskId, String description, String timeTaken) {

		int rowNumber = 0;
		List<String> taskDetailsFetched = new ArrayList<>();

		for (ExtendedWebElement taskDetails : taskDetailsRow) {
			taskDetailsFetched.add(taskDetails.getText());
		}

		for (String taskDetails : taskDetailsFetched) {
			if (taskDetails.contains(taskId) && taskDetails.contains(description) && taskDetails.contains(timeTaken)) {
				System.out.println(taskDetails);
				rowNumber = taskDetailsFetched.indexOf(taskDetails) + 1;
			}
		}

		String rejectReason = driver.findElement(By.xpath("(//tbody)[2]/tr[" + rowNumber + "]/td[6]")).getText();

		return rejectReason;
	}

	public List<String> fetchTaskIdOfEmployeeTasksDisplayedInGridView() throws InterruptedException {
		List<WebElement> expandButtonList = driver.findElements(By.xpath("//tbody/tr/td[5]"));
		List<String> taskIdListFetched = new ArrayList<>();
		for (WebElement expandButton : expandButtonList) {
			Actions builder = new Actions(driver);
			builder.moveToElement(expandButton).perform();
			expandButton.click();
			displayAllRows();
			for (ExtendedWebElement taskId : taskIdList) {
				taskIdListFetched.add(taskId.getText());
			}
		}
		return taskIdListFetched;
	}

	public void sortByDate() {
		dateHeader.click();
	}

	public ExtendedWebElement getSubmitButton() {
		return submitButton;
	}

	public void verifyRemoveIconForEachTask() {
		List<WebElement> expandButtonList = driver.findElements(By.xpath("//tbody/tr/td[5]"));
		for (WebElement expandButton : expandButtonList) {
			Actions builder = new Actions(driver);
			builder.moveToElement(expandButton).perform();
			expandButton.click();
			displayAllRows();
			int index;
			for (index = 1; index <= taskDetailsRow.size(); index++) {
				WebElement statusOfTheTask = driver.findElement(By.xpath("(//tbody)[2]/tr[" + index + "]/td[5]"));
				WebElement removeButton = driver.findElement(By.xpath("(//tbody)[2]/tr[" + index + "]/td[9]/button"));
				if (statusOfTheTask.getText().equals("Pending")) {

					Assert.assertTrue(removeButton.isEnabled(),
							"Remove Button is not available for the task in pending status.");
				}

				else if (statusOfTheTask.getText().equals("Submitted") || statusOfTheTask.getText().equals("Approved")
						|| statusOfTheTask.getText().equals("Rejected")) {
					Assert.assertTrue(removeButton.getAttribute("disabled").equals("true"),
							"Remove button is available for tasks other than in Pending status.");
				}

			}

		}
	}

	public void verifyEditIconForEachTask() {
		List<WebElement> expandButtonList = driver.findElements(By.xpath("//tbody/tr/td[5]"));
		for (WebElement expandButton : expandButtonList) {
			Actions builder = new Actions(driver);
			builder.moveToElement(expandButton).perform();
			expandButton.click();
			displayAllRows();
			int index;
			for (index = 1; index <= taskDetailsRow.size(); index++) {
				System.out.println(taskDetailsRow.size());
				WebElement statusOfTheTask = driver.findElement(By.xpath("(//tbody)[2]/tr[" + index + "]/td[5]"));
				WebElement editButton = driver.findElement(By.xpath("(//tbody)[2]/tr[" + index + "]/td[8]/button"));
				if (statusOfTheTask.getText().equals("Pending")) {

					Assert.assertTrue(editButton.isDisplayed(),
							"Edit Button is not available for the task in pending status.");
				}

				else if (statusOfTheTask.getText().equals("Submitted") || statusOfTheTask.getText().equals("Approved")
						|| statusOfTheTask.getText().equals("Rejected")) {
					Assert.assertFalse(editButton.isDisplayed(),
							"Edit button is available for tasks other than in Pending status.");
				}

			}

		}
	}

	public EmployeeEditTimeSheetBox clickEditButtonOfRequiredTask(String requiredTaskId) {

		int rowNumber = 0;
		List<String> taskIdlist = new ArrayList<>();
		for (ExtendedWebElement taskId : taskIdList) {
			taskIdlist.add(taskId.getText());
		}
		rowNumber = taskIdlist.indexOf(requiredTaskId) + 1;
		WebElement editButton = driver.findElement(By.xpath("(//tbody)[2]/tr[" + rowNumber + "]/td[8]/button"));
		editButton.click();

		return new EmployeeEditTimeSheetBox(driver, driver);

	}

	public int fetchTheCountOfTasksInFirstDate() {
		String count = noOfTasksOfFirstDate.getText();
		int countOfTasks = Integer.parseInt(count);
		return countOfTasks;
	}

	public void expandTheTaskDetails() {
		expandButton.click();

	}

	public RemoveConfirmationPopUp clickRemoveIcon() {

		firstRemoveTaskIcon.click();
		return new RemoveConfirmationPopUp(driver, driver);
	}

	public EmployeeEditTimeSheetBox clickEditTaskButton() {
		editTaskButton.click();
		return new EmployeeEditTimeSheetBox(driver, driver);
	}

	public ExtendedWebElement getDateOfFirstEntry() {
		return dateOfFirstEntry;
	}

	public ExtendedWebElement getFirtTimeSheetEntry() {
		return firtTimeSheetEntry;
	}

	public List<ExtendedWebElement> getTaskDateList() {
		return taskDateList;
	}

}
