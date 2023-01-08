package ru.itmo.hungergames.selenium.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TributePage {
    @FindBy(xpath = "//mat-card-title[contains(text(),'Трибут ')]")
    public WebElement tributeTitle;

    @FindBy(xpath = "//mat-card-subtitle[contains(text(),'Дистрикт ')]")
    public WebElement tributeDistrict;

    @FindBy(xpath = "//img[@mat-card-image]")
    public WebElement avatar;

    //TODO: Posts
}
