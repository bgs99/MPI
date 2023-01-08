package ru.itmo.hungergames.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class ChatsPage {
    private final WebDriver driver;

    public ChatsPage(WebDriver driver) {
        this.driver = driver;
    }

    public static class ChatRow {
        private final WebElement row;

        public ChatRow(WebElement row) {
            this.row = row;
        }
        public WebElement getSelectButton() {
            return row.findElement(By.tagName("button"));
        }
        public String getLastMessage() {
            return row.findElements(By.tagName("p")).get(0).getText();
        }
    }

    public List<ChatRow> getChatRows() {
        return this.driver.findElements(By.tagName("mat-list-item")).stream().map(ChatRow::new).collect(Collectors.toList());
    }

    public void waitUntilChatsLoaded() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(By.tagName("mat-list-item"), 0));
    }
}
