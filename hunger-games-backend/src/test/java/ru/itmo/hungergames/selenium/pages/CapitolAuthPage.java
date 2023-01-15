package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Getter
public class CapitolAuthPage {

    private final WebDriver driver;

    @FindBy(xpath = "//mat-mdc-select")
    private List<WebElement> selectInputs;

    public List<WebElement> getSelectOptions() {
        return driver.findElements(By.tagName("mat-option"));
    }

    public WebElement getSelectOptionByValue(String option) {
        return driver.findElement(By.xpath(String.format("//mat-option//*[contains(text(),'%s')]", option)));
    }

    @FindBy(xpath = "//button//*[contains(text(),'Войти')]")
    private WebElement loginButton;

    public CapitolAuthPage(WebDriver driver) {
        this.driver = driver;
    }
}
