package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class ModeratorLoginPage {
    @FindBy(id = "login")
    private WebElement loginInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(xpath = "//button//*[contains(text(),'Войти')]")
    private WebElement loginButton;

    @FindBy(xpath = "//mat-error")
    private WebElement loginError;

    public void clear() {
        this.loginInput.clear();
        this.passwordInput.clear();
    }

    public void login(String username, String password) {
        this.clear();
        this.loginInput.sendKeys(username);
        this.passwordInput.sendKeys(password);
        this.loginButton.click();
    }
}
