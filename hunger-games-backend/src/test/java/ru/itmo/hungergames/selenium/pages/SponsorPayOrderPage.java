package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.itmo.hungergames.selenium.util.OrderRepresentation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SponsorPayOrderPage {
    @Getter
    @FindBy(id = "status")
    private WebElement paymentStatus;

    @Getter
    @FindBy(xpath = "//button//*[contains(text(), 'Вернуться')]")
    private WebElement goBackButton;

    @Getter
    @FindBy(xpath = "//button//*[contains(text(), 'Попробовать еще раз')]")
    private WebElement retryButton;

    @Getter
    @FindBy(xpath = "//button//*[contains(text(), 'Выбрать другие ресурсы')]")
    private WebElement reselectResourcesButton;

    @Getter
    @FindBy(xpath = "//button//*[contains(text(), 'Выбрать другого трибута')]")
    private WebElement reselectTributeButton;

    public static class OrderRow {
        private final WebElement row;

        public OrderRow(WebElement row) {
            this.row = row;
        }
        public WebElement getPayButton() {
            return row.findElement(By.tagName("button"));
        }

        public List<String> getDetails() {
            return row.findElements(By.tagName("mat-list-item")).stream().map(WebElement::getText).collect(Collectors.toList());
        }

        private String getDetailsText() {
            return row.findElements(By.tagName("mat-list-item")).stream().map(WebElement::getText).collect(Collectors.joining("\n"));
        }

        public int getSum() {
            final var sumStr = row.findElements(By.tagName("td")).get(1).getText();
            return Integer.parseInt(sumStr.substring(0, sumStr.length() - 1));
        }
    }

    @FindBy(tagName = "tr")
    private List<WebElement> orderElements;

    private Stream<OrderRow> getOrderRowsStream() {
        return this.orderElements.stream().skip(1).map(OrderRow::new);
    }

    public List<OrderRow> getOrderRows() {
        return this.getOrderRowsStream().collect(Collectors.toList());
    }

    public OrderRow getOrderRow(OrderRepresentation order) {
        return this.getOrderRowsStream()
                .filter(row -> row.getDetailsText().equals(order.toString()))
                .findFirst().orElseThrow();
    }

    public void payOrder(OrderRepresentation order) {
        this.getOrderRow(order).getPayButton().click();
    }
}
