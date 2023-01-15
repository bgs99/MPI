package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class TributePage {
    @Getter
    @FindBy(xpath = "//mat-card-title[contains(text(),'Трибут ')]")
    private WebElement tributeTitle;

    @Getter
    @FindBy(xpath = "//mat-card-subtitle[contains(text(),'Дистрикт ')]")
    private WebElement tributeDistrict;

    @Getter
    @FindBy(xpath = "//img[@mat-card-image]")
    private WebElement avatar;

    @Getter
    @FindBy(xpath = "//mat-list-item[contains(@class, 'post')]")
    private List<WebElement> posts;


    @FindBy(tagName = "app-event")
    private List<WebElement> eventElements;

    public List<EventCard> getEventCards() {
        return this.eventElements.stream().map(EventCard::new).collect(Collectors.toList());
    }
}
