package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class TributeMenuPage {
    @FindBy(xpath = "//button//*[contains(text(),'Чаты')]")
    private WebElement chatsButton;
    @FindBy(xpath = "//button//*[contains(text(),'События')]")
    private WebElement eventsButton;
    @FindBy(xpath = "//button//*[contains(text(),'Добавить пост')]")
    private WebElement addPostButton;
}
