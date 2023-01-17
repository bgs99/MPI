package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class MentorMenuPage {
    @FindBy(xpath = "//button//*[contains(text(),'Рассмотреть предложения спонсоров')]")
    private WebElement considerSponsorOffers;
    @FindBy(xpath = "//button//*[contains(text(),'Запросить ресурсы')]")
    private WebElement requestResources;
    @FindBy(xpath = "//button//*[contains(text(),'Чаты')]")
    private WebElement chats;
}
