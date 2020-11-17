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

public class ProjectSelectorBox extends AbstractUIObject {

	@FindBy(xpath = "//*[@id='devopsTaskDiv']/div/div[3]/div/div[1]/div[1]")
	private ExtendedWebElement projectDropDown;

	@FindBy(xpath = "//*[@id='devopsTaskDiv']/div/div[3]/div/div[1]/div[1]/div[1]/div")
	private ExtendedWebElement projectSelected;

	@FindBy(xpath = ".//*[@role='listbox']/div/div/div")
	private List<ExtendedWebElement> projectDropDownMenuList;

	public ProjectSelectorBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);
	}

	public void clickSelectProjectDropDown() {
		projectDropDown.click();
	}

	public List<String> fetchTheProjectList() {
		List<WebElement> projectList = driver.findElements(By.xpath(
				".//*[@class='v-menu__content theme--light v-menu__content--fixed menuable__content__active']/div/div/div/div"));
		List<String> projectsDisplayedForUser = new ArrayList<>();

		for (WebElement projects : projectList) {
			projectsDisplayedForUser.add(projects.getText());
		}

		return projectsDisplayedForUser;
	}

	public boolean selectTheRequiredProject(String requiredProject) {
		boolean isProjectFound = false;
		List<WebElement> projectList = driver.findElements(By.xpath(
				".//*[@class='v-menu__content theme--light v-menu__content--fixed menuable__content__active']/div/div"));

		for (WebElement project : projectList) {
			System.out.println(project.getText());
			if (project.getText().equalsIgnoreCase(requiredProject)) {
				System.out.println(project.getText());

				isProjectFound = true;
				project.click();
				break;
			}
		}
		return isProjectFound;
	}

	public ExtendedWebElement getProjectDropDown() {
		return projectDropDown;
	}

	public ExtendedWebElement getProjectSelected() {
		return projectSelected;
	}
}
