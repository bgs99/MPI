package ru.itmo.hungergames.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

public class SponsorTributesPage {
    private final WebDriver driver;

    public SponsorTributesPage(WebDriver driver) {
        this.driver = driver;
    }

    public static class TributeRow {
        private final WebElement row;

        public TributeRow(WebElement row) {
            this.row = row;
        }
        public WebElement getSelectButton() {
            return row.findElement(By.tagName("button"));
        }
        public String getName() {
            return row.findElements(By.tagName("td")).get(0).getText();
        }
    }

    public Map<String, TributeRow> getTributeRows() {
        return this.driver.findElements(By.tagName("tr")).stream().skip(1).map(TributeRow::new).collect(Collectors.toMap(TributeRow::getName, tributeRow -> tributeRow));
    }

    public void waitUntilTributesLoaded() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(By.tagName("tr"), 1));
    }
}
