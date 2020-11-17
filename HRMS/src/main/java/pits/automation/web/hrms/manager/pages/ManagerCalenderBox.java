package pits.automation.web.hrms.manager.pages;

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
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

public class ManagerCalenderBox extends AbstractUIObject {

	@FindBy(xpath = ".//*[@class='v-input__append-inner']")
	private ExtendedWebElement employeeListDropDown;

	@FindBy(xpath = "//*[@role='option']/div/div")
	private List<ExtendedWebElement> employeeNameList;

	@FindBy(xpath = "//div/div[2]/div/div/div/div/div/div/div/div/div")
	private List<ExtendedWebElement> employeeTaskList;

	@FindBy(xpath = "//div[4]/div/header/div/button")
	private ExtendedWebElement taskDetailsMenuCloseButton;

	@FindBy(xpath = ".//*[@class='v-calendar-weekly__head']")
	private ExtendedWebElement calenderHead;

	@FindBy(xpath = ".//*[@class='v-toolbar__title']")
	private ExtendedWebElement calenderMonthAndYear;

	@FindBy(xpath = "//*[@id='inspire']/div[1]/main/div/div[2]/div[2]/div/div/div/div[1]/header/div/button[1]/span/i")
	private ExtendedWebElement calenderBackwardButton;

	@FindBy(xpath = "//*[@id='inspire']/div[1]/main/div/div[2]/div[2]/div/div/div/div[1]/header/div/button[2]/span/i")
	private ExtendedWebElement calenderForwardButton;

	@FindBy(xpath = "//*[@id='inspire']/div/div[2]/div/div/div/div[2]/div[1]/div[1]/div")
	private List<ExtendedWebElement> daysDisplayedInEmployeeCalenderForManager;

	public ManagerCalenderBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);
		// daysDisplayedInCalender =
		// findExtendedWebElements(By.xpath(daysDisplayedInEmployeeCalenderForManagerXpath));
	}

	public boolean selectEmployee(String employeeToBeSelected) {

		boolean employeePresent = false;
		employeeListDropDown.click();
		for (ExtendedWebElement employee : employeeNameList) {
			if (employee.getText().contains(employeeToBeSelected)) {
				employeePresent = true;
				employee.click();
				break;
			}
		}
		return employeePresent;
	}

	public boolean selectTimeSheetToApprove(String taskToBeApproved) {

		for (ExtendedWebElement task : employeeTaskList) {
			System.out.println(task.getText());
		}

		boolean isTaskFound = false;
		for (ExtendedWebElement task : employeeTaskList) {
			System.out.println(task.getText());
			if (task.getText().equals(taskToBeApproved)) {
				isTaskFound = true;
				task.click();
				break;
			}

		}
		return isTaskFound;
	}

	public List<String> fetchTheEmployeeTasksDisplayedForManager() {
		List<String> taskListOfManager = new ArrayList<String>();

		for (ExtendedWebElement task : employeeTaskList) {
			System.out.println(task.getText());
			taskListOfManager.add(task.getText());
		}
		return taskListOfManager;
	}

	public void VerifyTaskDetailsMenuBoxOfManager() throws InterruptedException {
		ManagerTaskDetailsMenuBox taskDetailsMenuBox = new ManagerTaskDetailsMenuBox(driver, driver);
		for (ExtendedWebElement task : employeeTaskList) {
			task.click();
			taskDetailsMenuBox.verifytheTaskDetailsDisplayedForManager();
			taskDetailsMenuCloseButton.click();
			calenderHead.click();
		}
	}

	public List<String> fetchClassOfTimeSheetEntriesDisplayedForManager() {

		int index;
		List<String> classList = new ArrayList<String>();
		for (index = 2; index <= 50; index++) {
			List<WebElement> timeSheetEntries = driver
					.findElements(By.xpath(".//*[@class='v-calendar-weekly__week']/div/div[" + index + "]"));

			for (WebElement entry : timeSheetEntries) {
				classList.add(entry.getAttribute("Class"));
			}
		}
		return classList;
	}

	public ExtendedWebElement getEmployeeListDropDown() {
		return employeeListDropDown;
	}

	public boolean verifyTheDaysInCalenderViewOfManagerEmployee() {
		List<WebElement> daysDisplayedInCalender = driver
				.findElements(By.xpath(".//*[@class='v-calendar-weekly__head']/div"));
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

		System.out.println(daysDisplayedInCalender.size());
		for (WebElement days : daysDisplayedInCalender) {
			daysDisplayedInCalenderView.add(days.getText());
		}

		if (daysOfWeek.equals(daysDisplayedInCalenderView)) {
			isDaysDisplayedInOrder = true;
		}
		return isDaysDisplayedInOrder;
	}

	public boolean verifyTheDaysInEmployeeCalenderViewOfManager() {

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

		for (ExtendedWebElement days : daysDisplayedInEmployeeCalenderForManager) {
			daysDisplayedInCalenderView.add(days.getText());
		}

		if (daysOfWeek.equals(daysDisplayedInCalenderView)) {
			isDaysDisplayedInOrder = true;
		}
		return isDaysDisplayedInOrder;
	}

	public List<String> fetchBackgroudColourOfWeekendsForManagerCalender() {
		int indexOfRow;
		int weekendCount;

		List<String> backGroundColor = new ArrayList<>();

		for (indexOfRow = 2; indexOfRow <= 6; indexOfRow++) {
			for (weekendCount = 6; weekendCount <= 7; weekendCount++) {
				WebElement weekEnds = driver.findElement(By.xpath("//*[@id='inspire']/div/div/div/div/div/div/div/div["
						+ indexOfRow + "]/div[" + weekendCount + "]"));
				backGroundColor.add(weekEnds.getCssValue("background-color"));
			}
		}
		return backGroundColor;
	}

	public List<String> fetchBackgroudColourOfWeekendsOfManagerEmployee() {
		int indexOfRow;
		int weekendCount;

		List<String> backGroundColor = new ArrayList<>();

		for (indexOfRow = 2; indexOfRow <= 6; indexOfRow++) {
			for (weekendCount = 6; weekendCount <= 7; weekendCount++) {
				WebElement weekEnds = driver.findElement(By.xpath(
						"//*[@id='app']/div/div/div/div/div/div/div[" + indexOfRow + "]/div[" + weekendCount + "]"));
				backGroundColor.add(weekEnds.getCssValue("background-color"));
				System.out.println(weekEnds.getCssValue("background-color"));

			}
		}
		return backGroundColor;
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

	public WebElement verifyDevOpsIconInManagerCalenderView(String requiredTaskId, String requiredTime,
			String requiredDate) {

		WebElement devopsIndicationIcon = null;

		List<WebElement> taskList = driver
				.findElements(By.xpath(".//*[@class='v-event v-event-start v-event-end white--text']"));
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

	public ManagerTaskDetailsMenuBox clickTheRequiredTask(String taskId, String requiredTime) {
		boolean isTaskPresent = false;
		List<WebElement> timeSheetEntriesList = driver
				.findElements(By.xpath(".//*[@class='v-event v-event-start v-event-end white--text']"));
		for (WebElement timesheet : timeSheetEntriesList) {
			if (timesheet.getText().contains(taskId) && timesheet.getText().contains(requiredTime)) {
				isTaskPresent = true;
				timesheet.click();
				break;
			}
		}

		if (isTaskPresent == false) {
			fail("The required task is not available in the task list.");
		}
		return new ManagerTaskDetailsMenuBox(driver, driver);
	}
}
