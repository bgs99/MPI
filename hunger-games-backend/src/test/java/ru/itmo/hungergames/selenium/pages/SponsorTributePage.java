package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
@Setter
public class SponsorTributePage extends TributePage {
    @FindBy(xpath = "//button//*[contains(text(),'Предложить ресурсы')]")
    private WebElement giveResourcesButton;

    @FindBy(xpath = "//button//*[contains(text(),'Оплатить ресурсы')]")
    private WebElement payResourcesButton;

    @FindBy(xpath = "//button//*[contains(text(),'Начать чат')]")
    private WebElement startChatButton;

    @FindBy(xpath = "//button//*[contains(text(),'Перейти к чату')]")
    private WebElement goToChatButton;
}
