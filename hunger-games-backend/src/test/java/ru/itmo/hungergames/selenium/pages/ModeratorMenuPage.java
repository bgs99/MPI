package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class ModeratorMenuPage {
    @FindBy(xpath = "//button//*[contains(text(),'Опубликовать новость')]")
    private WebElement addNewsButton;
    @FindBy(xpath = "//button//*[contains(text(),'Рассмотреть посты')]")
    private WebElement tributesButton;

    public enum Action {
        Tributes,
        News,
    }

    public void goTo(Action action) {
        switch (action) {
            case Tributes -> this.tributesButton.click();
            case News -> this.addNewsButton.click();
        }
    }
}
