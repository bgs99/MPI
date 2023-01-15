package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.itmo.hungergames.model.entity.user.UserRole;

import java.time.Duration;
import java.util.List;

@Getter
public class CapitolAuthPage {

    private final WebDriver driver;

    @FindBy(xpath = "//button//*[contains(text(),'Войти')]")
    private WebElement loginButton;

    public List<WebElement> getSelectInputs() {
        waitUntilInputsLoaded();
        return driver.findElements(By.tagName("mat-select"));
    }

    public WebElement getRoleInput() {
        return this.getSelectInputs().get(0);
    }

    public WebElement getNameInput() {
        return this.getSelectInputs().get(1);
    }

    public WebElement getSelectOptionByValue(String option) {
        return driver.findElement(By.xpath(String.format("//mat-option//*[contains(text(),'%s')]", option)));
    }

    public CapitolAuthPage(WebDriver driver) {
        this.driver = driver;
    }

    public void login(String name, UserRole userRole) {
        this.getRoleInput().click();

        switch (userRole) {
            case TRIBUTE -> this.getSelectOptionByValue("Трибут").click();
            case MENTOR -> this.getSelectOptionByValue("Ментор").click();
            case SPONSOR -> throw new RuntimeException("Sponsors cannot use capitol services");
        }

        this.getNameInput().click();
        this.getSelectOptionByValue(name).click();
        this.getLoginButton().click();
    }

    public void waitUntilInputsLoaded() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(By.tagName("mat-select"), 0));
    }

}
