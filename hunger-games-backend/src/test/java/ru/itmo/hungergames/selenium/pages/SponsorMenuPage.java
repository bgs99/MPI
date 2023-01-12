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

    public enum Action {
        Chats,
        Tributes,
        News,
        Settings,
    }

    public void goTo(Action action) {
        switch (action) {
            case Chats:
                this.chatsButton.click();
                break;
            case Tributes:
                this.tributesButton.click();
                break;
            case News:
                this.newsButton.click();
                break;
            case Settings:
                this.settingsButton.click();
                break;
        }
    }

}
