package ru.itmo.hungergames.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.itmo.hungergames.selenium.util.OrderRepresentation;

import java.util.List;
import java.util.stream.Collectors;

public class MentorSuggestOrdersPage {
    @FindBy(tagName = "tr")
    private List<WebElement> resourceElements;

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
    }

    public List<ResourceRow> getResourceRows() {
        return this.resourceElements.stream().skip(1).map(ResourceRow::new).collect(Collectors.toList());
    }

    public ResourceRow getResourceRowByName(String resourceName) {
        return this.resourceElements.stream()
                .skip(1)
                .map(ResourceRow::new)
                .filter(row -> row.getName().equals(resourceName))
                .findFirst().orElseThrow();
    }

    @FindBy(xpath = "//h2[contains(text(), 'Получатель: ')]")
    private WebElement recipient;

    public String getRecipient() {
        return this.recipient.getText().substring("Получатель: ".length());
    }

    @FindBy(xpath = "//button//*[contains(text(), 'Запросить')]")
    private WebElement orderButton;

    public void clear() {
        for (final var row : this.getResourceRows()) {
            row.getAmountInput().clear();
        }
    }

    public void orderSelected() {
        this.orderButton.click();
    }

    public void createOrder(OrderRepresentation order) {
        this.clear();
        for (final var detail : order.details) {
            this.getResourceRowByName(detail.name)
                    .getAmountInput()
                    .sendKeys(String.valueOf(detail.amount));
        }
        this.orderButton.click();
    }
}
