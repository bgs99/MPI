package ru.itmo.hungergames.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.itmo.hungergames.selenium.util.OrderRepresentation;

import java.util.List;
import java.util.stream.Collectors;

public class ResourcesPage {
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

    @FindBy(tagName = "tr")
    private List<WebElement> resourceElements;

    public List<ResourceRow> getResourceRows() {
        return this.resourceElements.stream().skip(1).map(ResourceRow::new).collect(Collectors.toList());
    }

    public void clear() {
        this.resourceElements.stream().skip(1).map(ResourceRow::new).forEach(row -> row.getAmountInput().clear());
    }

    public ResourceRow getResourceRowByName(String name) {
        return this.resourceElements.stream().skip(1).map(ResourceRow::new)
                .filter(row -> row.getName().equals(name))
                .findFirst()
                .orElseThrow();
    }

    protected void createOrder(OrderRepresentation order) {
        this.clear();
        for (final var detail : order.details) {
            this.getResourceRowByName(detail.name).getAmountInput().sendKeys(String.valueOf(detail.amount));
        }
    }
}
