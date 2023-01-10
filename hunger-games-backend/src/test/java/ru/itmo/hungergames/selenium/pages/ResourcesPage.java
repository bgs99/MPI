package ru.itmo.hungergames.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class ResourcesPage {
    private final WebDriver driver;

    public ResourcesPage(WebDriver driver) {
        this.driver = driver;
    }

    public static class ResourceRow {
        private final WebElement row;

        public ResourceRow(WebElement row) {
            this.row = row;
        }

        public String getName() {
            return this.row.findElements(By.tagName("td")).get(0).getText();
        }

        public WebElement getAmountInput() {
            return this.row.findElement(By.xpath(".//input[@type='number']"));
        }

        public int getUnitPrice() {
            final var sumStr = row.findElements(By.tagName("td")).get(2).getText();
            return Integer.parseInt(sumStr.substring(0, sumStr.length() - 1));
        }

        public int getSum() {
            final var sumStr = row.findElements(By.tagName("td")).get(3).getText();
            return Integer.parseInt(sumStr.substring(0, sumStr.length() - 1));
        }

        public boolean hasSum() {
            return row.findElements(By.tagName("td")).get(3).isDisplayed();
        }
    }

    public List<ResourceRow> getResourceRows() {
        return this.driver.findElements(By.tagName("tr")).stream().skip(1).map(ResourceRow::new).collect(Collectors.toList());
    }
}
