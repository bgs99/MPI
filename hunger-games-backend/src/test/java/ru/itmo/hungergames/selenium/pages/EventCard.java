package ru.itmo.hungergames.selenium.pages;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class EventCard {
    private final WebElement card;

    public EventCard(WebElement card) {
        this.card = card;
    }

    public String getEventType() {
        return card.findElement(By.tagName("mat-card-title")).getText();
    }

    public Instant getEventDate() {
        String stringDate = card.findElement(By.tagName("mat-card-subtitle")).getText();
        String pattern = "dd.MM.uuuu, HH:mm:ss";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.parse(stringDate, dateTimeFormatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.toInstant();
    }

    public String getEventDateString() {
        return card.findElement(By.tagName("mat-card-subtitle")).getText();
    }

    public String getPlace() {
        return card.findElement(By.tagName("mat-card-content"))
                .findElement(By.tagName("div")).getText().substring(7);//starts with "Адрес: "
    }

    public WebElement getEditButton() {
        return card.findElement(By.xpath("//button//*[contains(text(),'Изменить')]"));
    }

    public WebElement getDeleteButton() {
        return card.findElement(By.xpath("//button//*[contains(text(),'Удалить')]"));
    }
}
