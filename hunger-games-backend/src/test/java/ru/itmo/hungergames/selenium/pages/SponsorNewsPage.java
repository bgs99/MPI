package ru.itmo.hungergames.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class SponsorNewsPage {
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

    @FindBy(tagName = "mat-card")
    private List<WebElement> rawEntries;

    public List<NewsEntry> getEntries() {
        return this.rawEntries.stream().map(NewsEntry::new).collect(Collectors.toList());
    }
}
