package pits.automation.web.hrms.employee.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

public class TaskDetailsExpandedGridBox extends AbstractUIObject {

	@FindBy(xpath = "(//tbody)[2]/tr")
	private List<ExtendedWebElement> taskDetailsRow;

	public TaskDetailsExpandedGridBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);

	}

	public WebElement fetchDevOpsIconOfTheTask(String requiredTaskTitle, String requiredTaskId, String requiredtime) {
		int rowNumber = 0;
		List<String> taskDetailsFetched = new ArrayList<>();

		for (ExtendedWebElement taskDetails : taskDetailsRow) {
			taskDetailsFetched.add(taskDetails.getText());
		}

		for (String taskDetails : taskDetailsFetched) {
			rowNumber++;
			if (taskDetails.contains(requiredTaskTitle) && taskDetails.contains(requiredTaskId)
					&& taskDetails.contains(requiredtime)) {
				break;
			}
		}

		WebElement devopsIcon = driver.findElement(By.xpath("(//tbody)[2]/tr[" + rowNumber + "]/td[1]/i"));
		return devopsIcon;
	}

	public void clickSubmitButton(String requiredTaskTitle, String requiredTaskId, String requiredtime) {

		int rowNumber = 0;
		List<String> taskDetailsFetched = new ArrayList<>();

		for (ExtendedWebElement taskDetails : taskDetailsRow) {
			taskDetailsFetched.add(taskDetails.getText());
		}

		for (String taskDetails : taskDetailsFetched) {
			rowNumber++;
			if (taskDetails.contains(requiredTaskTitle) && taskDetails.contains(requiredTaskId)
					&& taskDetails.contains(requiredtime)) {
				break;
			}
		}
		WebElement submitButton = driver.findElement(By.xpath("(//tbody)[2]/tr[" + rowNumber + "]/td[7]/button"));

		submitButton.click();
	}
}
