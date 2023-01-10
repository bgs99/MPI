package ru.itmo.hungergames.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class SponsorNewsPage {
    private final WebDriver webDriver;

    public SponsorNewsPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public static class NewsEntry{
        private final WebElement element;

        public NewsEntry(WebElement element) {
            this.element = element;
        }

        public String getTitle() {
            return this.element.findElement(By.tagName("mat-card-title")).getText();
        }

        public String getContents() {
            return this.element.findElement(By.tagName("mat-card-content")).getAttribute("innerHTML");
        }
    }

    public List<NewsEntry> getEntries() {
        return this.webDriver.findElements(By.tagName("mat-card")).stream().map(NewsEntry::new).collect(Collectors.toList());
    }
}
