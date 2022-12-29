package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class LoginPage {
    @FindBy(id = "login")
    private WebElement sponsorLoginInput;

    @FindBy(id = "password")
    private WebElement sponsorPasswordInput;

    @FindBy(xpath = "//button//*[contains(text(),'Войти')]")
    private WebElement sponsorLoginButton;


    @FindBy(xpath = "//button//*[contains(text(),'Зарегистрироваться')]")
    private WebElement sponsorRegisterButton;

    @FindBy(xpath = "//input[@type='image']")
    private WebElement capitolAuthLink;
}
