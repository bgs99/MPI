package ru.itmo.hungergames.selenium.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SponsorChatsPage extends ChatsPage {
    @FindBy(xpath = "//button//*[contains(text(),'Новый чат')]")
    public WebElement newChatButton;
}
