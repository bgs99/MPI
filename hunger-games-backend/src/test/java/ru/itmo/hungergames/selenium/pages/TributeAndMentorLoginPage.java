package ru.itmo.hungergames.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class TributeAndMentorLoginPage {

    private final WebDriver driver;

    @FindBy(xpath = "//mat-mdc-select")
    private List<WebElement> selectInputs;

    public List<WebElement> getSelectOptions() {
        return driver.findElements(By.tagName("mat-option"));
    }

    @FindBy(xpath = "//button//*[contains(text(),'Войти')]")
    private WebElement loginButton;

    public TributeAndMentorLoginPage(WebDriver driver) {
        this.driver = driver;
    }
}
