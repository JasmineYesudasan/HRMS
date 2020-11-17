package pits.automation.web.hrms.employee.pages;

import java.util.List;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;

public class DatePickerForEditingTimeSheet extends AbstractUIObject {

	@FindBy(xpath = ".//*[@class='v-date-picker-header__value']/div/button")
	private ExtendedWebElement monthAndDate;

	@FindBy(xpath = ".//*[@class='v-date-picker-header__value']/div/button")
	private ExtendedWebElement year;

	@FindBy(xpath = "//div/div/div/ul/li")
	private List<ExtendedWebElement> yearList;

	@FindBy(xpath = ".//*[@class='v-date-picker-table v-date-picker-table--month theme--light']/table/tbody/tr/td/button")
	private List<ExtendedWebElement> monthList;

	@FindBy(xpath = ".//*[@class='v-date-picker-table v-date-picker-table--date theme--light']/table/tbody/tr/td/button")
	private List<ExtendedWebElement> dateList;

	@FindBy(xpath = ".//*[@class='v-date-picker-table v-date-picker-table--date theme--light']/table/tbody/tr/td[1]/button")
	private List<ExtendedWebElement> sundaysOfTheMonth;

	@FindBy(xpath = ".//*[@class='v-date-picker-table v-date-picker-table--date theme--light']/table/tbody/tr/td[7]/button")
	private List<ExtendedWebElement> saturdaysOfTheMonth;

	public DatePickerForEditingTimeSheet(WebDriver driver, SearchContext searchContext) {
		super(driver, searchContext);
	}

	public void selectDate(String requiredYear, String requiredMonth, String requiredDate) {
		monthAndDate.click();
		year.click();

		for (ExtendedWebElement year : yearList) {
			if (year.getText().equals(requiredYear)) {
				year.click();
				break;
			}
		}
		for (ExtendedWebElement month : monthList) {
			if (month.getText().equals(requiredMonth)) {
				month.click();
				break;
			}
		}
		for (ExtendedWebElement date : dateList) {
			if (date.getText().equals(requiredDate)) {
				date.click();
				break;
			}
		}
	}

	public boolean verifyTheRequiredDateIsEnabledOrNot(String requiredYear, String requiredMonth, String requiredDate) {
		monthAndDate.click();
		year.click();
		boolean isDateEnabled = false;
		for (ExtendedWebElement year : yearList) {
			if (year.getText().equals(requiredYear)) {
				year.click();
				break;
			}
		}
		for (ExtendedWebElement month : monthList) {
			if (month.getText().equals(requiredMonth)) {
				month.click();
				break;
			}
		}
		for (ExtendedWebElement date : dateList) {
			if (date.getText().equals(requiredDate)) {

				isDateEnabled = date.isClickable();
				break;
			}
		}
		return isDateEnabled;
	}

	public boolean verifySundaysAreDisabled() {

		boolean areSundaysDisabled = false;

		for (ExtendedWebElement sunday : sundaysOfTheMonth) {
			if (sunday.getAttribute("class").contains("disabled theme")
					&& sunday.getAttribute("disabled").equals("true")) {
				areSundaysDisabled = true;
			}
		}
		return areSundaysDisabled;
	}

	public boolean verifySaturdaysAreDisabled() {

		boolean areSaturdaysDisabled = false;

		for (ExtendedWebElement saturday : saturdaysOfTheMonth) {
			if (saturday.getAttribute("class").contains("disabled theme")
					&& saturday.getAttribute("disabled").equals("true")) {
				areSaturdaysDisabled = true;
			}
		}
		return areSaturdaysDisabled;
	}

	public boolean verifySaturdaysCannotBeSelected() {

		boolean isSaturdayClickable = false;
		for (ExtendedWebElement saturday : saturdaysOfTheMonth) {
			if (saturday.isClickable() == true) {
				isSaturdayClickable = true;
			}
		}
		return isSaturdayClickable;
	}

	public boolean verifySundaysCannotBeSelected() {

		boolean isSundayClickable = false;
		for (ExtendedWebElement sunday : sundaysOfTheMonth) {
			if (sunday.isClickable() == true) {
				isSundayClickable = true;
			}
		}
		return isSundayClickable;
	}

	public String selectCurrentDate() {

		String currentDay = "";
		for (ExtendedWebElement day : dateList) {
			if (day.getAttribute("class").contains("outlined")) {
				day.click();
				currentDay = day.getText();
				break;

			}
		}
		return currentDay;
	}
}
