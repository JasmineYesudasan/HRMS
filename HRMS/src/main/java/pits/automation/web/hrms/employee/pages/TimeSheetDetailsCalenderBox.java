package pits.automation.web.hrms.employee.pages;

import static org.junit.Assert.fail;

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

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

import pits.automation.web.hrms.manager.pages.ManagerTaskDetailsMenuBox;

public class TimeSheetDetailsCalenderBox extends AbstractUIObject {

	@FindBy(xpath = "/html/body/div/div/div[1]/main/div/div[2]/div[1]/div/div/div/div[3]/div/div/div/div[2]/div[1]/div/div/div/div")
	private List<ExtendedWebElement> timeSheetEntriesList;

	@FindBy(xpath = "(.//*[@class='pl-1'])[1]")
	private ExtendedWebElement timeSheetEntry;

	@FindBy(xpath = ".//div/div/div/div[@class='pl-1']")
	private List<ExtendedWebElement> taskList;

	@FindBy(xpath = ".//*[@class='col']")
	private ExtendedWebElement timeSheetBox;

	@FindBy(xpath = ".//*[@role='menu']")
	private ManagerTaskDetailsMenuBox taskDetailsMenuBox;

	@FindBy(xpath = ".//*[@role='menu']/div/div[2]/button")
	private ExtendedWebElement taskDetailsSubmitButton;

	@FindBy(xpath = ".//*[@class='v-calendar-weekly__day v-past']/div/div")
	private List<ExtendedWebElement> pastDateEntries;

	@FindBy(xpath = "(.//*[@class='v-calendar-weekly__day v-past']/div/div)[1]")
	private ExtendedWebElement firstEntryInPendingStatus;

	@FindBy(xpath = ".//*[@class='v-calendar-weekly__day v-future']/div/div")
	private List<ExtendedWebElement> futureDateEntries;

	@FindBy(xpath = ".//*[@class='v-calendar-weekly__day v-present']/div/div")
	private List<ExtendedWebElement> presentDayEntries;

	@FindBy(xpath = "//div[4]/div/header/div/button")
	private ExtendedWebElement taskDetailsMenuCloseButton;

	@FindBy(xpath = ".//*[@class='v-calendar-weekly__week']/div")
	private List<ExtendedWebElement> calenderDayBox;

	@FindBy(xpath = ".//*[@class='v-toolbar__title']")
	private ExtendedWebElement calenderMonthAndYear;

	@FindBy(xpath = "((//header)[2]/div/button)[1]")
	private ExtendedWebElement calenderBackwardButton;

	@FindBy(xpath = "((//header)[2]/div/button)[2]")
	private ExtendedWebElement calenderForwardButton;

	@FindBy(xpath = ".//*[@class='v-calendar-weekly__head']")
	private ExtendedWebElement calenderHead;

	@FindBy(xpath = "//*[@id='app']/div/div[2]/div/div[1]/div[2]/div[1]/div[1]/div")
	private List<ExtendedWebElement> daysDisplayedInCalender;

	@FindBy(xpath = ".//*[@class='v-calendar-weekly__day v-present']/div/div")
	private List<ExtendedWebElement> timeSheetEntriesForCurrentDay;

	@FindBy(xpath = ".//*[@class='holidayEvent']")
	private List<ExtendedWebElement> holidayEvents;

	@FindBy(xpath = "//*[@id='app']/div/div/div/div/div/div/div/div/div/button/span")
	private List<ExtendedWebElement> calenderDays;

	@FindBy(xpath = ".//*[@class='v-tooltip__content menuable__content__active']/span")
	private ExtendedWebElement taskToolTip;

	public TimeSheetDetailsCalenderBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);

	}

	public List<String> fetchBackgroudColourOfWeekends() {
		int indexOfRow;
		int weekendCount;

		List<String> backGroundColor = new ArrayList<>();

		for (indexOfRow = 2; indexOfRow <= 6; indexOfRow++) {
			for (weekendCount = 6; weekendCount <= 7; weekendCount++) {
				WebElement weekEnds = driver.findElement(By.xpath(
						"//*[@id='app']/div/div/div/div/div/div/div[" + indexOfRow + "]/div[" + weekendCount + "]"));
				backGroundColor.add(weekEnds.getCssValue("background-color"));
			}
		}
		return backGroundColor;
	}

	public void selectRequiredTaskFromCurrentDayEntries(String requiredTaskID) {
		for (ExtendedWebElement entry : timeSheetEntriesForCurrentDay) {
			if (entry.getText().contains(requiredTaskID)) {
				entry.click();
				break;
			}
		}
	}

	public ArrayList<String> fetchClassOfFilteredTimeSheetEntries() {
		timeSheetBox.click();
		int index;
		ArrayList<String> classList = new ArrayList<String>();
		for (index = 2; index <= 50; index++) {
			List<WebElement> timeSheetEntries = driver
					.findElements(By.xpath(".//*[@class='v-calendar-weekly__week']/div/div[" + index + "]"));

			for (WebElement entry : timeSheetEntries) {
				classList.add(entry.getAttribute("Class"));
			}
		}
		return classList;
	}

	public ExtendedWebElement getTimeSheetEntry() {
		return timeSheetEntry;
	}

	public boolean verifySubmitButtonOfEntriesFromPastDate() throws InterruptedException {
		boolean isSubmitButtonPresent = false;

		for (ExtendedWebElement pastDateEntry : pastDateEntries) {
			pastDateEntry.scrollTo();
			pastDateEntry.click();
			if (taskDetailsSubmitButton.isVisible() == true) {
				isSubmitButtonPresent = true;
			}
			taskDetailsMenuCloseButton.click();
		}
		return isSubmitButtonPresent;
	}

	public boolean verifySubmitButtonOfEntriesFromPresentDate() throws InterruptedException {
		boolean isSubmitButtonPresent = false;
		for (ExtendedWebElement presentDateEntry : presentDayEntries) {
			presentDateEntry.click();
			if (taskDetailsSubmitButton.isVisible() == true) {
				isSubmitButtonPresent = true;
			}
			taskDetailsMenuCloseButton.click();

		}
		return isSubmitButtonPresent;
	}

	public boolean verifySubmitButtonOfEntriesInFuturetDate() throws InterruptedException {
		boolean isSubmitButtonPresent = false;
		for (ExtendedWebElement futureDateEntry : futureDateEntries) {

			futureDateEntry.click();
			if (taskDetailsSubmitButton.isVisible() == true) {
				isSubmitButtonPresent = true;
			}
			taskDetailsMenuCloseButton.click();

		}
		return isSubmitButtonPresent;

	}

	public void submitEntryFromCalenderMode() {
		if (firstEntryInPendingStatus.isPresent()) {
			firstEntryInPendingStatus.click();
			taskDetailsSubmitButton.click();
		} else {
			System.out.println("There are no entries in past date with pending status");
		}
	}

	public void verifyTheTextOfTimeSheeTDisplayed() {
		for (ExtendedWebElement entry : timeSheetEntriesList) {
			System.out.println(entry.getText());
			if (entry.getText().length() > 23) {
				fail("The task details displayed in calender view of '" + entry.getText()
						+ "' has more than 20 charachters");
			}
			if (entry.getText().length() > 20) {
				String lastThreecharachters = entry.getText().substring(entry.getText().length() - 3);
				if (!lastThreecharachters.equals("...")) {
					fail("The last three charachters of the entry '" + entry.getText()
							+ "' is expected to be '...' but is " + lastThreecharachters);
				}
			}

		}
	}

	public boolean verifyApprovedTask(String approvedTask) {

		boolean isTaskApproved = false;
		for (ExtendedWebElement timesheet : timeSheetEntriesList) {
			if (timesheet.getText().equals(approvedTask)) {
				isTaskApproved = true;
				break;
			}
		}
		return isTaskApproved;
	}

	public boolean verifyTaskIsPresentInCalenderView(String requiredTask) {

		boolean isTaskFound = false;
		for (ExtendedWebElement timesheet : timeSheetEntriesList) {
			if (timesheet.getText().equals(requiredTask)) {
				isTaskFound = true;
				break;
			}
		}
		return isTaskFound;
	}

	public EmployeeTaskDetailsMenuBox clickTheRequiredTaskInCalenderView(String requiredTask) {
		boolean isTaskPresent = false;
		for (ExtendedWebElement timesheet : timeSheetEntriesList) {
			if (timesheet.getText().contains(requiredTask)) {
				isTaskPresent = true;
				timesheet.click();
				break;
			}
		}

		if (isTaskPresent == false) {
			fail("The required task is not available in the task list.");
		}
		return new EmployeeTaskDetailsMenuBox(driver, driver);
	}

	public EmployeeTaskDetailsMenuBox clickTheRequiredTask(String taskId, String requiredTime, String requiredDate) {
		boolean isTaskPresent = false;
		List<WebElement> timeSheetEntriesList = driver
				.findElements(By.xpath(".//*[@class='v-event v-event-start v-event-end white--text']"));
		for (WebElement timesheet : timeSheetEntriesList) {
			if (timesheet.getText().contains(taskId) && timesheet.getText().contains(requiredTime)
					&& timesheet.getAttribute("data-date").equals(requiredDate)) {
				isTaskPresent = true;
				timesheet.click();
				break;
			}
		}

		if (isTaskPresent == false) {
			fail("The required task is not available in the task list.");
		}
		return new EmployeeTaskDetailsMenuBox(driver, driver);
	}

	public List<String> fetchTimesheetOfEmployee() {

		List<WebElement> timeSheetEntriesList = driver
				.findElements(By.xpath("//div/div[2]/div/div[1]/div[2]/div[1]/div/div/div/div"));
		List<String> timeSheetList = new ArrayList<String>();

		for (WebElement timesheet : timeSheetEntriesList) {
			timeSheetList.add(timesheet.getText());
		}
		return timeSheetList;
	}

	public boolean verifyTheRemoveButton() {
		boolean isRemoveButtonFound = false;
		EmployeeTaskDetailsMenuBox employeeTaskDetailsmenuBox = new EmployeeTaskDetailsMenuBox(driver, driver);
		for (ExtendedWebElement timesheet : timeSheetEntriesList) {
			timesheet.click();
			isRemoveButtonFound = employeeTaskDetailsmenuBox.verifyRemoveButton();
			calenderHead.click();
		}
		return isRemoveButtonFound;
	}

	public boolean verifyTheEditButton() {
		boolean isEditButtonFound = false;
		EmployeeTaskDetailsMenuBox employeeTaskDetailsmenuBox = new EmployeeTaskDetailsMenuBox(driver, driver);
		for (ExtendedWebElement timesheet : timeSheetEntriesList) {
			timesheet.click();
			isEditButtonFound = employeeTaskDetailsmenuBox.verifyRemoveButton();
			calenderHead.click();
		}
		return isEditButtonFound;
	}

	public EmployeeTaskDetailsMenuBox clickTheFirstTimeSheetEntry() {
		if (timeSheetEntry.isVisible()) {
			timeSheetEntry.click();
		} else {
			System.out.println("There are no entries for the current month with pending status");
		}
		return new EmployeeTaskDetailsMenuBox(driver, driver);
	}

	public int formatDateAndGetYear(String dateToBeSelected) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");
		Date formattedDate = dateFormat.parse(dateToBeSelected);
		String formattedDateAsString = dateFormat.format(formattedDate);
		String yearAsString = formattedDateAsString.substring(formattedDateAsString.length() - 4);
		int year = Integer.parseInt(yearAsString);
		return year;
	}

	public String formatDateAndGetMonth(String dateToBeSelected) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");
		Date formattedDate = dateFormat.parse(dateToBeSelected);
		String dateAsString = formattedDate.toString();
		String requiredMonth = dateAsString.substring(4, 7);
		return requiredMonth;
	}

	public void selectTheRequiredMonthInCalenderView(String monthToBeSelected) {

		List<String> monthList = new ArrayList<>();
		monthList.add("January");
		monthList.add("February");
		monthList.add("March");
		monthList.add("April");
		monthList.add("May");
		monthList.add("June");
		monthList.add("July");
		monthList.add("August");
		monthList.add("September");
		monthList.add("October");
		monthList.add("November");
		monthList.add("December");
		int indexOfCurrentMonth = 0;
		int indexOfMonthToBeSelected = 0;
		String currentMonthDisplayed = calenderMonthAndYear.getText();
		String monthDisplayedOnNavigation;
		for (String month : monthList) {
			if (month.contains(monthToBeSelected)) {
				indexOfMonthToBeSelected = monthList.indexOf(month) + 1;
				break;
			}
		}
		for (String month : monthList) {
			if (currentMonthDisplayed.contains(month)) {
				indexOfCurrentMonth = monthList.indexOf(month) + 1;
				break;
			}
		}
		if (!(currentMonthDisplayed.contains(monthToBeSelected))) {
			if (indexOfMonthToBeSelected < indexOfCurrentMonth) {
				do {
					calenderBackwardButton.click();
					monthDisplayedOnNavigation = calenderMonthAndYear.getText();

				} while (!(monthDisplayedOnNavigation.contains(monthToBeSelected)));

			}
			if (indexOfMonthToBeSelected > indexOfCurrentMonth) {
				do {
					calenderForwardButton.click();
					monthDisplayedOnNavigation = calenderMonthAndYear.getText();

				} while (!(monthDisplayedOnNavigation.contains(monthToBeSelected)));

			}
		}
	}

	public void selectTheRequiredYearInCalenderView(int yearToBeSelected) {
		String currentYearAsString = calenderMonthAndYear.getText()
				.substring(calenderMonthAndYear.getText().length() - 4);

		int currentYear = Integer.parseInt(currentYearAsString);
		int yearDisplayedOnNavigation;
		if (yearToBeSelected < currentYear) {

			do {
				calenderBackwardButton.click();
				String currentYearOnNavigationAsString = calenderMonthAndYear.getText()
						.substring(calenderMonthAndYear.getText().length() - 4);
				yearDisplayedOnNavigation = Integer.parseInt(currentYearOnNavigationAsString);

			} while (yearToBeSelected != yearDisplayedOnNavigation);

		}

		else if (yearToBeSelected > currentYear) {
			do {
				calenderForwardButton.click();
				String currentYearAsStringOne = calenderMonthAndYear.getText()
						.substring(calenderMonthAndYear.getText().length() - 4);
				yearDisplayedOnNavigation = Integer.parseInt(currentYearAsStringOne);

			} while (yearToBeSelected != yearDisplayedOnNavigation);
		}
	}

	public boolean verifyTheDaysInCalenderViewOfEmployee() {

		boolean isDaysDisplayedInOrder = false;
		List<String> daysOfWeek = new ArrayList<>();
		List<String> daysDisplayedInCalenderView = new ArrayList<>();
		daysOfWeek.add("MON");
		daysOfWeek.add("TUE");
		daysOfWeek.add("WED");
		daysOfWeek.add("THU");
		daysOfWeek.add("FRI");
		daysOfWeek.add("SAT");
		daysOfWeek.add("SUN");

		for (ExtendedWebElement days : daysDisplayedInCalender) {
			daysDisplayedInCalenderView.add(days.getText());
		}

		if (daysOfWeek.equals(daysDisplayedInCalenderView)) {
			isDaysDisplayedInOrder = true;
		}
		return isDaysDisplayedInOrder;
	}

	public String verifyHolidayEvent(String requiredEvent) {
		String backGroundColour = "";
		System.out.println(backGroundColour);
		for (ExtendedWebElement event : holidayEvents) {
			if (event.getText().equals(requiredEvent)) {
				backGroundColour = event.getElement().getCssValue("color");
				System.out.println(backGroundColour);
			}
		}
		return backGroundColour;
	}

	public WebElement verifyDevOpsIconInCalenderView(String requiredTaskId, String requiredTime, String requiredDate) {

		WebElement devopsIndicationIcon = null;

		List<WebElement> taskList = driver
				.findElements(By.xpath(".//*[@class='v-event v-event-start v-event-end white--text']"));
		System.out.println(taskList.size());
		int count = 0;
		for (WebElement task : taskList) {
			count++;
			if (task.getText().contains(requiredTaskId) && task.getText().contains(requiredTime)
					&& task.getAttribute("data-date").equalsIgnoreCase(requiredDate)) {
				System.out.println(count);
				System.out.println(task.getAttribute("data-date"));
				devopsIndicationIcon = driver.findElement(
						By.xpath("(.//*[@class='v-event v-event-start v-event-end white--text'])[" + count + "]/i"));
			}
		}

		return devopsIndicationIcon;
	}

	public WebElement verifyDevOpsIconInCalenderViewForApprovedTasks(String requiredTaskId, String requiredTime,
			String requiredDate) {

		WebElement devopsIndicationIcon = null;

		List<WebElement> taskList = driver
				.findElements(By.xpath(".//*[@class='v-event v-event-start v-event-end green white--text']"));
		int count = 0;
		for (WebElement task : taskList) {
			count++;
			if (task.getText().contains(requiredTaskId) && task.getText().contains(requiredTime)
					&& task.getAttribute("data-date").equalsIgnoreCase(requiredDate)) {
				System.out.println(count);
				System.out.println(task.getAttribute("data-date"));
				devopsIndicationIcon = driver.findElement(By.xpath(
						"(.//*[@class='v-event v-event-start v-event-end green white--text'])[" + count + "]/i"));
			}
		}

		return devopsIndicationIcon;
	}

	public String fetchToolTipTextOfTheTask(String requiredTaskId, String requiredTime, String requiredDate) {
		String toolTipText = "";

		List<WebElement> taskList = driver
				.findElements(By.xpath(".//*[@class='v-event v-event-start v-event-end white--text']"));
		System.out.println("size :" + taskList.size());

		for (WebElement task : taskList) {
			if (task.getText().contains(requiredTaskId) && task.getText().contains(requiredTime)
					&& task.getAttribute("data-date").equalsIgnoreCase(requiredDate)) {
				Actions builder = new Actions(driver);
				builder.moveToElement(task).perform();
				toolTipText = taskToolTip.getText();
			}
		}
		return toolTipText;
	}

	public List<ExtendedWebElement> getPastDateEntries() {
		return pastDateEntries;
	}

	public List<ExtendedWebElement> getFutureDateEntries() {
		return futureDateEntries;
	}

	public List<ExtendedWebElement> getPresentDayEntries() {
		return presentDayEntries;
	}

	public ExtendedWebElement getFirstEntryInPendingStatus() {
		return firstEntryInPendingStatus;
	}

	public ExtendedWebElement getTimeSheetBox() {
		return timeSheetBox;
	}

}
