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
}
