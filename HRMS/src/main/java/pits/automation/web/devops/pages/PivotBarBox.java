package pits.automation.web.devops.pages;

import java.util.List;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

public class PivotBarBox extends AbstractUIObject {

	@FindBy(xpath = ".//*[@data-command-key='work-item-filter-bar']")
	private ExtendedWebElement filterButton;

	@FindBy(xpath = "(.//span[@role='textbox'])[2]")
	private ExtendedWebElement assignedToFilterDropdown;

	@FindBy(xpath = "//*[@class='vss-PickList--multiSelectTextContainer']/div/span")
	private List<ExtendedWebElement> assignedToFilterOptions;

	@FindBy(xpath = ".//*[@class='vss-FilterBar--list']")
	private ExtendedWebElement filterBar;

	@FindBy(xpath = "//*[@class='vss-PickListDropdown--title']/div/div/span")
	private ExtendedWebElement assigneeDisplayed;

	@FindBy(xpath = "//*[@class='vss-PickList--filterBarFooter flex-column']/button")
	private ExtendedWebElement clearFilterButton;

	public PivotBarBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);

	}

	public boolean verifyIfAssigneeFilterIsApplied(String requiredAssignee) {
		boolean isAssigneeFound = false;

		if (filterBar.isPresent()) {
			System.out.println(assigneeDisplayed.getText());
			if (!(assigneeDisplayed.getText().equals(requiredAssignee))) {
				assignedToFilterDropdown.click();
				clearFilterButton.click();
				for (ExtendedWebElement assignedToOption : assignedToFilterOptions) {
					if (assignedToOption.getText().equalsIgnoreCase(requiredAssignee)) {
						isAssigneeFound = true;
						assignedToOption.click();
						break;
					}
				}
			} else {
				isAssigneeFound = true;
			}

		} else {
			filterButton.click();
			assignedToFilterDropdown.click();
			clearFilterButton.click();

			for (ExtendedWebElement assignedToOption : assignedToFilterOptions) {
				if (assignedToOption.getText().equalsIgnoreCase(requiredAssignee)) {
					isAssigneeFound = true;
					assignedToOption.click();
					break;
				}
			}

		}

		return isAssigneeFound;
	}

	public boolean applyFilterWithTheRequiredAssignee(String requiredAssignee) {
		boolean isAssigneeFound = false;
		filterButton.click();
		assignedToFilterDropdown.click();
		for (ExtendedWebElement assignedToOption : assignedToFilterOptions) {
			if (assignedToOption.getText().equalsIgnoreCase(requiredAssignee)) {
				isAssigneeFound = true;
				assignedToOption.click();
				break;
			}
		}
		return isAssigneeFound;
	}
}
