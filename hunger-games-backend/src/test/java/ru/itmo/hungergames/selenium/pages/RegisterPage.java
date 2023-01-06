package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class RegisterPage {
    @FindBy(id = "name")
    private WebElement sponsorNameInput;

    @FindBy(id = "login")
    private WebElement sponsorLoginInput;

    @FindBy(id = "password")
    private WebElement sponsorPasswordInput;

    @FindBy(id = "password2")
    private WebElement sponsorPassword2Input;

    @FindBy(xpath = "//button//*[contains(text(),'Зарегистрироваться')]")
    private WebElement sponsorRegisterButton;

    @FindBy(xpath = "//mat-error")
    private WebElement registerError;
}
