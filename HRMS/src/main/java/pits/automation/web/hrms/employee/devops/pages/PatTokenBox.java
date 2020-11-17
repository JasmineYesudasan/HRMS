package pits.automation.web.hrms.employee.devops.pages;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

public class PatTokenBox extends AbstractUIObject {

	@FindBy(xpath = ".//*[@data-test-id='txtPatToken']")
	private ExtendedWebElement patTokenField;

	public PatTokenBox(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);
	}

	public ExtendedWebElement getPatTokenField() {
		return patTokenField;
	}

}
