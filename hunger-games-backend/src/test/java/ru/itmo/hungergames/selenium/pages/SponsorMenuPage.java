package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class SponsorMenuPage {
    @FindBy(xpath = "//button//*[contains(text(),'Чаты')]")
    private WebElement chatsButton;
    @FindBy(xpath = "//button//*[contains(text(),'Трибуты')]")
    private WebElement tributesButton;
    @FindBy(xpath = "//button//*[contains(text(),'Новости')]")
    private WebElement newsButton;
    @FindBy(xpath = "//button//*[contains(text(),'Настройки')]")
    private WebElement settingsButton;

}
