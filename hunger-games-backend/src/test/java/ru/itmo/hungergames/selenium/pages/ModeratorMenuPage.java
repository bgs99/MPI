package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class ModeratorMenuPage {
    @FindBy(xpath = "//button//*[contains(text(),'Опубликовать новость')]")
    private WebElement chatsButton;
    @FindBy(xpath = "//button//*[contains(text(),'Рассмотреть посты')]")
    private WebElement tributesButton;
}
