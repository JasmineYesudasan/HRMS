package pits.automation.web.hrms.manager.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

public class TaskApprovalListBox extends AbstractUIObject {

	@FindBy(xpath = ".//button[@class='v-expansion-panel-header']")
	private List<ExtendedWebElement> employeeList;

	@FindBy(xpath = "//table/tbody/tr")
	private List<ExtendedWebElement> employeeTaskList;

	@FindBy(xpath = "//table/tbody/tr/td[1]")
	private List<ExtendedWebElement> submittedTaskIdList;

	@FindBy(xpath = ".//*[@class='v-toolbar__title lblColor']")
	private List<ExtendedWebElement> taskDateList;

	@FindBy(xpath = ".//*[@data-test-id='btnApprove']")
	private ExtendedWebElement approveIcon;

	@FindBy(xpath = ".//*[@data-test-id='btnReject']")
	private ExtendedWebElement rejectIcon;

	@FindBy(xpath = "(.//*[@class='v-toolbar__title lblColor'])[1]")
	private ExtendedWebElement dateOfFirstEntryOfTheSelectedEmployee;

	@FindBy(xpath = "(//table/tbody/tr[1])[1]/td[1]")
	private ExtendedWebElement taskIDOfFirstEntryOfTheSelectedEmployee;

	@FindBy(xpath = "(//table/tbody/tr[1])[1]/td[2]")
	private ExtendedWebElement descriptionOfFirstEntryOfTheSelectedEmployee;

	@FindBy(xpath = "(//table/tbody/tr[1])[1]/td[3]")
	private ExtendedWebElement timeTakenOfFirstEntryOfTheSelectedEmployee;

	public TaskApprovalListBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);
	}

	public boolean expandTheTaskDetailsOfParticularEmployee(String requiredEmployee) {
		boolean isEmployeeFound = false;
		for (ExtendedWebElement employee : employeeList) {
			if (employee.getText().contains(requiredEmployee)) {
				isEmployeeFound = true;
				employee.click();
			}
		}
		return isEmployeeFound;
	}

	public boolean verifyEmployeeNamesAreSorted() {
		boolean isSorted = true;
		int index;
		ArrayList<String> employeeNameList = new ArrayList<String>();
		ArrayList<String> filteredList = new ArrayList<String>();
		for (ExtendedWebElement employee : employeeList) {
			employeeNameList.add(employee.getText());
		}

		for (String employee : employeeNameList) {
			employee = employee.substring(0, employee.indexOf('-'));
			filteredList.add(employee);

		}

		for (index = 1; index < filteredList.size(); index++) {
			if (filteredList.get(index - 1).compareTo(filteredList.get(index)) > 0)
				isSorted = false;
		}
		return isSorted;
	}

	public boolean selectTheRequiredEmployee(String employeeToBeSelected) {
		boolean isEmployeeFound = false;
		for (ExtendedWebElement employee : employeeList) {
			if (employee.getText().toLowerCase().contains(employeeToBeSelected.toLowerCase())) {
				isEmployeeFound = true;
				employee.click();
				break;
			}
		}
		return isEmployeeFound;
	}

	public int fetchTheEmployeeTaskCountDisplayedForManager(String employeeToBeSelected) {
		String countDisplayed;
		int countOfTasksDisplayed = 0;
		for (ExtendedWebElement employee : employeeList) {
			if (employee.getText().contains(employeeToBeSelected)) {
				countDisplayed = employee.getText().substring(employee.getText().indexOf("(") + 1,
						employee.getText().indexOf(")"));
				System.out.println(countDisplayed);
				countOfTasksDisplayed = Integer.parseInt(countDisplayed);
				System.out.println(countOfTasksDisplayed);

			}
		}
		return countOfTasksDisplayed;
	}

	/*
	 * public void verifyDatesAreInAscendingOrder() throws ParseException {
	 * List<Date> dateCollection = new ArrayList<>(); SimpleDateFormat dateFormat =
	 * new SimpleDateFormat("dd-MM-yyyy"); for (ExtendedWebElement employee :
	 * employeeList) { employee.click(); for (ExtendedWebElement taskDate :
	 * taskDateList) { System.out.println(taskDate.getText());
	 * dateCollection.add(dateFormat.parse(taskDate.getText())); }
	 * Collections.sort(dateCollection); for (Date taskDate : dateCollection) {
	 * System.out.println(taskDate); } }
	 * 
	 * for (ExtendedWebElement taskDate : taskDateList) { for (Date taskDaten :
	 * dateCollection) {
	 * 
	 * } } }
	 */

	public void verifyDate() {
		for (ExtendedWebElement employee : employeeList) {
			employee.click();
			for (ExtendedWebElement taskDate : taskDateList) {
				System.out.println(taskDate.getText());
				// String year = taskDate.getText().substring(taskDate.getText().length() - 3);

			}
		}
	}

	public List<String> fetchTaskIdOfSubmittedTasksDisplayedForManager() {
		List<String> taskIdList = new ArrayList<>();
		for (ExtendedWebElement taskId : submittedTaskIdList) {
			taskIdList.add(taskId.getText());
			System.out.println(taskId.getText());
		}
		return taskIdList;
	}

	public ExtendedWebElement getDateOfFirstEntryOfTheSelectedEmployee() {
		return dateOfFirstEntryOfTheSelectedEmployee;
	}

	public ExtendedWebElement getApproveIcon() {
		return approveIcon;
	}

	public ExtendedWebElement getRejectIcon() {
		return rejectIcon;
	}

	public ExtendedWebElement getTaskIDOfFirstEntryOfTheSelectedEmployee() {
		return taskIDOfFirstEntryOfTheSelectedEmployee;
	}

	public ExtendedWebElement getDescriptionOfFirstEntryOfTheSelectedEmployee() {
		return descriptionOfFirstEntryOfTheSelectedEmployee;
	}

	public ExtendedWebElement getTimeTakenOfFirstEntryOfTheSelectedEmployee() {
		return timeTakenOfFirstEntryOfTheSelectedEmployee;
	}
}
