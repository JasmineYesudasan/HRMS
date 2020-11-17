package pits.automation.web.hrms.manager.pages;

import static org.junit.jupiter.api.Assertions.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

public class EmployeeTaskListBox extends AbstractUIObject {

	@FindBy(xpath = "//table/tbody/tr")
	private List<ExtendedWebElement> employeeTaskList;

	@FindBy(xpath = "//*[@id='inspire']/div/div/div/div/div/div/button")
	private List<ExtendedWebElement> employeeList;

	@FindBy(xpath = "//div/div/div/div/div/div/div/div/div/div/div/div/table/thead")
	private List<ExtendedWebElement> taskHeaderList;

	@FindBy(xpath = ".//*[@class='v-toolbar__title lblColor']")
	private List<ExtendedWebElement> taskDateList;

	@FindBy(xpath = ".//*[@class='v-expansion-panel-header v-expansion-panel-header--active']")
	private ExtendedWebElement selectedEmployee;

	public EmployeeTaskListBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);

	}

	public boolean verifyCountOfTask() {

		boolean isCountSame = false;
		System.out.println(employeeList.size());
		for (ExtendedWebElement employee : employeeList) {
			System.out.println(employeeList.size());

			employee.click();
			int countOfTask = employeeList.size();
			String taskCount = Integer.toString(countOfTask);

			String countDisplayed = employee.getText().substring(0, employee.getText().indexOf('('));
			System.out.println(countDisplayed);
			System.out.println(taskCount);
			if (taskCount.equals(countDisplayed)) {
				isCountSame = true;
			}
		}
		return isCountSame;

	}

	public ArrayList<String> fetchEmployeeTaskDetailsHeaderElements() throws InterruptedException {

		ArrayList<String> headerList = new ArrayList<String>();
		for (ExtendedWebElement header : taskHeaderList) {
			headerList.add(header.getText());
		}
		return headerList;
	}

	public boolean verifyCountOfTaskDisplayedForTheEmployee() {
		boolean isCountSame = false;
		int requiredCount = employeeTaskList.size();
		String requiredTaskCount = Integer.toString(requiredCount);
		String countDisplayed = selectedEmployee.getText().substring(selectedEmployee.getText().indexOf("(") + 1,
				selectedEmployee.getText().indexOf(")"));
		if (countDisplayed.equalsIgnoreCase(requiredTaskCount)) {
			isCountSame = true;
		}
		return isCountSame;
	}

	public void verifyApproveAndRejectButtonsAreAvailableForEachTask() {
		int index = 0;
		for (ExtendedWebElement employeetask : employeeTaskList) {
			index++;
			WebElement approveButton = driver
					.findElement(By.xpath("(.//*[@data-test-id='btnApprove'])[" + index + "]"));
			WebElement rejectButton = driver.findElement(By.xpath("(.//*[@data-test-id='btnReject'])[" + index + "]"));
			if (!approveButton.isDisplayed() && !rejectButton.isDisplayed()) {
				fail("Approve and Reject Buttons are not available for " + employeetask.getText());
			}
		}
	}

	public boolean verifyTaskDatesAreSorted() throws ParseException {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		List<String> employeeTaskDateList = new ArrayList<String>();

		List<WebElement> taskDateList = driver.findElements(By.xpath(".//*[@class='v-toolbar__title lblColor']"));
		for (WebElement employee : taskDateList) {
			employeeTaskDateList.add(employee.getText());
		}
		boolean ascendingOrder = true;
		for (int index = 0; index < employeeTaskDateList.size() - 1; index++) {
			if (simpleDateFormat.parse(employeeTaskDateList.get(index)).getTime() > simpleDateFormat
					.parse(employeeTaskDateList.get(index + 1)).getTime()) {
				ascendingOrder = false;
				break;
			}
		}
		return ascendingOrder;
	}

	public WebElement verifyDevOpsIconInManagerTaskList(String requiredDate, String requiredTaskTitle,
			String requiredTaskId, String requiredtime) throws InterruptedException {
		int rowNumber = 0;
		List<String> taskDetailsFetched = new ArrayList<>();

		List<String> employeeTaskDateList = new ArrayList<String>();

		List<WebElement> taskDateList = driver.findElements(By.xpath(".//*[@class='v-toolbar__title lblColor']"));
		for (WebElement employee : taskDateList) {
			employeeTaskDateList.add(employee.getText());
		}

		int tableNumber = employeeTaskDateList.indexOf(requiredDate) + 1;

		List<WebElement> employeeTaskList = driver.findElements(By.xpath("(//tbody)[" + tableNumber + "]/tr"));

		for (WebElement taskDetails : employeeTaskList) {
			taskDetailsFetched.add(taskDetails.getText());
		}

		for (String taskDetails : taskDetailsFetched) {
			rowNumber++;
			if (taskDetails.contains(requiredTaskTitle) && taskDetails.contains(requiredTaskId)
					&& taskDetails.contains(requiredtime)) {
				break;
			}
		}

		WebElement devopsIcon = driver
				.findElement(By.xpath("(//tbody)[" + tableNumber + "]/tr[" + rowNumber + "]/td[1]/i"));
		Actions builder = new Actions(driver);

		builder.moveToElement(devopsIcon);
		Thread.sleep(2000);
		return devopsIcon;
	}

	public TaskRejectConfirmationPopUp clickRejectButtonOfTheRequiredTask(String requiredDate, String requiredTaskTitle,
			String requiredTaskId, String requiredtime) throws InterruptedException {

		int rowNumber = 0;
		List<String> taskDetailsFetched = new ArrayList<>();

		List<String> employeeTaskDateList = new ArrayList<String>();

		List<WebElement> taskDateList = driver.findElements(By.xpath(".//*[@class='v-toolbar__title lblColor']"));
		for (WebElement employee : taskDateList) {
			employeeTaskDateList.add(employee.getText());
		}

		int tableNumber = employeeTaskDateList.indexOf(requiredDate) + 1;

		List<WebElement> employeeTaskList = driver.findElements(By.xpath("(//tbody)[" + tableNumber + "]/tr"));

		for (WebElement taskDetails : employeeTaskList) {
			taskDetailsFetched.add(taskDetails.getText());
		}

		for (String taskDetails : taskDetailsFetched) {
			rowNumber++;
			if (taskDetails.contains(requiredTaskTitle) && taskDetails.contains(requiredTaskId)
					&& taskDetails.contains(requiredtime)) {
				break;
			}
		}

		WebElement rejectButton = driver
				.findElement(By.xpath("(//tbody)[" + tableNumber + "]/tr[" + rowNumber + "]/td[5]/button[2]"));

		Actions builder = new Actions(driver);

		builder.moveToElement(rejectButton);
		Thread.sleep(2000);
		rejectButton.click();

		return new TaskRejectConfirmationPopUp(driver, driver);

	}

	public void clickSubmitButtonOfTheRequiredTask(String requiredDate, String requiredTaskTitle, String requiredTaskId,
			String requiredtime) throws InterruptedException {

		int rowNumber = 0;
		List<String> taskDetailsFetched = new ArrayList<>();

		List<String> employeeTaskDateList = new ArrayList<String>();

		List<WebElement> taskDateList = driver.findElements(By.xpath(".//*[@class='v-toolbar__title lblColor']"));
		for (WebElement employee : taskDateList) {
			employeeTaskDateList.add(employee.getText());
		}

		int tableNumber = employeeTaskDateList.indexOf(requiredDate) + 1;

		List<WebElement> employeeTaskList = driver.findElements(By.xpath("(//tbody)[" + tableNumber + "]/tr"));

		for (WebElement taskDetails : employeeTaskList) {
			taskDetailsFetched.add(taskDetails.getText());
		}

		for (String taskDetails : taskDetailsFetched) {
			rowNumber++;
			if (taskDetails.contains(requiredTaskTitle) && taskDetails.contains(requiredTaskId)
					&& taskDetails.contains(requiredtime)) {
				break;
			}
		}

		WebElement submitButton = driver
				.findElement(By.xpath("(//tbody)[" + tableNumber + "]/tr[" + rowNumber + "]/td[5]/button[1]"));

		Actions builder = new Actions(driver);

		builder.moveToElement(submitButton);
		Thread.sleep(2000);
		submitButton.click();

	}
}