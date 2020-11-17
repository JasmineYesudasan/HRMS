package pits.automation.web.devops.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

public class ProjectCardBox extends AbstractUIObject {

	@FindBy(xpath = "/html/body/div/div/div/div/div/div/div/div/div/div/div/ul/li/a")
	private List<ExtendedWebElement> projectCardList;

	@FindBy(xpath = ".//*[@class='project-name fontSizeL fontWeightSemiBold']")
	private List<ExtendedWebElement> projectNameList;

	public ProjectCardBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);
	}

	public boolean selectTheRequiredProject(String requiredProject) {
		boolean isProjectAvailable = false;
		/*
		 * for (ExtendedWebElement projectcard : projectCardList) {
		 * System.out.println(projectcard.getText()); }
		 */
		for (ExtendedWebElement projectcard : projectCardList) {
			if (projectcard.getText().contains(requiredProject)) {
				isProjectAvailable = true;
				projectcard.click();
				break;
			}
		}
		return isProjectAvailable;
	}

	public List<String> fetchTheProjectList() {
		List<String> projectList = new ArrayList<>();
		for (ExtendedWebElement projectName : projectNameList) {
			projectList.add(projectName.getText());
		}
		return projectList;
	}

}
