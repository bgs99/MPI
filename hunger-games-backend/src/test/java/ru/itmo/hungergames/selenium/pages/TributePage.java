package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Getter
@Setter
public class TributePage {
    @FindBy(xpath = "//mat-card-title[contains(text(),'Трибут ')]")
    private WebElement tributeTitle;

    @FindBy(xpath = "//mat-card-subtitle[contains(text(),'Дистрикт ')]")
    private WebElement tributeDistrict;

    @FindBy(xpath = "//img[@mat-card-image]")
    private WebElement avatar;

    @FindBy(xpath = "//mat-list-item")
    private List<WebElement> posts;
}
