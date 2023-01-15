package ru.itmo.hungergames.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ru.itmo.hungergames.model.entity.EventType;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TributeEventsPage {

    private final WebDriver driver;

    public WebElement getAddEventButton() {
        return this.driver.findElement(By.xpath("//button//*[contains(text(),'Добавить')]"));
    }

    public WebElement getApproveButton() {
        return this.driver.findElement(By.xpath("//button//*[contains(text(),'Готово')]"));
    }

    public WebElement getCancelButton() {
        return this.driver.findElement(By.xpath("//button//*[contains(text(),'Отмена')]"));
    }

    public WebElement getDateInput() {
        return this.driver.findElement(By.xpath("//input[@placeholder='Дата и время события']"));
    }

    public void enterDate(String text) {
        waitUtilElementsClickable(this.getDateInput());
        this.getDateInput().clear();
        this.getDateInput().sendKeys(text);
    }

    public WebElement getPlaceInput() {
        return this.driver.findElement(By.xpath("//input[@type='text']"));
    }

    public void enterPlace(String text) {
        waitUtilElementsClickable(this.getPlaceInput());
        this.getPlaceInput().clear();
        this.getPlaceInput().sendKeys(text);
    }

    public List<WebElement> getRadioButtons() {
        return this.driver.findElements(By.xpath("//input[@type='radio']"));
    }

    public List<EventCard> getEventCards() {
        return this.driver.findElements(By.tagName("mat-card")).stream().map(EventCard::new)
                .collect(Collectors.toList());
    }

    public void waitUntilEventsLoaded(int number) {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(By.tagName("mat-card"), number - 1));
    }

    public void waitUntilEventsLoaded() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(By.tagName("mat-card"), 0));
    }

    private static DateTimeFormatter timeFormatter = DateTimeFormatter
            .ofPattern("MM/dd/yyyy, HH:mm:ss")
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault());

    public void addEvent(EventType type, String place, Instant time) {
        this.getAddEventButton().click();
        switch (type) {
            case INTERVIEW:
                this.getRadioButtons().get(0).click();
                break;
            case MEETING:
                this.getRadioButtons().get(1).click();
                break;
        }
        this.enterPlace(place);
        this.enterDate(TributeEventsPage.timeFormatter.format(time));
        this.getApproveButton().click();
    }

    public void waitUtilElementsClickable(WebElement webElement) {
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(webElement));
    }

    public TributeEventsPage(WebDriver driver) {
        this.driver = driver;
    }
}
