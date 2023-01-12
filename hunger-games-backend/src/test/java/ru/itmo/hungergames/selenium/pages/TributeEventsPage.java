package ru.itmo.hungergames.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

    public static class EventCard {
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

    public List<EventCard> getEventCards() {
        return this.driver.findElements(By.tagName("mat-card")).stream().map(EventCard::new).collect(Collectors.toList());
    }

    public void waitUntilEventsLoaded(int number) {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(By.tagName("mat-card"), number - 1));
    }

    public void waitUntilEventsLoaded() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(By.tagName("mat-card"), 0));
    }

    public void waitUtilElementsClickable(WebElement webElement) {
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(webElement));
    }

    public TributeEventsPage(WebDriver driver) {
        this.driver = driver;
    }
}
